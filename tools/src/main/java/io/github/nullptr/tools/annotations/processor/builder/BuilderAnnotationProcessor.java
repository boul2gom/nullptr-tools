package io.github.nullptr.tools.annotations.processor.builder;

import com.google.auto.service.AutoService;
import io.github.nullptr.tools.annotations.BuilderArgumentGenerator;
import io.github.nullptr.tools.annotations.BuilderGenerator;
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

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("io.github.nullptr.tools.annotations.BuilderGenerator");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final AtomicBoolean isProcessing = new AtomicBoolean(false);
        final Messager messager = processingEnv.getMessager();

        messager.printMessage(Diagnostic.Kind.NOTE, "Processing " + annotations.size() + " annotations");
        for (final TypeElement annotation : annotations) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Processing annotation " + annotation.getQualifiedName());

            for (final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Processing element " + element.getSimpleName());
                final BuilderGenerator builderGenerator = element.getAnnotation(BuilderGenerator.class);

                if (AnnotationProcessorHelper.getAnnotations(element, BuilderArgumentGenerator.class).isEmpty()) {
                    continue;
                }

                isProcessing.set(true);
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating builder for " + element.getSimpleName());

                final String builderClassName = this.getBuilderClassName(element, builderGenerator);
                messager.printMessage(Diagnostic.Kind.NOTE, "Builder class name: " + builderClassName);
                try {
                    final JavaFileObject file = processingEnv.getFiler().createSourceFile(builderClassName);
                    messager.printMessage(Diagnostic.Kind.NOTE, "Created file " + file.toUri());
                    final BuilderAnnotationWriter writer = new BuilderAnnotationWriter(element, builderClassName, file);
                    messager.printMessage(Diagnostic.Kind.NOTE, "Writing...");

                    writer.write();
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    isProcessing.set(false);
                }
            }
        }

        return isProcessing.get();
    }

    private String getBuilderClassName(final Element element, final BuilderGenerator builderGenerator) {
        final String className = element.getSimpleName().toString();
        final String pattern = builderGenerator.name();

        return pattern.replace("${Class}", className);
    }
}
