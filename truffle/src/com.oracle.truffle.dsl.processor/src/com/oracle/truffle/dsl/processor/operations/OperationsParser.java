package com.oracle.truffle.dsl.processor.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.java.ElementUtils;
import com.oracle.truffle.dsl.processor.java.model.CodeAnnotationMirror;
import com.oracle.truffle.dsl.processor.java.model.CodeTypeElement;
import com.oracle.truffle.dsl.processor.model.MessageContainer.Message;
import com.oracle.truffle.dsl.processor.parser.AbstractParser;

public class OperationsParser extends AbstractParser<OperationsData> {

    private static AnnotationMirror getAnnotationMirror(List<? extends AnnotationMirror> mirror, DeclaredType type) {
        for (AnnotationMirror m : mirror) {
            if (m.getAnnotationType().equals(type)) {
                return m;
            }
        }
        return null;
    }

    @Override
    protected OperationsData parse(Element element, List<AnnotationMirror> mirror) {

        TypeElement typeElement = (TypeElement) element;
        AnnotationMirror generateOperationsMirror = getAnnotationMirror(mirror, types.GenerateOperations);

        OperationsData data = new OperationsData(ProcessorContext.getInstance(), typeElement, generateOperationsMirror);
        {
            ExecutableElement parseMethod = ElementUtils.findExecutableElement(typeElement, "parse");
            if (parseMethod == null) {
                data.addError(typeElement,
                                "Parse method not found. You must provide a method named 'parse' with following signature: void parse({Language}, {Context}, %sBuilder)",
                                typeElement.getSimpleName());
                return data;
            }

            if (parseMethod.getParameters().size() != 3) {
                data.addError(parseMethod, "Parse method must have exactly three arguments: the language, source and the builder");
                return data;
            }

            TypeMirror languageType = parseMethod.getParameters().get(0).asType();
            TypeMirror contextType = parseMethod.getParameters().get(1).asType();

            data.setParseContext(languageType, contextType, parseMethod);
        }

        List<TypeElement> operationTypes = new ArrayList<>(ElementFilter.typesIn(typeElement.getEnclosedElements()));
        List<AnnotationMirror> opProxies = ElementUtils.getRepeatedAnnotation(typeElement.getAnnotationMirrors(), types.OperationProxy);

        for (AnnotationMirror mir : opProxies) {
            DeclaredType tgtType = (DeclaredType) ElementUtils.getAnnotationValue(mir, "value").getValue();

            SingleOperationData opData = new SingleOperationParser(data, (TypeElement) tgtType.asElement()).parse(null, null);

            if (opData != null) {
                data.addOperationData(opData);
            } else {
                data.addError("Could not generate operation: " + tgtType.asElement().getSimpleName());
            }
        }

        boolean hasSome = false;
        for (TypeElement inner : operationTypes) {
            if (ElementUtils.findAnnotationMirror(inner, types.Operation) == null) {
                continue;
            }

            hasSome = true;

            SingleOperationData opData = new SingleOperationParser(data).parse(inner, false);

            if (opData != null) {
                data.addOperationData(opData);
            } else {
                data.addError("Could not generate operation: " + inner.getSimpleName());
            }

            if (inner instanceof CodeTypeElement) {
                opData.redirectMessagesOnGeneratedElements(data);
            }

        }

        if (!hasSome) {
            data.addWarning("No operations found");
            return data;
        }

        // data.setTracing(true);

        if (data.hasErrors()) {
            return data;
        }

        data.initializeContext();

        return data;
    }

    @Override
    public DeclaredType getAnnotationType() {
        return types.GenerateOperations;
    }

}
