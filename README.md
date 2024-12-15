# mada-java-gradle

Custom convention Gradle plugin to configure mada java projects to my liking.

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

### DSL

Additional configuration available via DSL:

```groovy
madaJava {
    // publish to a local directory
    publishTo(file("build/dist"))

    // Configure all GenerateMavenPom tasks with common information
    pom {
        url         = 'https://github.com/jskov/reproducible-gradle'
        name        = mavenDisplayName
        description = mavenDescription

        developers {
            developer {
                id    = 'jskov'
                name  = 'Jesper Skov'
                email = 'jskov@mada.dk'
            }
        }

        licenses {
            license {
                name = 'Licensed under the EUPL-1.2-or-later'
                url  = 'https://joinup.ec.europa.eu/sites/default/files/custom-page/attachment/2020-03/EUPL-1.2%20EN.txt'
            }
        }

        scm {
            connection          = 'scm:git:git://github.com/jskov/reproducible-gradle.git'
            developerConnection = 'scm:git:ssh://github.com:jskov/reproducible-gradle.git'
            url                 = 'https://github.com/jskov/reproducible-gradle/'
        }
    }
}
```
