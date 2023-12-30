package de.richargh.sandbox.customtestreport.commons.testreport

class InMemoryStructuredTestMarker: StructuredTestMarker {

    private val steps = mutableListOf<String>()

    fun steps(): List<String> {
        return steps
    }

    override fun step(name: String) {
        steps.add(name)
    }
}