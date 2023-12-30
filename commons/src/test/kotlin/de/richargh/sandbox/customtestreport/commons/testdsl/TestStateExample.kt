package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.StructuredTestMarker

class TestStateExample(
    private val marker: StructuredTestMarker): TestState {
    fun name(): String {
        marker.step("a name")
        return "example"
    }
}