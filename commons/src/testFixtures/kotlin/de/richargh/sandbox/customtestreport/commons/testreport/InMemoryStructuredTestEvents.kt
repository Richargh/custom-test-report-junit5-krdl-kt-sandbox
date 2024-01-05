package de.richargh.sandbox.customtestreport.commons.testreport

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

class InMemoryStructuredTestEvents: StructuredTestEvents {

    private val observer = ConcurrentHashMap<String, Consumer<StructuredTestReport>>()

    override fun registerAfterEachTest(consumerName: String, callback: Consumer<StructuredTestReport>) {
        observer[consumerName] = callback
    }

    fun onTestReport(structuredTestReport: StructuredTestReport){
        observer.values.forEach { callback -> callback.accept(structuredTestReport) }
    }

    fun byName(consumerName: String): Consumer<StructuredTestReport>? =
        observer[consumerName]
}