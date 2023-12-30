package de.richargh.sandbox.customtestreport.commons.testreport

import java.util.concurrent.ConcurrentLinkedDeque

class InMemoryStructuredTestObserver: StructuredTestObserver {

    private val observer = ConcurrentLinkedDeque<(report: StructuredTestReport) -> Unit>()

    override fun registerAfterEachTest(callback: (report: StructuredTestReport) -> Unit) {
        observer.add(callback)
    }

    fun onTestReport(structuredTestReport: StructuredTestReport){
        observer.forEach { callback -> callback(structuredTestReport) }
    }
}