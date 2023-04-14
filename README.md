# CLAYMORE

A library that writes an hilt module for a requested interface implementation.

## Usage
Pre: Android application using [Hilt](https://dagger.dev/hilt/) for dependencies injection.

### Scenario
In a typical use case, you have:
```
interface MyInterface

---

class MyImplementation @Inject constructor() : MyInterface

```

and you need to write a simple module only to bind right implementation:
```
@Module
@InstallIn(SingletonComponent::class)
interface MyModule {
  @Binds
  ...
}
```

### Benefit
With _claymore_ you can avoid to manually write that `Module`, using `@InterfaceAutoBinds` annotation instead:

```
@InterfaceAutoBinds(implementationClass = MyImplementation::class)
interface MyInterface
```
_clamyore_ will automatically generate necessary module for you.

Take a look at :demo module for a [sample usage](https://github.com/alecarnevale/claymore/tree/master/demo/src/main/java/com/alessandro/claymore/demo).

## Installation

### Download
Claymore is available in [Maven Central Repository](https://central.sonatype.dev/artifact/io.github.alecarnevale/claymore/1.0.0/overview).

### Gradle
```
repositories {
  mavenCentral()
}

dependencies {
  implementation 'io.github.alecarnevale:claymore:x.y.z'
}
```

### KSP integration
In order to completely enable `claymore` integration you need to apply ksp plugin

```
plugins {
  id 'com.google.devtools.ksp'
}

dependencies {
  ksp 'io.github.alecarnevale:claymore:x.y.z'
}
```
