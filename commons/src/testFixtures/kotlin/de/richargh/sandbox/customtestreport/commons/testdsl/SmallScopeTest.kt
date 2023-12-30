package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.createInstance


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SmallScopeTestExtension::class)
annotation class SmallScopeTest(val value: KClass<out SmallScopeTestInitializer>)

class SmallScopeTestExtension : ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        isTestState(parameterContext)
                || isTestObserver(parameterContext)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return when {
            isTestState(parameterContext) -> storeFor(extensionContext).testState()
            isTestObserver(parameterContext) -> storeFor(extensionContext).testObserver()
            else -> throw TriedToResolveUnsupportedParamter(parameterContext.parameter.type)
        }
    }

    private fun isTestState(parameterContext: ParameterContext) =
        TestState::class.java in parameterContext.parameter.type.interfaces

    private fun isTestObserver(parameterContext: ParameterContext) =
        parameterContext.parameter.type === StructuredTestObserver::class.java

    override fun beforeEach(context: ExtensionContext) {
        val smallScopeInitializer = smallScopeInitializer(context)?.createInstance()
            ?: throw DidNotProvideSmallScopeInitializer()
        storeFor(context).initTestMarker()
        storeFor(context).initTestObserver()
        storeFor(context).initTestDsl(smallScopeInitializer, storeFor(context).testMarker())
    }

    override fun afterTestExecution(context: ExtensionContext) {
        val valueForLabel = mutableMapOf<String, String>()

        val element = context.element.get()
        element.getDeclaredAnnotation(Epic::class.java)?.value?.let { valueForLabel[EPIC_LABEL_NAME] = it }
        element.getDeclaredAnnotation(Feature::class.java)?.value?.let { valueForLabel[FEATURE_LABEL_NAME] = it }
        element.getDeclaredAnnotation(Story::class.java)?.value?.let { valueForLabel[STORY_LABEL_NAME] = it }

        val steps = storeFor(context).testMarker().steps()

        storeFor(context).testObserver().onTestReport(
            StructuredTestReport(
                id = context.uniqueId,
                title = context.displayName,
                valueForLabel = valueForLabel,
                steps = steps
            )
        )
    }

//    override fun afterEach(context: ExtensionContext) {
//        println("After Parent: ${context.parent.get().displayName}")
//        println("After Parent Annotations: ${context.parent.get().element.get().annotations.joinToString()}")
//        println("After Method: ${context.testMethod.get().name}")
//        println("After Displayname: ${context.displayName}")
//        println("After Testinstance: ${context.testInstance.get()}")
//        println("After Element Declared Annotations: ${context.element.get().declaredAnnotations.joinToString()}")
//        println("After Element Annotations: ${context.element.get().annotations.joinToString()}")
//        println("After Tags: ${context.tags}")
//    }

    private fun smallScopeInitializer(extensionContext: ExtensionContext): KClass<out SmallScopeTestInitializer>? {
        val scopeAnnotation = firstAnnotation(extensionContext, SmallScopeTest::class.java)
            ?: throw SmallScopeAnnotationMissing()
        return if (SmallScopeTestInitializer::class in scopeAnnotation.value.allSuperclasses)
            scopeAnnotation.value
        else
            null
    }

    private fun storeFor(context: ExtensionContext): Store {
        return context.getStore(Namespace.create(javaClass, context.requiredTestMethod))
    }

}

private fun Store.initTestDsl(scopeTestInitializer: SmallScopeTestInitializer, testMarker: StructuredTestMarker) {
    put(TEST_STATE_KEY, scopeTestInitializer.testState(testMarker))
}

private fun Store.testState() = get(TEST_STATE_KEY, TestState::class.java)

private fun Store.initTestObserver() {
    put(OBSERVER_KEY, InMemoryStructuredTestObserver())
}

private fun Store.testObserver() = get(OBSERVER_KEY, InMemoryStructuredTestObserver::class.java)

private fun Store.initTestMarker() {
    put(TEST_MARKER_KEY, InMemoryStructuredTestMarker())
}

private fun Store.testMarker() = get(TEST_MARKER_KEY, InMemoryStructuredTestMarker::class.java)

private const val OBSERVER_KEY = "Observer"
private const val TEST_MARKER_KEY = "Test Marker"
private const val TEST_STATE_KEY = "Test Dsl"

class SmallScopeAnnotationMissing
    : RuntimeException("Either Test Method or Class must be annotated with ${SmallScopeTest::class.simpleName}")

class DidNotProvideSmallScopeInitializer
    : RuntimeException("Please provide an initializer that implements ${SmallScopeTestInitializer::class.simpleName}")

class TriedToResolveUnsupportedParamter(unsupportedParamter: Class<*>) :
    RuntimeException("JUnit did something weird and tried to resolve unknown parameter: ${unsupportedParamter.simpleName}")
