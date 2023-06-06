package io.github.nullptr.tools.annotations.processor;

import io.github.nullptr.tools.types.Pair;

import javax.lang.model.element.Element;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.Reader;
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

    public static String read(FileObject file) throws IOException {
        try (final Reader reader = file.openReader(true)) {
            final StringBuilder builder = new StringBuilder();
            final char[] buffer = new char[1024];
            int read;

            while ((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }

            return builder.toString();
        }
    }
}
