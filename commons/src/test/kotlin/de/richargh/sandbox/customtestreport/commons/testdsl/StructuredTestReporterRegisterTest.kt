package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createTempDirectory

class StructuredTestReporterRegisterTest {

    @Nested
    @SmallScopeTest(SmallScopeTestInitializerExample::class)
    inner class CanRegisterAsciidocObserverViaMethod {

        @Test
        fun `can register asciidoc observer via method`() {
            /** empty because asserts are in after **/
        }

        @BeforeEach
        fun before(testEvents: StructuredTestEvents) {
            testEvents.registerAfterEachTest(AsciiDocReporter.reporterName, AsciiDocReporter(createTempDirectory()))
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path) {
            assertThat(Files.list(reportDirectory)).isNotEmpty
        }
    }

    @Nested
    @SmallScopeTest(SmallScopeTestInitializerExample::class)
    @ReportAsAsciiDoc
    inner class CanRegisterAsciidocObserverViaAnnotation {

        @Test
        fun `can register asciidoc observer via annotation`(testEvents: StructuredTestEvents,
                                                        @AsciiDocReportDirectory reportDirectory: Path) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path) {
            assertThat(Files.list(reportDirectory)).isNotEmpty
        }

    }

}
