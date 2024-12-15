# mada-java-gradle

Gradle plugin to configure projects to use mada java conventions

The intention is to simplify Gradle build files and avoid configuration duplication.


This plugin, while Open Source, is only intended to be used on projects under my control.

## Mada Opinionated Defaults

### Java Plugin

* Java source code encoding is UTF-8.
* Java classes retain the name of method parameters (compiled with option `-parameters`).
* Java sources are provided with artifacts.
* Javadoc is provided with artifacts.
* The latest version of the [jspecify](https://jspecify.dev/) artifact `org.jspecify:jspecify` is added to the `compileOnly` configuration.
* Task `preCommit` depends on relevant validation tasks (`check` and `javadoc` at present).
