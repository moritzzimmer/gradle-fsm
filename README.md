![e-spirit logo](https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash3/s160x160/581307_346033565453595_1547840127_a.jpg)

# Gradle FSM plugin

[![Build Status](https://travis-ci.org/moritzzimmer/gradle-fsm.svg?branch=master)](https://travis-ci.org/moritzzimmer/gradle-fsm) 

[![Download](https://api.bintray.com/packages/kachelzaehler/gradle-plugins/gradle-fsm-plugin/images/download.png) ](https://bintray.com/kachelzaehler/gradle-plugins/gradle-fsm-plugin/_latestVersion)

[Gradle](http://www.gradle.org/) plugin to build [FirstSpirit](http://www.e-spirit.com/en/product/advantage/advantages.html) modules (FSMs).

## Usage

To use the plugin, include the following snippet on top of your build script:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies { classpath 'com.github.moritzzimmer:gradle-fsm-plugin:0.3.0' }
}
apply plugin: 'com.github.moritzzimmer.fsm'
```

## Project layout

This plugin applies the [Java Plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html), thus uses the same layout as any standard Java project using Gradle.

## Tasks

The plugin defines the following tasks:

Task | Depends on | Type | Description
:---:|:----------:|:----:| -----------
fsm  | jar        | FSM  | Assembles a fsm archive containing the FirstSpirit module.

## Extension properties

The plugin defines the following extension properties in the `fsm` closure:

Property | Type | Default | Description
:-------:|:----:|:-------:| -----------
moduleDirName  | String        | src/main/resources  |  The name of the directory containing the module.xml, relative to the project directory.

### Example

```groovy
fsm {
    // set a different directory containing the module.xml
    moduleDirName = 'src/main/module'
}
```

## module.xml

This plugin supports the following placeholders in the _module.xml_ which will be replaced at build time:

Placeholder | Value | Description
-------|-------|------------
$name | project.name | Name of the FSM
$version | project.version | Version of the FSM
$description | project.description | Description of the FSM
$artifact | project.jar.archiveName | Artifact (jar) name if the FSM 

### Example

```xml
<!DOCTYPE module PUBLIC "module.dtd" "platform:/resource/fs-module-support/src/main/resources/dtds/module.dtd">
<module>
    <name>$name</name>
    <version>$version</version>
    <description>$description</description>
    <components>
        <library>
            <resources>
                <resource>lib/$artifact</resource>
            </resources>
        </library>
    </components>
</module>
```

## Dependency management

The FSM plugin adds two dependency configurations: _fsProvidedCompile_ and _fsProvidedRuntime_. Those configurations have the same scope as the respective compile and runtime configurations, except that they are not added to the FSM archive.

### Example

```groovy
dependencies {
  // Library required to compile the production source code of 
  // this FSM which is provided by FirstSpirit. 
  fsProvidedCompile ('commons-logging:commons-logging:1.1.3')
}
```

## IDE support

### IntelliJ

The two custom configurations can be added to the [IntelliJ](http://www.jetbrains.com/idea/webhelp/gradle-2.html) classpath:

```groovy
apply plugin: 'idea'

// add libraries from custom FirstSpirit configurations to the IntelliJ classpath (Gradle 2.0 syntax)
idea.module.scopes.COMPILE.plus += [configurations.fsProvidedCompile]
idea.module.scopes.COMPILE.plus += [configurations.fsProvidedRuntime]
```

### Eclipse

The two custom configurations can be added to the [Eclipse](http://docs.spring.io/sts/docs/2.9.0.old/reference/html/gradle/gradle-sts-tutorial.html) classpath:

```groovy
apply plugin: 'eclipse'

eclipse {
    classpath {
        // add libraries from custom FirstSpirit configurations to the eclipse classpath (Gradle 2.0 syntax)
        plusConfigurations += [configurations.fsProvidedCompile]
        plusConfigurations += [configurations.fsProvidedRuntime]
    }
}
```

## Example

You can either use this [example project](https://github.com/moritzzimmer/gradle-fsm-example) as a starting point or the following snippet:

```groovy
buildscript {
    repositories {
        jcenter()
    }

    dependencies { classpath 'com.github.moritzzimmer:gradle-fsm-plugin:0.3.0' }
}
apply plugin: 'com.github.moritzzimmer.fsm'
apply plugin: 'eclipse'
apply plugin: 'idea'

description = 'Example FSM Gradle build'
version = '0.1.0'

repositories { mavenCentral() }

dependencies {
  compile ('joda-time:joda-time:2.3')

  runtime ()
  
  fsProvidedCompile ('commons-logging:commons-logging:1.1.3')

  fsProvidedRuntime ()
  
  testCompile (
  	'junit:junit:4.11'
  )
}

fsm {
    // example to set a different directory containing the module.xml
	// moduleDirName = 'src/main/module'
}

eclipse {
    classpath {
        // add libraries from custom FirstSpirit configurations to the eclipse classpath (Gradle 2.0 syntax)
        plusConfigurations += [configurations.fsProvidedCompile]
        plusConfigurations += [configurations.fsProvidedRuntime]
    }
}

// add libraries from custom FirstSpirit configurations to the IntelliJ classpath (Gradle 2.0 syntax)
idea.module.scopes.COMPILE.plus += [configurations.fsProvidedCompile]
idea.module.scopes.COMPILE.plus += [configurations.fsProvidedRuntime]
```

## Requirements

* [Java](http://www.java.com/en/download/) 1.6+
* [Gradle](http://www.gradle.org/downloads) 1.7+
