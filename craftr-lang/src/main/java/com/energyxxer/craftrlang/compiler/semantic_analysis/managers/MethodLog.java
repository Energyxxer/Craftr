package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodSignature;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class MethodLog {
    private final Unit parentUnit;
    private final boolean isStatic;
    private ObjectInstance parentInstance;

    private HashMap<MethodSignature, Method> methods;

    public MethodLog(Unit parentUnit) {
        this.parentUnit = parentUnit;
        this.parentInstance = null;
        this.isStatic = true;

        this.methods = new HashMap<>();
    }

    public MethodLog(ObjectInstance instance) {
        this.parentUnit = instance.getUnit();
        this.parentInstance = instance;
        this.isStatic = false;

        this.methods = new HashMap<>();

        for(Unit unit : parentUnit.getInheritanceMap()) {
            unit.getStaticMethodLog().getAllMethods().forEach(this::addMethod);
            unit.getInstanceMethodLog().getAllMethods().forEach(this::addMethod);
        }
        parentUnit.getStaticMethodLog().getAllMethods().forEach(this::addMethod);
        parentUnit.getInstanceMethodLog().getAllMethods().forEach(this::addMethod);
    }

    public Method findMethod(MethodSignature signature) {
        for(Method method : methods.values()) {
            if(signature.matches(method.getSignature())) return method;
        }
        return null;
    }

    public Method findMethod(MethodSignature signature, TokenPattern<?> pattern, SemanticContext semanticContext, ObjectInstance instance) {
        Method method = this.findMethod(signature);
        if(method == null) {
            parentUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method '" + signature + "'", pattern));
            return null;
        }
        if(!method.isStaticAccess() && instance == null) { //TODO SOMETHING ABOUT THE INSTANCE PLEASE
            parentUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Non-static method '" + method.getSignature() + "' cannot be accessed from a static context", pattern));
        }

        switch(method.getVisibility()) {
            case GLOBAL: return method;
            case UNIT: {
                if(method.getUnit() == semanticContext.getUnit()) return method; else break;
            }
            case UNIT_INHERITED: {
                if(semanticContext.getUnit().instanceOf(method.getUnit())) return method; else break;
            }
            case PACKAGE: {
                if(method.getPackage() == semanticContext.getDeclaringFile().getPackage()) return method; else break;
            }
        }
        //If you got here it means you have no access. Sorry m8
        parentUnit.getAnalyzer().getCompiler().getReport().addNotice(
                new Notice(
                        NoticeType.ERROR,
                        "Cannot access method '" + signature.getFullyQualifiedName()
                                + "' from current semanticContext.",
                        pattern
                )
        );
        //I can't believe after all this error checking I'm returning the method anyways...
        return method;
    }

    public MethodLog createForInstance(ObjectInstance instance) {
        return new MethodLog(instance);
    }

    public void addMethod(Method method) {
        this.methods.put(method.getSignature(), method);
    }

    public Collection<Method> getAllMethods() {
        return methods.values();
    }

    public void initCodeBlocks() {
        methods.values().forEach(Method::initCodeBlock);
    }

    public Unit getDeclaringUnit() {
        return parentUnit;
    }
}
