package com.alessandro.claymore.demo.multiround.allies.generated

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.multiround.AutoProvidesKeysProvider
import com.alessandro.claymore.demo.multiround.allies.AlliesActivityIntent
import javax.inject.Inject
import kotlin.reflect.KClass

@AutoBinds(annotations = [AlliesActivity_AutoQualifier::class])
internal class AlliesActivity_AutoProvidesKeysProvider @Inject constructor() :
  AutoProvidesKeysProvider {
  override operator fun get(annotation: KClass<out Annotation>): String {
    return when (annotation) {
      AlliesActivityIntent.FirstAlly::class -> firstAlly
      AlliesActivityIntent.SecondAlly::class -> secondAlly
      else -> throw Exception("Unexpected Annotation")
    }
  }

  companion object {
    private const val firstAlly: String = "AlliesActivity_AutoProvidesKeysProvider_firstAlly"
    private const val secondAlly: String = "AlliesActivity_AutoProvidesKeysProvider_secondAlly"
  }
}