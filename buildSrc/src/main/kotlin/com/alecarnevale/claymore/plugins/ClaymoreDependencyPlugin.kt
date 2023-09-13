package com.alecarnevale.claymore.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ClaymoreDependencyPlugin: Plugin<Project> {
  override fun apply(project: Project) {
    val fetchProp = project.properties.containsKey("fetchRemoteDependency")

    if (fetchProp) {
      project.dependencies.add("compileOnly", project.libs("claymore.annotation"))
      project.dependencies.add("ksp", project.libs("claymore.processors"))
    } else {
      project.dependencies.add("compileOnly", project.project(":annotations"))
      project.dependencies.add("ksp", project.project(":processors"))
    }
  }

  companion object
}