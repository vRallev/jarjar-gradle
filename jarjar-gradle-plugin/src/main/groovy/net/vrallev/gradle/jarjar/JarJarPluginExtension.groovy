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

    boolean ignoreJarJarResult = false
    boolean verbose = false
    boolean skipManifest = false

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        JarJarPluginExtension that = (JarJarPluginExtension) o

        if (ignoreJarJarResult != that.ignoreJarJarResult) return false
        if (skipManifest != that.skipManifest) return false
        if (verbose != that.verbose) return false
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
        result = 31 * result + (ignoreJarJarResult ? 1 : 0)
        result = 31 * result + (verbose ? 1 : 0)
        result = 31 * result + (skipManifest ? 1 : 0)
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
                ", ignoreJarJarResult=" + ignoreJarJarResult +
                ", verbose=" + verbose +
                ", skipManifest=" + skipManifest +
                '}';
    }
}
