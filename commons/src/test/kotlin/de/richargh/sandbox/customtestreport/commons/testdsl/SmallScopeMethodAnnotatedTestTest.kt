package de.richargh.sandbox.customtestreport.commons.testdsl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SmallScopeMethodAnnotatedTestTest {

    @Test @SmallScopeTest(SmallScopeTestInitializerExample::class)
    fun `can resolve teststate when method is annotated`(a: TestStateExample) {
        assertThat(a).isInstanceOf(TestStateExample::class.java)
        assertThat(a.name()).isEqualTo("example")
    }

    @ParameterizedTest @ValueSource(ints = [1, 42]) @SmallScopeTest(SmallScopeTestInitializerExample::class)
    fun `can resolve teststate and parameter when method is annotated`(number: Number, a: TestStateExample) {
        assertThat(a).isInstanceOf(TestStateExample::class.java)
        assertThat(a.name()).isEqualTo("example")
        assertThat(number).isIn(1, 42)
    }

    @Nested
    inner class Nest {
        @Test @SmallScopeTest(SmallScopeTestInitializerExample::class)
        fun `can resolve teststate when nested method is annotated`(a: TestStateExample) {
            assertThat(a).isInstanceOf(TestStateExample::class.java)
            assertThat(a.name()).isEqualTo("example")
        }

        @ParameterizedTest @ValueSource(ints = [1, 42]) @SmallScopeTest(SmallScopeTestInitializerExample::class)
        fun `can resolve teststate and parameter when nested method is annotated`(number: Number, a: TestStateExample) {
            assertThat(a).isInstanceOf(TestStateExample::class.java)
            assertThat(a.name()).isEqualTo("example")
            assertThat(number).isIn(1, 42)
        }
    }

}
