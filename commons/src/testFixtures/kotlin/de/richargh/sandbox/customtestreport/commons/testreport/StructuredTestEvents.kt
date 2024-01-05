package de.richargh.sandbox.customtestreport.commons.testreport

import java.util.function.Consumer

interface StructuredTestEvents {
    fun registerAfterEachTest(consumerName: String, callback: Consumer<StructuredTestReport>)

}