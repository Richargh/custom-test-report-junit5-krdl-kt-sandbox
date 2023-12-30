package de.richargh.sandbox.customtestreport.commons.testreport

interface StructuredTestObserver {
    fun registerAfterEachTest(callback: (report: StructuredTestReport) -> Unit)

}