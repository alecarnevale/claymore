plugins {
  id("org.jetbrains.kotlin.jvm")
  id("maven-publish")
  id("signing")
}

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("processors") {
      groupId = "io.github.alecarnevale"
      artifactId = "claymore-processors"
      version = "1.2.0"
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
  implementation(project(":annotations"))

  implementation("com.google.devtools.ksp:symbol-processing-api:1.8.21-1.0.11")
  implementation("com.squareup:kotlinpoet:1.13.2")
  implementation("com.squareup:kotlinpoet-ksp:1.13.2")

  testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}