package de.richargh.sandbox.customtestreport.commons.testreport

import java.lang.annotation.Inherited


@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@LabelAnnotation(name = EPIC_LABEL_NAME)
annotation class Epic(val value: String)

data class EpicDetails(val id: String, val description: String)

@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@LabelAnnotation(name = FEATURE_LABEL_NAME)
annotation class Feature(val value: String)

@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@LabelAnnotation(name = STORY_LABEL_NAME)
annotation class Story(val value: String)


@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class LabelAnnotation(
    val name: String,
    val value: String = DEFAULT_VALUE
) {
    companion object {
        const val DEFAULT_VALUE = "%%%__value_%%%"
    }
}

const val EPIC_LABEL_NAME = "epic"
const val FEATURE_LABEL_NAME = "feature"
const val STORY_LABEL_NAME = "story"

