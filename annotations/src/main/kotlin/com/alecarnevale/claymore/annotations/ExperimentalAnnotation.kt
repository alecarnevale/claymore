package com.alecarnevale.claymore.annotations

@RequiresOptIn(
  "This annotation is experimental and is likely to change or to be removed in" +
    " the future."
)
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalAnnotation
