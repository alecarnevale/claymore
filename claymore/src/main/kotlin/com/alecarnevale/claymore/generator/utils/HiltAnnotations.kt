package com.alecarnevale.claymore.generator.utils

internal fun StringBuilder.moduleAnnotations(): StringBuilder {
  return appendLine("@Module")
    .appendLine("@InstallIn(SingletonComponent::class)")
}

internal fun StringBuilder.bindAnnotation(): StringBuilder {
  return appendLine("@Binds")
}