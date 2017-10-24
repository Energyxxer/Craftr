package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

public class ActualParameter extends AbstractFileComponent {
    private String name = null;
    private Value value;

    public ActualParameter(TokenPattern<?> pattern, String name, Value value) {
        super(pattern);
        this.name = name;
        this.setValue(value);
    }

    public ActualParameter(TokenPattern<?> pattern, Value value) {
        super(pattern);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        if(value instanceof Variable) this.value = ((Variable) value).getValue();
        else this.value = value;
    }

    public FormalParameter toFormal() {
        return new FormalParameter(getDataType(), name);
    }

    public DataType getDataType() {
        return (value != null) ? value.getDataType() : DataType.OBJECT;
    }
}
