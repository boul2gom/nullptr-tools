package io.github.nullptr.tools.annotations.processor;

import io.github.nullptr.tools.types.Pair;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationProcessorHelper {

    public static <T extends Annotation> Map<Element, T> getAnnotations(final Element element, Class<T> annotationClass) {
        return element.getEnclosedElements().stream()
                .filter(e -> e.getAnnotation(annotationClass) != null)
                .collect(Collectors.toMap(e -> e, e -> e.getAnnotation(annotationClass)));
    }

    public static Pair<String, String> getType(Element element) {
        final String type = element.asType().toString();
        final String[] split = type.split("\\.");

        return new Pair<>(type, split[split.length - 1]);
    }
}
