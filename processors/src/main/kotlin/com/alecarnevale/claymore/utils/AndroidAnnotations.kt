package com.alecarnevale.claymore.utils

import com.squareup.kotlinpoet.ClassName

internal val context = ClassName(packageName = "android.content", "Context")
internal val intent = ClassName(packageName = "android.content", "Intent")
internal val viewModelComponent = ClassName(packageName = "dagger.hilt.android.components", "ViewModelComponent")
internal val savedStateHandle = ClassName(packageName = "androidx.lifecycle", "SavedStateHandle")