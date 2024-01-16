package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.providers.AutoUninstallProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutoUninstallProcessorProviderTest {

  @Test
  fun `WHEN @AutoUninstall is applied to a val, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall
      
      @AutoUninstall
      val foo: String = "this is a variable, not a class"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `WHEN @AutoUninstall is applied to a fun, THEN compilation error and hilt module is not generated`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall
      
      @AutoUninstall
      fun foo() = "this is a fun, not a class"
      """,
    )

    val result = compileSourceFiles(src)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an interface Foo WHEN @AutoUninstall is applied referencing Foo as implementation, THEN compilation error and hilt module is not generated`() {
    val fooSrc = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      interface Foo
      """,
    )
    val testModuleSrc = SourceFile.kotlin(
      "TestModule.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall
      
      @AutoUninstall(implementations = [Foo::class])
      interface TestModule
      """,
    )

    val result = compileSourceFiles(fooSrc, testModuleSrc)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN an abstract class Foo WHEN @AutoUninstall is applied referencing Foo as implementation, THEN compilation error and hilt module is not generated`() {
    val fooSrc = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      abstract class Foo
      """,
    )
    val testModuleSrc = SourceFile.kotlin(
      "TestModule.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall
      
      @AutoUninstall(implementations = [Foo::class])
      interface TestModule
      """,
    )

    val result = compileSourceFiles(fooSrc, testModuleSrc)

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.result.exitCode)
    result.assertZeroGeneratedSources()
  }

  @Test
  fun `GIVEN a class Bar, WHEN @AutoUninstall is applied referencing Bar as implementation, THEN hilt module is generated replacing BarModule and installing it in SingletonComponent as default component`() {
    // Bar is not needed to implements Foo interface for the purpose of AutoUninstallProcessor, even though in the real scenario it actually does
    // moreover BarModule is not required to exists in the code base, since AutoUninstallProcessor work using naming convention, not looking at the source code
    val barSrc = SourceFile.kotlin(
      "Bar.kt",
      """
      package com.example

      class Bar
      """,
    )
    val testModuleSrc = SourceFile.kotlin(
      "TestModule.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall
      
      @AutoUninstall(implementations = [Bar::class])
      interface TestModule
      """,
    )

    val result = compileSourceFiles(barSrc, testModuleSrc)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/TestModule_AutoUninstallModule.kt")
    result.assertGeneratedContent(
      "com/example/TestModule_AutoUninstallModule.kt",
      """
      package com.example
      
      import dagger.Module
      import dagger.hilt.testing.TestInstallIn
      
      @Module
      @TestInstallIn(
        components = [dagger.hilt.components.SingletonComponent::class],
        replaces = [com.example.BarModule::class],
      )
      internal object TestModule_AutoUninstallModule

      """,
    )
  }

  @Test
  fun `GIVEN two class Bar and Tar, and two component AlphaComponent and BetaComponent , WHEN @AutoUninstall is applied referencing Bar and Tar as implementation and AlphaComponent and BetaComponent as component, THEN hilt module is generated replacing BarModule and TarModule and installing them in AlphaComponent and BetaComponent`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall

      interface AlphaComponent
      
      interface BetaComponent

      class Bar

      class Tar
      """,
    )

    val testModuleSrc = SourceFile.kotlin(
      "TestModule.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall

      @AutoUninstall(
        implementations = [Bar::class, Tar::class],
        components = [AlphaComponent::class, BetaComponent::class],
      )
      interface TestModule
      """,
    )

    val result = compileSourceFiles(src, testModuleSrc)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/TestModule_AutoUninstallModule.kt")
    result.assertGeneratedContent(
      "com/example/TestModule_AutoUninstallModule.kt",
      """
      package com.example
      
      import dagger.Module
      import dagger.hilt.testing.TestInstallIn
      
      @Module
      @TestInstallIn(
        components = [com.example.AlphaComponent::class, com.example.BetaComponent::class],
        replaces = [com.example.BarModule::class, com.example.TarModule::class],
      )
      internal object TestModule_AutoUninstallModule

      """,
    )
  }

  @Test
  fun `WHEN @AutoUninstall is applied to TestModule, even if it's not the only annotation for TestModule THEN hilt module is generated replacing the module of the provided implementations and installing in provided components`() {
    val src = SourceFile.kotlin(
      "Foo.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall

      interface AlphaComponent
      
      interface BetaComponent

      class Bar

      class Tar
      """,
    )

    val testModuleSrc = SourceFile.kotlin(
      "TestModule.kt",
      """
      package com.example

      import com.alecarnevale.claymore.annotations.AutoUninstall

      @ExperimentalStdlibApi
      @AutoUninstall(
        implementations = [Bar::class, Tar::class],
        components = [AlphaComponent::class, BetaComponent::class],
      )
      @OptIn
      interface TestModule
      """,
    )

    val result = compileSourceFiles(src, testModuleSrc)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/TestModule_AutoUninstallModule.kt")
    result.assertGeneratedContent(
      "com/example/TestModule_AutoUninstallModule.kt",
      """
      package com.example
      
      import dagger.Module
      import dagger.hilt.testing.TestInstallIn
      
      @Module
      @TestInstallIn(
        components = [com.example.AlphaComponent::class, com.example.BetaComponent::class],
        replaces = [com.example.BarModule::class, com.example.TarModule::class],
      )
      internal object TestModule_AutoUninstallModule

      """,
    )
  }

  private fun compileSourceFiles(vararg sourceFiles: SourceFile): KspCompilationResult {
    val kotlinCompilation = KotlinCompilation().apply {
      sources = sourceFiles.toList()
      symbolProcessorProviders = listOf(AutoUninstallProcessorProvider())
      inheritClassPath = true
    }
    return KspCompilationResult(
      sourcesDir = kotlinCompilation.kspSourcesDir,
      result = kotlinCompilation.compile(),
    )
  }
}