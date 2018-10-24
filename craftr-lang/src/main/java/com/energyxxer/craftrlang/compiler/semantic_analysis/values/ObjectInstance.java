package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.commands.summon.SummonCommand;
import com.energyxxer.commodore.coordinates.Coordinate;
import com.energyxxer.commodore.coordinates.CoordinateSet;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.TagCompound;
import com.energyxxer.commodore.nbt.TagList;
import com.energyxxer.commodore.nbt.TagString;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.selector.TagArgument;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
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
        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this, false); //TODO: debate whether this should be initialized or not
        this.methodLog = unit.getInstanceMethodLog().createForInstance(this);
        this.entity = other.entity;

        this.fieldLog.put("this", this);
    }

    public ObjectInstance(Unit unit, SemanticContext semanticContext, boolean initialized) {
        this(unit, null, semanticContext, initialized);
    }

    public ObjectInstance(Unit unit, CraftrEntity entity, SemanticContext semanticContext, boolean initialized) {
        super(semanticContext);
        this.unit = unit;

        if(entity == null) {
            //TODO: Actual entity constructor...
            //setEntity(new CraftrEntity(unit, new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument(semanticContext.getCompiler().getPrefix() + "_type:" + unit.getName()))));
        } else {
            setEntity(entity);
        }

        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this, initialized);
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
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
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

    public CraftrEntity requestEntity(FunctionSection function) {
        if(entity == null) {
            if(function != null) {
                function.append(new SummonCommand(semanticContext.getModule().minecraft.getTypeManager().entity.get("area_effect_cloud"), new CoordinateSet(0,0,0, Coordinate.Type.RELATIVE), new TagCompound(new TagList("Tag",new TagString(semanticContext.getCompiler().getPrefix() + "_type:" + unit.getName())))));
            }
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
