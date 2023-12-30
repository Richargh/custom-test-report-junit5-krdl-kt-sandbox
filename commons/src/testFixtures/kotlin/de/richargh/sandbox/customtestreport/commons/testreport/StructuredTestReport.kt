package de.richargh.sandbox.customtestreport.commons.testreport

data class StructuredTestReport(
    val id: String,
    val title: String,
    val valueForLabel: Map<String, String>)
