package de.richargh.sandbox.customtestreport.commons.testdsl

import de.richargh.sandbox.customtestreport.commons.testreport.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store
import java.util.Properties
import java.util.function.Consumer
import kotlin.io.path.Path
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.createInstance


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SmallScopeTestExtension::class)
annotation class SmallScopeTest(val value: KClass<out SmallScopeTestInitializer>)

class SmallScopeTestExtension : ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return isTestState(parameterContext)
                || isTestObserver(parameterContext)
                || isAsciiDocReportDirectory(parameterContext)
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return when {
            isTestState(parameterContext) ->
                extensionContext.testState()
            isTestObserver(parameterContext) ->
                extensionContext.testEvents()
            isAsciiDocReportDirectory(parameterContext) ->
                extensionContext.asciiDocReporter()?.folder ?: throw NoAsciiDocReporterRegistered()
            else ->
                throw TriedToResolveUnsupportedParamter(parameterContext.parameter.type)
        }
    }

    private fun isTestState(parameterContext: ParameterContext) =
        TestState::class.java in parameterContext.parameter.type.interfaces

    private fun isTestObserver(parameterContext: ParameterContext) =
        parameterContext.parameter.type === StructuredTestEvents::class.java

    private fun isAsciiDocReportDirectory(parameterContext: ParameterContext) =
        parameterContext.parameter.isAnnotationPresent(AsciiDocReportDirectory::class.java)

    override fun beforeEach(context: ExtensionContext) {
        val smallScopeInitializer = smallScopeInitializer(context)?.createInstance()
            ?: throw DidNotProvideSmallScopeInitializer()
        context.initTestMarker()
        context.initTestEvents(observer(context))
        context.initTestDsl(smallScopeInitializer, context.testMarker())
    }

    override fun afterTestExecution(context: ExtensionContext) {
        val valueForLabel = mutableMapOf<String, String>()

        val element = context.element.get()
        element.getDeclaredAnnotation(Epic::class.java)?.value?.let { valueForLabel[EPIC_LABEL_NAME] = it }
        element.getDeclaredAnnotation(Feature::class.java)?.value?.let { valueForLabel[FEATURE_LABEL_NAME] = it }
        element.getDeclaredAnnotation(Story::class.java)?.value?.let { valueForLabel[STORY_LABEL_NAME] = it }

        val steps = context.testMarker().steps()

        context.testEvents().onTestReport(
            StructuredTestReport(
                id = context.uniqueId,
                hierarchy = testHierarchy(context),
                title = context.displayName,
                valueForLabel = valueForLabel,
                steps = steps
            )
        )
    }

    private fun testHierarchy(context: ExtensionContext): List<String> {
        var currentContext: ExtensionContext = context
        val hierarchy = mutableListOf<String>()
        while (currentContext.parent.isPresent){
            hierarchy.add(0, currentContext.displayName)
            currentContext = currentContext.parent.orElseThrow()
        }

        return hierarchy
    }

    private fun smallScopeInitializer(extensionContext: ExtensionContext): KClass<out SmallScopeTestInitializer>? {
        val scopeAnnotation = firstAnnotation(extensionContext, SmallScopeTest::class.java)
            ?: throw SmallScopeAnnotationMissing()
        return if (SmallScopeTestInitializer::class in scopeAnnotation.value.allSuperclasses)
            scopeAnnotation.value
        else
            null
    }

    private fun observer(context: ExtensionContext): Map<String, Consumer<StructuredTestReport>> {
        val observer = mutableMapOf<String, Consumer<StructuredTestReport>>()
        asciidocReporter(context)?.let { observer[it.first] = it.second }
        return observer
    }

    private fun asciidocReporter(context: ExtensionContext): Pair<String, AsciiDocReporter>? {
        if(firstAnnotation(context, ReportAsAsciiDoc::class.java) == null)
            return null

        val propertiesFile = javaClass.getResourceAsStream("/customtestreport.properties")?.use {
            val properties = Properties()
            properties.load(it)
            properties
        }

        val rawReportFolder = propertiesFile?.getProperty("customtestreport.results.directory")
            ?: "build/custom-results"
        val reportFolder = Path(rawReportFolder)

        return AsciiDocReporter.reporterName to AsciiDocReporter(reportFolder)
    }

}

private fun ExtensionContext.initTestDsl(scopeTestInitializer: SmallScopeTestInitializer, testMarker: StructuredTestMarker) {
    testStoreFor(this).put(TEST_STATE_KEY, scopeTestInitializer.testState(testMarker))
}

private fun ExtensionContext.testState() = testStoreFor(this).get(TEST_STATE_KEY, TestState::class.java)

private fun ExtensionContext.initTestEvents(observer: Map<String, Consumer<StructuredTestReport>>) {
    val structuredTestEvents = InMemoryStructuredTestEvents()
    observer.forEach(structuredTestEvents::registerAfterEachTest)
    testStoreFor(this).put(TEST_EVENTS_KEY, structuredTestEvents)
}

private fun ExtensionContext.testEvents() = testStoreFor(this).get(TEST_EVENTS_KEY, InMemoryStructuredTestEvents::class.java)

private fun ExtensionContext.asciiDocReporter() = testEvents().byName(AsciiDocReporter.reporterName) as? AsciiDocReporter

private fun ExtensionContext.initTestMarker() {
    testStoreFor(this).put(TEST_MARKER_KEY, InMemoryStructuredTestMarker())
}

private fun ExtensionContext.testMarker() = testStoreFor(this).get(TEST_MARKER_KEY, InMemoryStructuredTestMarker::class.java)

private val NAMESPACE = Namespace.create("de", "richargh", "sandbox", "customtestreport")

private fun globalStoreFor(context: ExtensionContext): Store {
    return context.getStore(NAMESPACE)
}

private fun testStoreFor(context: ExtensionContext): Store {
    return context.getStore(Namespace.create(SmallScopeTestExtension::class, context.requiredTestMethod))
}

private const val TEST_EVENTS_KEY = "Test Events"
private const val TEST_REPORT_ASCIIDOC_FOLDER_KEY = "Test Report AsciiDoc Folder"
private const val TEST_MARKER_KEY = "Test Marker"
private const val TEST_STATE_KEY = "Test Dsl"

class SmallScopeAnnotationMissing
    : RuntimeException("Either Test Method or Class must be annotated with ${SmallScopeTest::class.simpleName}")

class DidNotProvideSmallScopeInitializer
    : RuntimeException("Please provide an initializer that implements ${SmallScopeTestInitializer::class.simpleName}")

class TriedToResolveUnsupportedParamter(unsupportedParamter: Class<*>) :
    RuntimeException("JUnit did something weird and tried to resolve unknown parameter: ${unsupportedParamter.simpleName}")

class NoAsciiDocReporterRegistered() :
    RuntimeException("No AsciiDoc Reporter is registed so you cannot get the reporter path: Register one via annotation: ${ReportAsAsciiDoc::class.simpleName}")
