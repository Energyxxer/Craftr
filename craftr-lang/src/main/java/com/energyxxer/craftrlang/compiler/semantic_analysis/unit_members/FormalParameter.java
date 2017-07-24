package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;

/**
 * Created by User on 5/16/2017.
 */
public class FormalParameter {
    private DataType type;
    private String name;

    public FormalParameter(DataType type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormalParameter that = (FormalParameter) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public DataType getType() {
        return type;
    }
}
