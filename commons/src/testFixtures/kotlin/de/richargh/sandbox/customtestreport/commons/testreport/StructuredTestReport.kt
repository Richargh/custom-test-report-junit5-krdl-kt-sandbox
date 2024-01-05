package de.richargh.sandbox.customtestreport.commons.testreport

data class StructuredTestReport(
    val id: String,
    val hierarchy: List<String>,
    val title: String,
    val valueForLabel: Map<String, String>,
    val steps: List<String>)
