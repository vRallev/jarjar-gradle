jarjar-gradle
=============

This plugin let's you easily use the jarjar tool in your gradle builds. This is handy for dependencies, which need to be repackaged, e.g. the full bouncy castle library for Android projects.

You setup the maven dependencies in in the `dependencies` section as usual. This plugin fetches and repackages them. The built `.jar` file is added to the `libs` folder.

Most credit goes to https://code.google.com/p/jarjar/ I've took their tool and wrote this wrapper.

Usage Maven Repo
----------------

I've uploaded the `.jar` in my maven repository. You only need to add following lines to your `build.gradle` to add the dependency:
```groovy
buildscript {
    repositories {
        maven {
            url 'https://raw.github.com/vRallev/mvn-repo/master/'
        }
    }
    dependencies {
        classpath 'net.vrallev.gradle:jarjar-gradle:1.0.0'
    }
}

apply plugin: 'jarjar'

jarjar {
	// required, path to the executable .jar file
    jarJarFile 'jarjar-1.4.jar'

    // optional, the rules for the jarjar tool
    rules = [
            'rule org.bouncycastle.** ext.org.bouncycastle.@1'
    ]
    
    // optional, exclude files from the dependency .jar files
    srcExcludes = ['META-INF/**']

    // optional, default is build_repackaged.jar, the result .jar file name
    outputName 'build_repackaged.jar'

    // optional, default is libs, the directory of the result .jar 
    outputDir 'libs'

    // optional, default is false
    ignoreJarJarResult false
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    jarjar 'org.bouncycastle:bcpg-jdk15on:1.50'
}
```

Compiling the library
---------------------

You can also clone the library and add it your local maven repository.
 
 1. Clone the repository.
 2. In the root project folder (`jarjar-gradle-plugin`) run `gradle uploadArchives`.
 3. Add the same plugin as above with the changed build script dependency:

```groovy
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath 'net.vrallev.gradle:jarjar-gradle:1.0.0-SNAPSHOT'
    }
}
``` 