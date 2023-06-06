package io.github.nullptr.tools.annotations.processor.builder;

import com.google.auto.service.AutoService;
import io.github.nullptr.tools.annotations.Builder;
import io.github.nullptr.tools.annotations.BuilderArgument;
import io.github.nullptr.tools.annotations.processor.AnnotationProcessorHelper;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@AutoService(Processor.class)
public class BuilderAnnotationProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("io.github.nullptr.tools.annotations.Builder");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final AtomicBoolean isProcessing = new AtomicBoolean(false);
        this.messager = this.processingEnv.getMessager();

        for (final Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            this.messager.printMessage(Diagnostic.Kind.NOTE, "Processing element " + element.getSimpleName());
            final Builder builder = element.getAnnotation(Builder.class);

            if (AnnotationProcessorHelper.getAnnotations(element, BuilderArgument.class).isEmpty()) {
                this.messager.printMessage(Diagnostic.Kind.ERROR, "No fields annotated with @BuilderArgument found");
                continue;
            }

            isProcessing.set(true);
            this.messager.printMessage(Diagnostic.Kind.NOTE, "Generating builder for " + element.getSimpleName());

            final String builderClassName = this.getBuilderClassName(element, builder);
            this.messager.printMessage(Diagnostic.Kind.NOTE, "Builder class name: " + builderClassName);
            try {
                final JavaFileObject file = processingEnv.getFiler().createSourceFile(builderClassName);
                this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing...");

                final BuilderAnnotationWriter writer = new BuilderAnnotationWriter(element, builderClassName, file);
                writer.write();
            } catch (IOException e) {
                this.messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                isProcessing.set(false);
            }
        }

        return isProcessing.get();
    }

    private String getBuilderClassName(final Element element, final Builder builder) {
        final String className = element.getSimpleName().toString();
        final String pattern = builder.name();

        return pattern.replace("${Class}", className);
    }
}
