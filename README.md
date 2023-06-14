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
With _claymore_ you can avoid to manually write that `Module`, using either `@AutoBinds` or `@InterfaceAutoBinds` annotation:

```
// you can directly annotate the class implementation
@AutoBinds
class MyImplementation: MyInterface
```

```
// otherwise, you can annotate the interface specifying which class implementation want to bind
@InterfaceAutoBinds(implementation = MyImplementation::class)
interface MyInterface
```

In both case, _clamyore_ will automatically generate necessary module for you.

Take a look at :demo module for a [sample usage](https://github.com/alecarnevale/claymore/tree/master/demo).

### Component
You can optionally request claymore to install the binding in a specific hilt component, using the `component` parameter.
```
@AutoBinds(component = ActivityComponent::class)
class MyImplementation: MyInterface
// or
@InterfaceAutoBinds(implementation = MyImplementation::class, component = ActivityComponent::class)
interface MyInterface
```
If not set, the `SingletonComponent` will be used by default.

## Installation

### Download
Claymore is available in Maven Central Repository:
- [annotations](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-annotations/1.3.2)
- [processors](https://central.sonatype.com/artifact/io.github.alecarnevale/claymore-processors/1.3.2)

### Gradle
```
repositories {
  mavenCentral()
}

dependencies {
  compileOnly 'io.github.alecarnevale:claymore-annotations:x.y.z'
}
```

### KSP integration
In order to completely enable `claymore` integration you need to apply ksp plugin

```
plugins {
  id 'com.google.devtools.ksp'
}

dependencies {
  ksp 'io.github.alecarnevale:claymore-processors:x.y.z'
}
```

## Thanks to
- KotlinPoet https://github.com/square/kotlinpoet
