package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.Epic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.lang.RuntimeException
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.createInstance

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SmallScopeTestExtension::class)
annotation class SmallScopeTest(val value: KClass<out SmallScopeTestInitializer>)

class SmallScopeTestExtension : ParameterResolver, AfterEachCallback {
    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val paramType = parameterContext.parameter.type
        if (TestState::class.java !in paramType.interfaces)
            return false

        if (smallScopeInitializer(extensionContext) != null)
            return true
        else
            throw DidNotProvideSmallScopeInitializer()
    }

    private fun smallScopeInitializer(extensionContext: ExtensionContext): KClass<out SmallScopeTestInitializer>? {
        val scopeAnnotation = firstAnnotation(extensionContext, SmallScopeTest::class.java)
            ?: throw SmallScopeAnnotationMissing()
        return if (SmallScopeTestInitializer::class in scopeAnnotation.value.allSuperclasses)
            scopeAnnotation.value
        else
            null
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        val smallScopeInitializer = smallScopeInitializer(extensionContext)?.createInstance()
            ?: throw DidNotProvideSmallScopeInitializer()

        println("Resolve: ${extensionContext.parent.get().displayName}")
        println(extensionContext.testInstance.get())
        println(extensionContext.parent.get().displayName)

        return smallScopeInitializer.testState()
    }

    override fun afterEach(extensionContext: ExtensionContext) {
        println("After Parent: ${extensionContext.parent.get().displayName}")
        println("After Parent Annotations: ${extensionContext.parent.get().element.get().annotations.joinToString()}")
        println("After Method: ${extensionContext.testMethod.get().name}")
        println("After Displayname: ${extensionContext.displayName}")
        println("After Testinstance: ${extensionContext.testInstance.get()}")
        println("After Element Declared Annotations: ${extensionContext.element.get().declaredAnnotations.joinToString()}")
        println(
            "After Element Epic value: ${
                extensionContext.element.get().getDeclaredAnnotation(Epic::class.java)?.value
            }"
        )
        println("After Element Annotations: ${extensionContext.element.get().annotations.joinToString()}")
        println("After Tags: ${extensionContext.tags}")
    }

}

class SmallScopeAnnotationMissing
    : RuntimeException("Either Test Method or Class must be annotated with ${SmallScopeTest::class.simpleName}")

class DidNotProvideSmallScopeInitializer
    : RuntimeException("Please provide an initializer that implements ${SmallScopeTestInitializer::class.simpleName}")