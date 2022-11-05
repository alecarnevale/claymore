package com.alecarnevale.claymore.utils

import com.alecarnevale.claymore.Constants
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile

internal fun CodeGenerator.createFile(
  sourceFile: KSFile,
  fileText: String,
  fileName: String
) {
  val file = createNewFile(
    dependencies = Dependencies(
      aggregating = false,
      sourceFile,
    ),
    packageName = Constants.packageName,
    fileName = fileName
  )

  file.use { it.write(fileText.toByteArray()) }
}
