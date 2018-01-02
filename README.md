# Deprecated
### This project is not maintained anymore.


~~jarjar-gradle~~
=================

~~This plugin let's you easily use the jarjar tool in your gradle builds. This is handy for dependencies, which need to be repackaged, e.g. the full bouncy castle library for Android projects.~~

You setup the maven dependencies in in the `dependencies` section as usual. This plugin fetches and repackages them. The built `.jar` file is added to the `libs` folder.

Most credit goes to https://code.google.com/p/jarjar/ I've took their tool and wrote this wrapper.

Download
--------

Download [the latest version][1] or grab via Gradle:
```groovy
buildscript {
    repositories {
        maven {
            jcenter()
        }
    }
    dependencies {
        classpath 'net.vrallev.gradle:jarjar-gradle:1.1.0'
    }
}

apply plugin: 'net.vrallev.jarjar'

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

License
-------

    Copyright 2015 Ralf Wondratschek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: http://search.maven.org/#search|gav|1|g:"net.vrallev.gradle"%20AND%20a:"jarjar-gradle"