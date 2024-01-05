package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.nio.file.Path
import kotlin.io.path.name

@SmallScopeTest(SmallScopeTestInitializerExample::class)
@ReportAsAsciiDoc
class AsciidocTestReporterNestedFileCreationTest {

    @Nested
    inner class Nest {
        @Test
        fun `will create repo file for this test`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val expectedTestDirectory = reportDirectory.resolve(AsciidocTestReporterNestedFileCreationTest::class.simpleName!!)
            val expectedNestedDirectory = expectedTestDirectory.resolve(Nest::class.simpleName!!)
            val expectedReportFile = expectedNestedDirectory.resolve("${testInfo.displayName}.adoc")

            assertThat(reportDirectory.toFile().listFiles()).contains(expectedTestDirectory.toFile())
            val actualTestDirectory = reportDirectory.toFile().listFiles()!!.single { it.name == expectedTestDirectory.name }
            assertThat(actualTestDirectory.listFiles()).contains(expectedNestedDirectory.toFile())
            val actualNestedDirectory = actualTestDirectory.listFiles()!!.single { it.name == expectedNestedDirectory.name }
            assertThat(actualNestedDirectory.listFiles()).contains(expectedReportFile.toFile())
        }

    }


}
