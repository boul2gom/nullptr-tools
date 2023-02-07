package io.github.nullptr.tools.annotations.processor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import java.util.HashSet;
import java.util.Set;

//This class should be able to store in a set all the imports that are needed for the generated class
//Excepting primitive types and java.lang, that are automatically imported
//This should check fields, methods params and returns, classes and interfaces, etc.
public class ImportScanner extends ElementScanner8<Void, Void> {

    private final Set<String> imports = new HashSet<>();

    public Set<String> getImports() {
        return this.imports;
    }

    @Override
    public Void visitType(TypeElement e, Void aVoid) {
        final String name = e.getQualifiedName().toString();
        System.out.println("ImportScanner.visitType: " + name);

        if (this.importRequired(name)) {
            this.imports.add(name);
        }
        return super.visitType(e, aVoid);
    }

    @Override
    public Void visitTypeParameter(TypeParameterElement e, Void aVoid) {
        final String name = e.asType().toString();
        System.out.println("ImportScanner.visitTypeParameter: " + name);

        if (this.importRequired(name)) {
            this.imports.add(name);
        }
        return super.visitTypeParameter(e, aVoid);
    }

    @Override
    public Void visitExecutable(ExecutableElement e, Void aVoid) {
        final String name = e.getReturnType().toString();
        System.out.println("ImportScanner.visitExecutable: " + name);

        if (this.importRequired(name)) {
            this.imports.add(name);
        }
        return super.visitExecutable(e, aVoid);
    }

    @Override
    public Void visitVariable(VariableElement e, Void aVoid) {
        final String name = e.asType().toString();
        System.out.println("ImportScanner.visitVariable: " + name);

        if (this.importRequired(name)) {
            this.imports.add(name);
        }
        return super.visitVariable(e, aVoid);
    }

    private boolean importRequired(String name) {
        switch (name) {
            case "boolean":
            case "byte":
            case "char":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "void":
            case "null":
                return false;
            default:
                return true;
        }
    }
}
