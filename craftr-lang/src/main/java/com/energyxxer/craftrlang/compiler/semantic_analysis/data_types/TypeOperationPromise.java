package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

public interface TypeOperationPromise {
    DataType check(Operator op, DataType other);
}
