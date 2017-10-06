package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodSignature;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class MethodLog {

    private final Unit parentUnit;
    private ObjectInstance parentInstance;
    private MethodLog parentLog;

    private List<Method> methods;
    //private List<Method> staticMethods;
    //private List<Method> instanceMethods;

    public MethodLog(Unit parentUnit) {
        this.parentUnit = parentUnit;
        this.parentInstance = null;
        this.parentLog = null;

        this.methods = new ArrayList<>();
        //this.staticMethods = new ArrayList<>();
        //this.instanceMethods = new ArrayList<>();
    }

    public MethodLog(ObjectInstance instance) {
        this.parentUnit = instance.getUnit();
        this.parentInstance = instance;
        this.parentLog = parentUnit.getStaticMethodLog();

        this.methods = new ArrayList<>();

        for(Method method : parentUnit.getInstanceMethodLog().methods) {
            this.methods.add(method.duplicate());
        }
    }

    public Method findMethod(MethodSignature signature) {
        for(Method method : methods) {
            if(method.getSignature().equals(signature)) return method;
        }
        /*for(Method method : instanceMethods) {
            if(method.getSignature().equals(signature)) return method;
        }
        for(Method method : staticMethods) {
            if(method.getSignature().equals(signature)) return method;
        }*/
        return null;
    }

    public Method findMethod(MethodSignature signature, TokenPattern<?> pattern, Context context, ObjectInstance instance) {
        Method method = this.findMethod(signature);
        if(method == null) {
            if(parentLog != null) return parentLog.findMethod(signature, pattern, context, instance);
            else {
                parentUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method '" + signature + "'", pattern.getFormattedPath()));
                return null;
            }
        }
        if(!method.isStatic() && instance == null) { //DO SOMETHING ABOUT THE INSTANCE PLEASE
            parentUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Non-static method '" + method.getSignature() + "' cannot be accessed from a static context", pattern.getFormattedPath()));
        }

        switch(method.getVisibility()) {
            case GLOBAL: return method;
            case UNIT: {
                if(parentUnit == context.getUnit()) return method; else break;
            }
            case UNIT_INHERITED: {
                if(instance == null && context.getUnit().instanceOf(parentUnit)) return method; else break;
            }
            case PACKAGE: {
                if(parentUnit.getPackage().equals(context.getDeclaringFile().getPackage())) return method; else break;
            }
        }
        //If you got here it means you have no access. Sorry m8
        parentUnit.getAnalyzer().getCompiler().getReport().addNotice(
                new Notice(
                        NoticeType.ERROR,
                        "Cannot access method '" + signature.getFullyQualifiedName()
                                + "' from current context.",
                        pattern.getFormattedPath()
                )
        );
        //I can't believe after all this error checking I'm returning the method anyways...
        return method;
    }

    public MethodLog createForInstance(ObjectInstance instance) {
        return new MethodLog(instance);
    }

    /*public void insertMethod(TokenStructure component) {
        Method method = new Method(parentUnit, component);
        if(this.findMethod(method.getSignature()) == null) {
            (method.isStatic() ? staticMethods : instanceMethods).add(method);
        } else {
            parentUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(
                    NoticeType.ERROR,
                    "'" + method.getSignature() + "' is already defined in '" + parentUnit.getFullyQualifiedName() + "'",
                    component.getFormattedPath()
            ));
        }
    }*/

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public List<Method> getAllMethods() {
        return new ArrayList<>(methods);
    }

    public void initCodeBlocks() {
        methods.forEach(Method::initCodeBlock);
    }

    public Unit getDeclaringUnit() {
        return parentUnit;
    }
}
