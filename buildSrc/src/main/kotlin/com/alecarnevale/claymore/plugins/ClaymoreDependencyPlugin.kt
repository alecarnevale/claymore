package com.alecarnevale.claymore.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

internal class ClaymoreDependencyPlugin: Plugin<Project> {
  override fun apply(project: Project) {
    val fetchProp = project.properties.containsKey("fetchRemoteDependency")

    if (fetchProp) {
      project.logger.log(LogLevel.LIFECYCLE, "You are using remote claymore version")
      project.dependencies.add("compileOnly", project.libs("claymore.annotations"))
      project.dependencies.add("ksp", project.libs("claymore.processors"))
      project.dependencies.add("testCompileOnly", project.libs("claymore.annotations"))
      project.dependencies.add("kspTest", project.libs("claymore.processors"))
    } else {
      project.logger.log(LogLevel.LIFECYCLE, "You are using local claymore version")
      project.dependencies.add("compileOnly", project.project(":annotations"))
      project.dependencies.add("ksp", project.project(":processors"))
      project.dependencies.add("testCompileOnly", project.project(":annotations"))
      project.dependencies.add("kspTest", project.project(":processors"))
    }
  }

  companion object
}