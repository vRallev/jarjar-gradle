package net.vrallev.gradle.jarjar.tasks
import net.vrallev.gradle.jarjar.JarJarPlugin
import net.vrallev.gradle.jarjar.JarJarPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
/**
 * User: Ralf Wondratschek
 */
class CreateRulesFileTask extends DefaultTask {

    CreateRulesFileTask() {
        outputs.file(JarJarPlugin.getRulesFile(project).absolutePath)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    @TaskAction
    def taskAction() {

        final JarJarPluginExtension config = JarJarPlugin.getExtension(project)
        final File rulesFile = JarJarPlugin.getRulesFile(project)

        if (getRules() && !getRules().isEmpty()) {
            rulesFile.withWriter { out ->
                config.rules.each { String rule ->
                    out.writeLine(rule)
                }
            }
        }
    }

    @Input
    protected Set<String> getRules() {
        return JarJarPlugin.getExtension(project).rules
    }
}
