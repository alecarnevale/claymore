package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.providers.AutoProvidesProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutoProvidesProcessorProviderTest {

  @Test
  fun `GIVEN an interface Foo and a KeyProviderQualifier annotation Foo_AutoQualifier, WHEN @AutoProvides is applied to Bar with a Foo as activity class argument THEN an implementation of AutoProvidesKeysProvider for Foo is generated`() {
    val foo = SourceFile.kotlin(
      "Foo.kt",
      """
        package com.example

        class Foo
      """
    )

    val fooAutoQualifier = SourceFile.kotlin(
      "Foo_AutoQualifier.kt",
      """
        package com.example

        import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
        import javax.inject.Qualifier
        
        @KeyProviderQualifier(activityClass = Foo::class)
        @Qualifier
        annotation class Foo_AutoQualifier
      """
    )

    val bar = SourceFile.kotlin(
      "Bar.kt",
      """
        package com.example

        import android.content.Intent
        import com.alecarnevale.claymore.annotations.AutoProvides

        @AutoProvides(activityClass = Foo::class)
        interface Bar {
          operator fun invoke(): Intent
        }
      """,
    )

    val result = compileSourceFiles(foo, fooAutoQualifier, bar)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/Bar_AutoProvidesKeysProvider.kt")
    result.assertGeneratedContent(
      "com/example/Bar_AutoProvidesKeysProvider.kt",
      """
        package com.example
        
        import com.alecarnevale.claymore.annotations.AutoBinds
        import com.alecarnevale.claymore.api.AutoProvidesKeysProvider
        import javax.inject.Inject

        @AutoBinds(annotations = [Foo_AutoQualifier::class])
        internal class Bar_AutoProvidesKeysProvider @Inject constructor() : AutoProvidesKeysProvider

      """,
    )
  }

  private fun compileSourceFiles(vararg sourceFiles: SourceFile): KspCompilationResult {
    val kotlinCompilation = KotlinCompilation().apply {
      sources = sourceFiles.toList()
      symbolProcessorProviders = listOf(AutoProvidesProcessorProvider())
      inheritClassPath = true
    }
    return KspCompilationResult(
      sourcesDir = kotlinCompilation.kspSourcesDir,
      result = kotlinCompilation.compile(),
    )
  }
}