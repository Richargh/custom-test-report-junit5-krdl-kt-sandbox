package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.reflect.KClass


@SmallScopeTest(SmallScopeTestInitializerExample::class)
@ReportAsAsciiDoc
class AsciidocTestReporterFileContentsTest {

    @Nested
    inner class ReportFileLocation {
        @Test
        fun `will create repo file in the expected folder`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            assertThat(reportFile(reportDirectory, this::class, testInfo)).exists()
        }
    }


    @Nested
    inner class Title {
        @Test
        fun `first line will contain title`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            assertThat(result.readLines()).first().isEqualTo("= ${testInfo.displayName}")
        }
    }

    private fun reportFile(@AsciiDocReportDirectory reportDirectory: Path, nest: KClass<*>, testInfo: TestInfo): File {
        return reportDirectory
            .resolve(this::class.simpleName!!)
            .resolve(nest.simpleName!!)
            .resolve("${testInfo.displayName}.adoc")
            .toFile()
    }
}