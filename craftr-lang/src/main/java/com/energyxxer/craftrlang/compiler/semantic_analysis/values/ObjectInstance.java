package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.selector.TagArgument;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.EntityReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class ObjectInstance extends Value implements Symbol, DataHolder {

    private Unit unit;

    private FieldLog fieldLog;
    private MethodLog methodLog;

    private CraftrEntity entity;

    public ObjectInstance(ObjectInstance other) {
        super(other.reference, other.semanticContext);
        this.unit = other.unit;
        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this);
        this.methodLog = unit.getInstanceMethodLog().createForInstance(this);
        this.entity = other.entity;

        this.fieldLog.put("this", this);
    }

    public ObjectInstance(Unit unit, SemanticContext semanticContext) {
        this(unit, true, semanticContext);
    }

    public ObjectInstance(Unit unit, boolean explicit, SemanticContext semanticContext) {
        super(semanticContext);
        this.unit = unit;

        if(!explicit) {
            //TODO: Actual entity constructor...
            setEntity(new CraftrEntity(unit, new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument(semanticContext.getCompiler().getPrefix() + "_type:" + unit.getName()))));
        }

        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this);
        this.methodLog = unit.getInstanceMethodLog().createForInstance(this);

        this.fieldLog.put("this", this);
    }

    public @NotNull Unit getUnit() {
        return unit;
    }

    @Override
    public DataType getDataType() {
        return unit.getDataType();
    }

    @Override
    public @NotNull SymbolTable getSubSymbolTable() {
        return fieldLog;
    }

    @Override
    public MethodLog getMethodLog() {
        return methodLog;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        return null;
    }

    @Override
    public String getName() {
        return "<instance of " + unit.getFullyQualifiedName() + ">";
    }

    @Override
    public SymbolVisibility getVisibility() {
        return SymbolVisibility.UNIT;
    }

    @Override
    public ObjectInstance clone(Function function) {
        return new ObjectInstance(this);
    }

    @Override
    public ObjectInstance asObjectInstance() {
        return this;
    }

    public CraftrEntity getEntity() {
        return entity;
    }

    private void setEntity(CraftrEntity entity) {
        this.entity = entity;
        this.reference = new EntityReference(entity);
    }

    public CraftrEntity requestEntity() {
        if(entity == null) {
            setEntity(new CraftrEntity(unit, new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument(semanticContext.getCompiler().getPrefix() + "_type:" + unit.getName()))));
        }
        return entity;
    }

    @Override
    public boolean isExplicit() {
        return entity == null;
    }

    @Override
    public boolean isImplicit() {
        return entity != null;
    }

    @Override
    public String toString() {
        return "ObjectInstance:" + unit.getName() + "@" + hashCode();
    }
}
