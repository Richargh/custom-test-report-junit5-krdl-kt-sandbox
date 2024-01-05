package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Path
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
    inner class EmptySpaceContent {
        @Test
        fun `first and last line will be empty`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            val lines = result.readLines()
            assertThat(lines).first().isEqualTo("")
            assertThat(lines).last().isEqualTo("")
        }
    }

    @Nested
    inner class TitleContent {
        @Test
        fun `second line will contain title`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            assertThat(result.readLines()[1]).isEqualTo("= ${testInfo.displayName}")
        }
    }

    @Nested
    inner class EpicExistsContent {

        @Epic(EPIC_NAME)
        @Test
        fun `will contain name of epic`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            assertThat(result.readText()).contains("Epic: $EPIC_NAME")
        }
    }

    @Nested
    inner class FeatureExistsContent {

        @Feature(FEATURE_NAME)
        @Test
        fun `will contain name of feature`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            assertThat(result.readText()).contains("Feature: $FEATURE_NAME")
        }
    }



    @Nested
    inner class StoryExistsContent {

        @Story(STORY_NAME)
        @Test
        fun `will contain name of story`(
            testEvents: StructuredTestEvents,
            @AsciiDocReportDirectory reportDirectory: Path
        ) {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after(@AsciiDocReportDirectory reportDirectory: Path, testInfo: TestInfo) {
            val result = reportFile(reportDirectory, this::class, testInfo)
            assertThat(result.readText()).contains("Story: $STORY_NAME")
        }
    }

    companion object {
        private const val EPIC_NAME = "my-awesome-epic"
        private const val FEATURE_NAME = "my-awesome-feature"
        private const val STORY_NAME = "my-awesome-story"
    }

    private fun reportFile(@AsciiDocReportDirectory reportDirectory: Path, nest: KClass<*>, testInfo: TestInfo): File {
        return reportDirectory
            .resolve(this::class.simpleName!!)
            .resolve(nest.simpleName!!)
            .resolve("${testInfo.displayName}.adoc")
            .toFile()
    }
}