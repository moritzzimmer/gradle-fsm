gradle-fsm
==========

Example project for creating [FirstSpirit](http://www.e-spirit.com/en/product/advantage/advantages.html) modules using [Gradle](http://www.gradle.org/).

Tasks
-----

Using the provided project structure and build script, the FSM can be assembled using this task:
```groovy
task assembleFSM(type: Jar, dependsOn: jar, group: 'build', description: 'Assembles the FirstSpirit module') {
  extension = 'fsm'
  destinationDir = file('build/fsm')
  
  // include production code
  into ('lib') {
      from "${buildDir}/libs"
      include project.jar.archiveName
  }
  
  // include compile and runtime dependencies
  into ('lib') {
    from configurations.compile
    from configurations.runtime
    
    exclude { it.file in configurations.fsprovidedCompile.files }
    exclude { it.file in configurations.fsprovidedRuntime.files }
  }

  // configure META-INF directory
  metaInf {
    from 'src/main/resources/'
    include 'module.xml'
      
    // expand placeholders in module.xml
    expand(name: project.name,
      version: project.version,
      description: project.description,
      vendor: vendor,
      artifact: project.jar.archiveName)
  }
  
  // customize MANIFEST.MF as needed here
  manifest {
    attributes "Implementation-Version": version
  }
}
```
which can be executed directly (using the included [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html)):
`./gradlew clean assembleFSM` or by using the standard task of Gradles [Java Plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html):`./gradlew clean build`, 
`./gradlew clean assemble`

module.xml
----------

The required module definition is located under `src/main/resources` containg some placeholders which will be expanded at build time.

Custom configurations
---------------------

Besides the known configurations of the [Gradle Java Plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html), 
the script defines two additional custom configurations:

```groovy
configurations {
  /*
   * Custom configuration listing dependencies required to
   * compile the production source code of this FSM but are
   * provided by FirstSpirit.
   * 
   * Libraries added to this configuration won't be added
   * to the FSM.
   */
  fsprovidedCompile
  compile.extendsFrom(fsprovidedCompile)
  
  /*
   * Custom configuration listing dependencies required by
   * the production classes at runtime but are
   * provided by FirstSpirit.
   * 
   * Libraries added to this configuration won't be added
   * to the FSM.
   */
  fsprovidedRuntime
  runtime.extendsFrom(fsprovidedRuntime)
}
```

IDE support
-----------

The two custom configurations will be added to the classpath if imported into [Eclipse](http://docs.spring.io/sts/docs/2.9.0.old/reference/html/gradle/gradle-sts-tutorial.html):
```groovy
eclipse {
  classpath {
    // add libraries from custom FirstSpirit configurations to the eclipse classpath
    plusConfigurations += configurations.fsprovidedCompile
    plusConfigurations += configurations.fsprovidedRuntime
  }
}
```
