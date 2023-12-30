package de.richargh.sandbox.customtestreport.commons.testdsl

import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.jvm.optionals.getOrNull

fun <T: Annotation> firstAnnotation(extensionContext: ExtensionContext, annotationClass: Class<T>): T? {
    var currentContext: ExtensionContext = extensionContext
    while (true) {
        val annotation = currentContext.element.get().getAnnotation(annotationClass)
        if(annotation != null)
            return annotation

        // top element is the JupiterEngineExtensionContext
        currentContext = currentContext.parent.getOrNull() ?: break
    }

    return null
}