plugins {
  id("org.jetbrains.kotlin.jvm") version "1.6.21"
  id("maven-publish")
  id("signing")

  `java-library`
}

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("nexus") {
      groupId = "io.github.alecarnevale"
      artifactId = "claymore"
      version = "1.0.0"
      from(components["java"])

      repositories {
        maven {
          name = "Nexus"
          url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") // Github Package
          credentials {
            username = properties["ossrhUsername"] as String
            password = properties["ossrhPassword"] as String

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
  sign(publishing.publications["nexus"])
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.google.guava:guava:31.0.1-jre")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  api("org.apache.commons:commons-math3:3.6.1")

  implementation("com.google.devtools.ksp:symbol-processing-api:1.6.20-1.0.5")
}
