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
  fun `GIVEN an class Foo, an interface Bar with invoke function, and a KeyProviderQualifier annotation Foo_AutoQualifier, WHEN @AutoProvides is applied to Bar with a Foo as activity class argument THEN an implementation of AutoProvidesKeysProvider for Foo is generated`() {
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
          @Qualifier
          annotation class FirstQualifier
        
          @Qualifier
          annotation class SecondQualifier

          operator fun invoke(
            @FirstQualifier firstParameter: String,
            @SecondQualifier secondParameter: String,
          ): Intent
        }
      """,
    )

    val result = compileSourceFiles(foo, fooAutoQualifier, bar)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/Bar_AutoProvidesKeysProvider.kt", "com/example/Bar_AutoIntentImpl.kt")
    result.assertGeneratedContent(
      "com/example/Bar_AutoProvidesKeysProvider.kt",
      """
        package com.example
        
        import com.alecarnevale.claymore.annotations.AutoBinds
        import com.alecarnevale.claymore.annotations.keyprovider.AutoProvidesKeysProvider
        import com.example.Bar.FirstQualifier
        import com.example.Bar.SecondQualifier
        import javax.inject.Inject
        import kotlin.Annotation
        import kotlin.String
        import kotlin.reflect.KClass

        @AutoBinds(annotations = [Foo_AutoQualifier::class])
        internal class Bar_AutoProvidesKeysProvider @Inject constructor() : AutoProvidesKeysProvider {
          override operator fun `get`(`annotation`: KClass<out Annotation>): String = when(annotation) {
            FirstQualifier::class -> firstParameter
            SecondQualifier::class -> secondParameter
            else -> throw Exception("Unexpected Annotation")
          }
        
          private companion object {
            private const val firstParameter: String = "Bar_AutoProvidesKeysProvider_firstParameter"

            private const val secondParameter: String = "Bar_AutoProvidesKeysProvider_secondParameter"
          }
        }

      """,
    )
  }

  @Test
  fun `GIVEN an class Foo, an interface Bar with invoke function, and a KeyProviderQualifier annotation Foo_AutoQualifier, WHEN @AutoProvides is applied to Bar with a Foo as activity class argument THEN an implementation of Bar is generated`() {
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
          @Qualifier
          annotation class FirstQualifier
        
          @Qualifier
          annotation class SecondQualifier

          operator fun invoke(
            @FirstQualifier firstParameter: String,
            @SecondQualifier secondParameter: String,
          ): Intent
        }
      """,
    )

    val result = compileSourceFiles(foo, fooAutoQualifier, bar)

    assertEquals(KotlinCompilation.ExitCode.OK, result.result.exitCode)

    result.assertGeneratedSources("com/example/Bar_AutoProvidesKeysProvider.kt", "com/example/Bar_AutoIntentImpl.kt")
    result.assertGeneratedContent(
      "com/example/Bar_AutoIntentImpl.kt",
      """
        package com.example
        
        import android.content.Intent
        import com.alecarnevale.claymore.annotations.AutoBinds
        import javax.inject.Inject
        import kotlin.String

        @AutoBinds
        internal class Bar_AutoIntentImpl @Inject constructor() : Bar {
          override operator fun invoke(firstParameter: String, secondParameter: String): Intent {
          }
        }

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