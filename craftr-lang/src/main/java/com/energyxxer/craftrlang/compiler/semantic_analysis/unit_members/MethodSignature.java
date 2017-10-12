package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;

import java.util.List;

/**
 * Created by Energyxxer on 07/09/2017.
 */
public class MethodSignature {
    public final Unit declaringUnit;
    public final String name;
    public final List<FormalParameter> positionalParams;

    MethodSignature(Unit declaringUnit, String name, List<FormalParameter> positionalParams) {
        this.declaringUnit = declaringUnit;
        this.name = name;
        this.positionalParams = positionalParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodSignature that = (MethodSignature) o;

        return name.equals(that.name) && positionalParams.equals(that.positionalParams);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + positionalParams.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append((declaringUnit.getName().equals(this.name)) ? "new" : name);
        sb.append(name);
        sb.append("(");
        boolean hasParam = false;
        for(FormalParameter p : positionalParams) {
            sb.append(p.getType());
            sb.append(", ");
            hasParam = true;
        }
        if(hasParam) sb.setLength(sb.length()-2);
        sb.append(")");

        return sb.toString();
    }

    public String getFullyQualifiedName() {
        StringBuilder sb = new StringBuilder();
        sb.append(declaringUnit.getFullyQualifiedName());
        sb.append('.');
        sb.append((declaringUnit.getName().equals(this.name)) ? "new" : name);
        sb.append('(');
        boolean hasParam = false;
        for(FormalParameter p : positionalParams) {
            sb.append(p.getType());
            sb.append(", ");
            hasParam = true;
        }
        if(hasParam) sb.setLength(sb.length()-2);
        sb.append(')');

        return sb.toString();
    }
}
