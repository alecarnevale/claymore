# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - 2024-01-31

### Added
- Add `AutoUninstall` annotation to uninstall generated module when testing.
- New `AutoBinds.annotations` parameter to support annotations for generated binding function.

### Changed
- Set JDK level 17.
- Revamp demo project: split as multimodule project, describe new features.
- Update README.

### Removed
- Remove `InterfaceAutoBinds` annotation.

### Fixed
- Fix symbol validation: removed a useless expensive call to `KSNode.validate()` ksp check during validation phase.

### Internal changes:
- Migrate to Gradle version catalog.
- Integrate Dependabot group.
- Add a Gradle plugin to switch between local and remote version of claymore.
- Bump dependencies.

Deployed in Maven Central Repository:
- [annotations](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-annotations/2.0.0)
- [processors](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-processors/2.0.0)

## [1.3.2] - 2023-06-13

### Fixed
- Fix usage of `claymore-processors` for multi-module project.

Deployed in Maven Central Repository:
- [annotations](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-annotations/1.3.2)
- [processors](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-processors/1.3.2)

## [1.3.1] - 2023-06-09

### Fixed
- Set JDK level 11.

Deployed in Maven Central Repository:
- [annotations](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-annotations/1.3.1)
- [processors](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-processors/1.3.1)

## [1.3.0] - 2023-06-09

### Added
- `component` added to `InterfaceAutoBinds` and `AutoBinds` annotations.

### Changed
- `claymore` project splitted in two: `claymore-annotations` and `claymore-processors`.
- `InterfaceAutoBinds.implementationClass` renamed as `implementation`.

### Internal changes:
- Added github `dependabot.yml`.

Deployed in Maven Central Repository:
- [annotations](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-annotations/1.3.0)
- [processors](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-processors/1.3.0)

## [1.2.0] - 2023-05-23

### Added
- Create new AutoBinds annotation for implementation class.

### Changed
- `Autobind` annotation become `InterfaceAutoBinds`.
- Removed unused. 

### Fixed
- Fix for generated package name.

### Internal changes:
- Add tests.
- Integrate github action.
- Update gradle 8.1.1.
- Update dependencies.

Deployed in [Maven Central Repository](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore/1.2.0)

## [1.1.0] - 2022-11-05

### Internal changes:
- Use KotlinPoet.
- Removed unused dependencies.

Deployed in [Maven Central Repository](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore/1.1.0)

## [1.0.0] - 2022-11-04

First release

Deployed in [Maven Central Repository](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore/1.0.0)