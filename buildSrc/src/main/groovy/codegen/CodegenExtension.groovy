package codegen

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory

class CodegenExtension {
    @Input
    String className

    @Input
    String packageName

    @OutputDirectory
    File outputDir

    @Input
    Map<String, String> fields = [:]
}