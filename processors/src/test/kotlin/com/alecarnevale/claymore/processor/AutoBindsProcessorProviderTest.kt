package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.processors.providers.AutoBindsProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutoBindsProcessorProviderTest {

  @Test
  fun `WHEN @AutoBinds is applied to a val, THEN compilation error and hilt module are not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      @AutoBinds
      val foo: String = "this is a variable, not a class"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `WHEN @AutoBinds is applied to a fun, THEN compilation error and hilt module are not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      @AutoBinds
      fun foo() = "this is a fun, not a class"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `WHEN @AutoBinds is applied to an interface, THEN compilation error and hilt module are not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      @AutoBinds
      interface Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that doesn't implement Foo, WHEN @AutoBinds is applied to Bar, THEN compilation error and hilt module are not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      interface Foo

      @AutoBinds
      class Bar
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar, THEN hilt module are generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      interface Foo

      @AutoBinds
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
      symbolProcessorProviders = listOf(AutoBindsProcessorProvider())
      inheritClassPath = true
    }
    return KspCompilationResult(
      sourcesDir = kotlinCompilation.kspSourcesDir,
      result = kotlinCompilation.compile(),
    )
  }
}