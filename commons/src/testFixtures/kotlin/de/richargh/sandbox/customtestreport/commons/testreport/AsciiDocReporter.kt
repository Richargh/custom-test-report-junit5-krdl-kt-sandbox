package de.richargh.sandbox.customtestreport.commons.testreport

import java.io.File
import java.nio.file.Path
import java.util.function.Consumer
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

annotation class ReportAsAsciiDoc(val reportFolder: String = "")

annotation class AsciiDocReportDirectory

class AsciiDocReporter(
    val folder: Path
): Consumer<StructuredTestReport> {

    override fun accept(testReport: StructuredTestReport) {
        val reportFile = resolve(folder, testReport.hierarchy)
        println("File: ${reportFile.absolutePath}")
        if(!reportFile.parentFile.exists())
            reportFile.parentFile.mkdirs()
        reportFile.createNewFile()
        reportFile.writeText("""
            
            = ${testReport.title}
            
            Epic: ${testReport.valueForLabel[EPIC_LABEL_NAME]}
            Feature: ${testReport.valueForLabel[FEATURE_LABEL_NAME]}
            Story: ${testReport.valueForLabel[STORY_LABEL_NAME]}
            
            
            """.trimIndent())
    }

    private fun resolve(folder: Path, testPath: List<String>): File {
        var current = folder
        testPath.forEachIndexed { index, name ->
            current = if(index + 1 == testPath.size)
                current.resolve("$name.adoc")
            else
                current.resolve(name)
        }
        return current.toFile()
    }

    companion object {
        val reporterName = AsciiDocReporter::class.simpleName!!
    }

}