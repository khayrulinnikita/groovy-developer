package codegen

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodegenPlugin implements Plugin<Project> {
    void apply(Project project) {
        def ext = project.extensions.create('codegen', CodegenExtension)
        project.afterEvaluate {
            def genTask = project.tasks.register('generateModel', GenerateModelTask) {
                it.extension = ext
            }

            project.sourceSets.main.groovy.srcDir(ext.outputDir)

            project.tasks.named('compileGroovy') {
                dependsOn(genTask)
            }
        }
    }
}