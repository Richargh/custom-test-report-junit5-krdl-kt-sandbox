package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.StructuredTestMarker

class SmallScopeTestInitializerExample: SmallScopeTestInitializer {

    override fun testState(structuredTestMarker: StructuredTestMarker): TestState =
        TestStateExample(structuredTestMarker)

}
