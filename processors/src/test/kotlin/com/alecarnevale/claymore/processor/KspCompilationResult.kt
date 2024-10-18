package com.alecarnevale.claymore.processor

import com.tschuchort.compiletesting.KotlinCompilation
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
internal data class KspCompilationResult(
  private val sourcesDir: File,
  val result: KotlinCompilation.Result,
) {
  val generatedSources: List<File> get() = sourcesDir.listFilesRecursively()

  private fun File.listFilesRecursively(): List<File> = listFiles().orEmpty().flatMap { file ->
    if (file.isDirectory) file.listFilesRecursively() else listOf(file)
  }

}

// because file is store in a tmp folder like /var/folders/.../ksp/sources/kotlin/
private val File.kspSourcePath: String get() = path.substringAfter("/ksp/sources/kotlin/")

internal fun KspCompilationResult.assertZeroGeneratedSources() = assertGeneratedSources()

internal fun KspCompilationResult.assertGeneratedSources(vararg generatedSources: String) {
  val sourcesPaths = this.generatedSources.map { file -> file.kspSourcePath }
  assertEquals(generatedSources.toSet(), sourcesPaths.toSet())
}

internal fun KspCompilationResult.assertGeneratedContent(sourcePath: String, @Language("kotlin") content: String) {
  val files = generatedSources.filter { file -> file.kspSourcePath == sourcePath }
  return when (files.size) {
    0 -> throw AssertionError("No files found for path $sourcePath.")
    1 -> assertEquals(content.trimIndent(), files.first().readText())
    else -> throw AssertionError("Multiple files found for path $sourcePath.")
  }
}