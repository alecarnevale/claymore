plugins {
  alias(libs.plugins.kotlin)
  id("maven-publish")
  id("signing")
}

java {
  withJavadocJar()
  withSourcesJar()
}

kotlin {
  jvmToolchain(11)
}

publishing {
  publications {
    create<MavenPublication>("processors") {
      groupId = "io.github.alecarnevale"
      artifactId = "claymore-processors"
      version = "1.3.2"
      from(components["java"])

      repositories {
        maven {
          name = "Nexus"
          url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") // Github Package
          credentials {
            username = properties["ossrhUsername"] as String?
            password = properties["ossrhPassword"] as String?

          }
        }
      }

      pom {
        name.set("Claymore")
        description.set("A library that writes an hilt module for a requested interface implementation.")
        url.set("https://github.com/alecarnevale/claymore")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("alecarnevale")
            name.set("Alessandro Carnevale")
            email.set("alecarnevale.dev@gmail.com")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/alecarnevale/claymore.git")
          developerConnection.set("scm:git:ssh://github.com/alecarnevale/claymore.git")
          url.set("https://github.com/alecarnevale/claymore")
        }
      }
    }
  }
}

signing {
  sign(publishing.publications["processors"])
}

dependencies {
  // despite the fact those are used only at compile time they cannot be compileOnly,
  // otherwise access to java class won't be feasible
  implementation(libs.dagger)
  implementation(libs.hilt.core)

  implementation(project(":annotations"))

  implementation(libs.ksp)
  implementation(libs.bundles.kotlinpoet)

  testImplementation(libs.junit.jupiter)
  testImplementation(libs.ksp.testing)
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}