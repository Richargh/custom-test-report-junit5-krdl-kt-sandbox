package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


class StructuredTestReporterTest {

    @Nested
    @SmallScopeTest(SmallScopeTestInitializerExample::class)
    inner class ReportIsGenerated {

        @Test
        fun `report is logged`() {
            /** empty because asserts are in after **/
        }

        @AfterEach
        fun after() {
            assertThat(report).isNotNull
            assertThat(report.title).isEqualTo("report is logged()")
        }

        lateinit var report: StructuredTestReport

        @BeforeEach
        fun before(observer: StructuredTestObserver) {
            observer.registerAfterEachTest {
                report = it
            }
        }
    }

    @Nested
    inner class ReportEpic {

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class ExistingEpicIsPresent {

            @Epic("an epic name")
            @Test
            fun `existing epic is present`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[EPIC_LABEL_NAME]).isEqualTo("an epic name")
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class MissingEpicIsNull {

            @Test
            fun `missing epic is null`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[EPIC_LABEL_NAME]).isNull()
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

    }

    @Nested
    inner class ReportFeature {

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class ExistingFeatureIsPresent {

            @Feature("a feature name")
            @Test
            fun `existing feature is present`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[FEATURE_LABEL_NAME]).isEqualTo("a feature name")
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class MissingFeatureIsNull {

            @Test
            fun `missing feature is null`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[FEATURE_LABEL_NAME]).isNull()
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

    }

    @Nested
    inner class ReportStory {

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class ExistingStoryIsPresent {

            @Story("a story name")
            @Test
            fun `existing story is present`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[STORY_LABEL_NAME]).isEqualTo("a story name")
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class MissingFeatureIsNull {

            @Test
            fun `missing story is null`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.valueForLabel[STORY_LABEL_NAME]).isNull()
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestObserver) {
                observer.registerAfterEachTest {
                    report = it
                }
            }
        }

    }

}
