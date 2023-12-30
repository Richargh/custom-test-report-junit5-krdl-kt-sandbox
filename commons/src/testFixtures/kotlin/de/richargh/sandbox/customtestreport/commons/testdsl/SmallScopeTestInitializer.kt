package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.StructuredTestMarker

interface SmallScopeTestInitializer {
    fun testState(structuredTestMarker: StructuredTestMarker): TestState

}