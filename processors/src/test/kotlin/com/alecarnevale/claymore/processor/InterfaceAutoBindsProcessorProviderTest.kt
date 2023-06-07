package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.processors.providers.InterfaceAutoBindsProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InterfaceAutoBindsProcessorProviderTest {

  @Test
  fun `WHEN @InterfaceAutoBinds is applied to a val, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds
      val foo: String = "this is a variable, not an interface"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `WHEN @InterfaceAutoBinds is applied to a fun, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds
      fun foo() = "this is a fun, not an interface"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `WHEN @InterfaceAutoBinds is applied to a class, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds
      class Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that doesn't implement Foo, WHEN @InterfaceAutoBinds is applied to Foo with Bar as implementationClass argument, THEN compilation error returned and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds(implementationClass = Bar::class)
      interface Foo

      class Bar
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN interfaces Foo, Tar and a class Bar that implements Tar, WHEN @InterfaceAutoBinds is applied to Foo with Bar as implementationClass argument, THEN compilation error returned and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds(implementationClass = Bar::class)
      interface Foo

      interface Tar

      class Bar: Tar
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @InterfaceAutoBinds is applied to Foo with Bar as implementationClass argument, THEN hilt module is generated with SingletonComponent as default component`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      
      @InterfaceAutoBinds(implementationClass = Bar::class)
      interface Foo

      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/FooModule.kt")
    result.assertGeneratedContent(
      "com/example/FooModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface FooModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @InterfaceAutoBinds is applied to Foo with Bar as implementationClass argument and with a specific component, THEN hilt module is generated using the provided component`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
      import dagger.hilt.components.SingletonComponent
      
      @InterfaceAutoBinds(implementationClass = Bar::class, component = SingletonComponent::class)
      interface Foo

      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/FooModule.kt")
    result.assertGeneratedContent(
      "com/example/FooModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface FooModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  private fun compileSourceFiles(vararg sourceFiles: SourceFile): KspCompilationResult {
    val kotlinCompilation = KotlinCompilation().apply {
      sources = sourceFiles.toList()
      symbolProcessorProviders = listOf(InterfaceAutoBindsProcessorProvider())
      inheritClassPath = true
    }
    return KspCompilationResult(
      sourcesDir = kotlinCompilation.kspSourcesDir,
      result = kotlinCompilation.compile(),
    )
  }
}