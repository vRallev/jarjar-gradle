package net.vrallev.gradle.jarjar
import net.vrallev.gradle.jarjar.tasks.CreateRulesFileTask
import net.vrallev.gradle.jarjar.tasks.RepackageTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.bundling.Jar

/**
 * User: Ralf Wondratschek
 */
class JarJarPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        addConfiguration(project, 'jarjar')
        project.extensions.create('jarjar', JarJarPluginExtension)

        project.afterEvaluate {
            if (isJavaProject(project)) {
                project.tasks.compileJava.dependsOn project.task('runJarJar', type: RepackageTask)

            } else if (isAndroidProject(project)) {
                project.tasks.preBuild.dependsOn project.task('runJarJar', type: RepackageTask)

            } else {
                throw new GradleException('unknown plugin type')
            }

            project.tasks.runJarJar.dependsOn project.task('createRulesFile', type: CreateRulesFileTask)

            project.task('createRawFatJar', type: Jar) {

                getDependencies(project).each { File file ->
                    from project.zipTree(file)
                }

                if (getExtension(project).srcExcludes != null && !getExtension(project).srcExcludes.isEmpty()) {
                    excludes = getExtension(project).srcExcludes
                }
                destinationDir getOutputDirRaw(project)
                archiveName = getRawJar(project).name
            }

            // this task may be slow, so use hash check
            if (!hashAvailable(project)) {
                project.tasks.createRulesFile.dependsOn project.tasks.findByName('createRawFatJar')
            }
        }
    }

    private static boolean hashAvailable(Project project) {
        File hashDir = new File(getOutputDirRaw(project), 'hash')

        int hash = project.configurations.jarjar.getAsPath().hashCode()
        File hashFile = new File(hashDir, '' + hash)

        if (hashFile.exists()) {
            return true
        } else {
            hashDir.deleteDir()
            hashDir.mkdirs()
            hashFile.createNewFile()
            return false
        }
    }


    private static Configuration addConfiguration(Project project, String name) {
        Configuration configuration = project.configurations.create(name)
        configuration.visible = false
        // configuration.extendsFrom(project.configurations.getByName("compile"))
        return configuration
    }




    public static JarJarPluginExtension getExtension(Project project) {
        final JarJarPluginExtension config = project.jarjar
        config.outputDir = config.outputDir == null ? '' : config.outputDir
        return config
    }

    public static Set<File> getDependencies(Project project) {
        Set<File> files = new HashSet<>()
        project.configurations.jarjar.each { File file ->
            if (!file.name.endsWith('.jar')) {
                throw new ProjectConfigurationException('Only .jar dependencies are allowed! ' + file.absolutePath, null)
            }
            files.add(file)
        }
        return files
    }

    public static File getOutputDir(Project project) {
        File file = new File(project.buildDir, 'jarjar')
        file.mkdirs()
        return file
    }

    public static File getOutputDirRaw(Project project) {
        File file = new File(getOutputDir(project), 'raw')
        file.mkdirs()
        return file
    }

    public static File getRawJar(Project project) {
        return new File(getOutputDirRaw(project), 'raw_' + getExtension(project).outputName)
    }

    public static File getJarJarExeFile(Project project) {
        File jarJarExeFile = new File(project.projectDir, getExtension(project).jarJarFile)
        if (!jarJarExeFile.exists() || !jarJarExeFile.isFile()) {
            throw new ProjectConfigurationException('No executable JarJar .jar file found at ' + jarJarExeFile.absolutePath + '.', null)
        }
        return jarJarExeFile
    }

    public static File getRulesFile(Project project) {
        return new File(getOutputDir(project), 'rules.txt')
    }

    public static File getResultFile(Project project) {
        def ext = getExtension(project)
        return new File(project.projectDir, "$ext.outputDir$File.separator$ext.outputName")
    }

    public static boolean isJavaProject(Project project) {
        project.plugins.findPlugin('java')
    }

    public static boolean isAndroidProject(Project project) {
        project.plugins.findPlugin('com.android.application') || project.plugins.findPlugin('com.android.library')
    }
}
