### Releasing to Maven Central

Pre-requisites:
- GPG key configured for signing artifacts, eg. in `~/.gradle/gradle.properties`:
  - signing.keyId
  - signing.password
  - signing.secretKeyRingFile
- [JReleaser](https://jreleaser.org/) installed;
- Maven Central credentials configured in `~/.jreleaser/config.properties`:
  - JRELEASER_DEPLOY_MAVEN_MAVENCENTRAL_SONATYPE_USERNAME
  - JRELEASER_DEPLOY_MAVEN_MAVENCENTRAL_SONATYPE_PASSWORD

1. `./gradlew publish` to generate `build/staging-deploy` for both `annotations` and `processors` modules;
2. the artifacts are automatically signed (`sign*Publication`) during the publish step with the configured GPG key;
3. `jreleaser deploy` to deploy to Maven Central.