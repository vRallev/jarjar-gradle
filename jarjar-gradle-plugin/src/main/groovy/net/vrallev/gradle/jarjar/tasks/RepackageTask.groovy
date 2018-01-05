package net.vrallev.gradle.jarjar.tasks
import net.vrallev.gradle.jarjar.JarJarPlugin
import net.vrallev.gradle.jarjar.JarJarPluginExtension
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile

/**
 * User: Ralf Wondratschek
 */
class RepackageTask extends DefaultTask {

    RepackageTask() {
        inputs.files(
                JarJarPlugin.getJarJarExeFile(project),
                JarJarPlugin.getRawJar(project),
                JarJarPlugin.getRulesFile(project)
        )
        outputs.file(JarJarPlugin.getResultFile(project))
    }

    @TaskAction
    def taskAction() {
        final JarJarPluginExtension config = JarJarPlugin.getExtension(project)
        final File jarJarExeFile = JarJarPlugin.getJarJarExeFile(project)

        final File rawFatJar = JarJarPlugin.getRawJar(project)
        final File rulesFile = JarJarPlugin.getRulesFile(project)
        final File outJar = JarJarPlugin.getResultFile(project)

        if (!rawFatJar.exists()) {
            project.tasks.findByName('createRawFatJar').execute()
        }

        project.exec {
            workingDir project.projectDir

            def extension = JarJarPlugin.getExtension(project)
            ignoreExitValue = extension.ignoreJarJarResult

            def args = []
            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                args.addAll 'cmd', '/c'
            }

            args.addAll 'java',
                    "-Dverbose=$extension.verbose",
                    '-jar', jarJarExeFile.absolutePath,
                    'process', rulesFile.absolutePath,
                    rawFatJar.absolutePath,
                    outJar.absolutePath
            commandLine args
        }

        String variantString = getVariant(project)
        if (variantString) {
            // add this for the very first build, otherwise compile task won't find repackaged jar
            project.android[variantString].all { variant ->
                JavaCompile compileTask = variant.getJavaCompile()
                if (!compileTask.classpath.contains(outJar)) {
                    compileTask.classpath = compileTask.classpath.plus(project.fileTree(dir: config.outputDir, include: [outJar.name]))
                }
            }
        }
    }

    private static String getVariant(Project project) {
        if (project.plugins.findPlugin("com.android.application")) {
            return "applicationVariants";
        } else if (project.plugins.findPlugin("com.android.library")) {
            return  "libraryVariants";
        } else {
            return null;
        }
    }
}
