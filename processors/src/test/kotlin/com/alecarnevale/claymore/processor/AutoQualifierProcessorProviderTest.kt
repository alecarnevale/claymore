package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.providers.AutoQualifierProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutoQualifierProcessorProviderTest {

  @Test
  fun `GIVEN a class Foo, WHEN @AutoProvides is applied to Foo with a Bar activity class as argument THEN annotation with KeyProviderQualifier annotation is generated with Bar activity class as argument`() {
    val bar = SourceFile.kotlin(
      "Bar.kt",
      """
        package com.example

        class Bar
      """
    )

    val foo = SourceFile.kotlin(
      "Foo.kt",
      """
        package com.example

        import com.alecarnevale.claymore.annotations.AutoProvides

        @AutoProvides(activityClass = Bar::class)
        class Foo
      """,
    )

    val result = compileSourceFiles(foo, bar)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/Bar_AutoQualifier.kt")
    result.assertGeneratedContent(
      "com/example/Bar_AutoQualifier.kt",
      """
        package com.example
        
        import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
        import javax.inject.Qualifier

        @Qualifier
        @KeyProviderQualifier(Bar::class)
        internal annotation class Bar_AutoQualifier

      """,
    )
  }

  private fun compileSourceFiles(vararg sourceFiles: SourceFile): KspCompilationResult {
    val kotlinCompilation = KotlinCompilation().apply {
      sources = sourceFiles.toList()
      symbolProcessorProviders = listOf(AutoQualifierProcessorProvider())
      inheritClassPath = true
    }
    return KspCompilationResult(
      sourcesDir = kotlinCompilation.kspSourcesDir,
      result = kotlinCompilation.compile(),
    )
  }
}