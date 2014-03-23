package net.vrallev.gradle.jarjar

/**
 * User: Ralf Wondratschek
 */
class JarJarPluginExtension {

    String jarJarFile

    Set<String> rules
    Set<String> srcExcludes

    String outputName = 'build_repackaged.jar'
    String outputDir = 'libs'

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof JarJarPluginExtension)) return false

        JarJarPluginExtension that = (JarJarPluginExtension) o

        if (jarJarFile != that.jarJarFile) return false
        if (outputDir != that.outputDir) return false
        if (outputName != that.outputName) return false
        if (rules != that.rules) return false
        if (srcExcludes != that.srcExcludes) return false

        return true
    }

    int hashCode() {
        int result
        result = (jarJarFile != null ? jarJarFile.hashCode() : 0)
        result = 31 * result + (rules != null ? rules.hashCode() : 0)
        result = 31 * result + (srcExcludes != null ? srcExcludes.hashCode() : 0)
        result = 31 * result + (outputName != null ? outputName.hashCode() : 0)
        result = 31 * result + (outputDir != null ? outputDir.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "JarJarPluginExtension{" +
                "jarJarFile='" + jarJarFile + '\'' +
                ", rules=" + rules +
                ", srcExcludes=" + srcExcludes +
                ", outputName='" + outputName + '\'' +
                ", outputDir='" + outputDir + '\'' +
                '}';
    }
}
