package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.providers.AutoBindsProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutoBindsProcessorProviderTest {

  @Test
  fun `WHEN @AutoBinds is applied to a val, THEN compilation error and hilt module is not generated`() {
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
  fun `WHEN @AutoBinds is applied to a fun, THEN compilation error and hilt module is not generated`() {
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
  fun `WHEN @AutoBinds is applied to an interface, THEN compilation error and hilt module is not generated`() {
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
  fun `GIVEN an interface Foo and a class Bar that doesn't implement Foo, WHEN @AutoBinds is applied to Bar, THEN compilation error and hilt module is not generated`() {
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
  fun `GIVEN an interface Foo and an abstract class Bar that implements Foo, WHEN @AutoBinds is applied to Bar, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      
      interface Foo

      @AutoBinds
      abstract class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar, THEN hilt module is generated with SingletonComponent as default component`() {
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

    result.assertGeneratedSources("com/example/BarModule.kt")
    result.assertGeneratedContent(
      "com/example/BarModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface BarModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar with a specific component, THEN hilt module is generated using the provided component`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      
      interface Foo

      @AutoBinds(component = SingletonComponent::class)
      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/BarModule.kt")
    result.assertGeneratedContent(
      "com/example/BarModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface BarModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN an abstract class Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar with a specific component, THEN hilt module is generated using the provided component`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      
      abstract class Foo

      @AutoBinds(component = SingletonComponent::class)
      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/BarModule.kt")
    result.assertGeneratedContent(
      "com/example/BarModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface BarModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar with a specific component, even if it's not the only annotation for Bar THEN hilt module is generated using the provided component`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      
      interface Foo

      @ExperimentalStdlibApi
      @AutoBinds(component = SingletonComponent::class)
      @OptIn
      class Bar: Foo {
        fun unresolvedSymbolMustNotBotherUs(unresolved: Unresolved) {}
      }
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/BarModule.kt")
    result.assertGeneratedContent(
      "com/example/BarModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface BarModule {
        @Binds
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar with a list of wrong annotations, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      
      interface Foo

      object ThisIsNotAnnotation

      @AutoBinds(component = SingletonComponent::class, annotations = [ThisIsNotAnnotation::class])
      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo and a class Bar that implements Foo, WHEN @AutoBinds is applied to Bar with a list of annotations, THEN hilt module is generated and the binding function is annotated with all annotations`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      import dagger.multibindings.IntoSet
      import javax.inject.Singleton
      
      interface Foo

      annotation class CustomQualifier

      @AutoBinds(component = SingletonComponent::class, annotations = [IntoSet::class, Singleton::class, CustomQualifier::class])
      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/BarModule.kt")
    result.assertGeneratedContent(
      "com/example/BarModule.kt",
      """
      package com.example
      
      import dagger.Binds
      import dagger.Module
      import dagger.hilt.InstallIn
      import dagger.hilt.components.SingletonComponent
      import dagger.multibindings.IntoSet
      import javax.inject.Singleton
      
      @Module
      @InstallIn(SingletonComponent::class)
      internal interface BarModule {
        @Binds
        @IntoSet
        @Singleton
        @CustomQualifier
        public fun foo(`impl`: Bar): Foo
      }

      """,
    )
  }

  @Test
  fun `GIVEN two interface Foo and Faz and a class Bar that implements both interfaces, WHEN @AutoBinds is applied to Bar, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent
      
      interface Foo
      
      interface Faz

      @AutoBinds(component = SingletonComponent::class)
      class Bar: Foo, Faz
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN a class Bar that implements a unknown interface Foo, WHEN @AutoBinds is applied to Bar, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoBinds
      import dagger.hilt.components.SingletonComponent

      @AutoBinds(component = SingletonComponent::class)
      class Bar: Foo
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
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