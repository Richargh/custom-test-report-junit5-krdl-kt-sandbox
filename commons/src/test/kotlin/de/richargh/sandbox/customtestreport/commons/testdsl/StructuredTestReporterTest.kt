package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*


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
        fun before(observer: StructuredTestEvents) {
            observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
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
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
                    report = it
                }
            }
        }

    }

    @Nested
    inner class ReportStep {

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class DslStepIsPresent {

            @Test
            fun `dsl logs a step`(a: TestStateExample) {
                a.name()
                /** asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.steps).containsExactly("a name")
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
                    report = it
                }
            }
        }

        @Nested
        @SmallScopeTest(SmallScopeTestInitializerExample::class)
        inner class DslStepIsEmpty {

            @Test
            fun `not calling dsl logs no step`() {
                /** empty because asserts are in after **/
            }

            @AfterEach
            fun after() {
                assertThat(report).isNotNull
                assertThat(report.steps).isEmpty()
            }

            lateinit var report: StructuredTestReport

            @BeforeEach
            fun before(observer: StructuredTestEvents) {
                observer.registerAfterEachTest("Tmp") {
                    report = it
                }
            }
        }

    }

}
