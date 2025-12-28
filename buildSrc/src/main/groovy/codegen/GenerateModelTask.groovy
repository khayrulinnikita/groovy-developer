package codegen

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class GenerateModelTask extends DefaultTask {

    @Input
    CodegenExtension extension

    @TaskAction
    void generate() {
        def targetDir = new File(extension.outputDir.path)
        targetDir.mkdirs()

        def file = new File(targetDir, "${extension.className}.groovy")
        file.text = generateFromTemplate(
                extension.className,
                extension.packageName,
                extension.fields
        )

        println("Generated: ${file}")
        println("File content: \n${file.text}")

    }

    static String generateFromTemplate(
            String className,
            String pkg,
            Map<String, String> fields
    ) {

        def template = """
package {{package}}

import groovy.transform.*

@Canonical
@ToString(includeNames = true)
//@Builder
class {{className}} {
    {{#fields}}
    {{type}} {{name}}
    {{/fields}}
    
    static {{className}} create(Map properties = [:]) {
        return new {{className}}(properties)
    }
    
    void update(Map updates) {
        updates.each { key, value ->
            if (this.hasProperty(key)) {
                this[key] = value
            }
        }
    }
}
"""
        template
                .replace("{{package}}", pkg)
                .replace("{{className}}", className)
                .replace("{{#fields}}", "")
                .replace("{{type}} {{name}}",
                        fields.collect { n, t -> "${t} ${n}" }.join("\n    ")
                )
                .replace("{{/fields}}", "")
    }
}
