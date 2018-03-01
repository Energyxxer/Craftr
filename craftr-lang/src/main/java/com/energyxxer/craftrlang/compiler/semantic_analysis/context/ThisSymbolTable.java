package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

public class ThisSymbolTable extends SymbolTable {
    public ThisSymbolTable(Symbol _this) {
        super();
        this.put("this", _this);
    }
}
