package com.alecarnevale.claymore.plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider

internal fun Project.libs(alias: String): Provider<MinimalExternalModuleDependency> {
  return extensions.getByType(VersionCatalogsExtension::class.java)
    .named("libs")
    .findLibrary(alias)
    .get()
}
