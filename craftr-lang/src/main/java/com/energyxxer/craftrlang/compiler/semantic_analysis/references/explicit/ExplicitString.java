package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.CommandUtils;
import com.energyxxer.commodore.commands.summon.SummonCommand;
import com.energyxxer.commodore.coordinates.Coordinate;
import com.energyxxer.commodore.coordinates.CoordinateSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTCompoundBuilder;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.TagCompound;
import com.energyxxer.commodore.nbt.TagString;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.selector.LimitArgument;
import com.energyxxer.commodore.selector.NameArgument;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.EntityReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

public class ExplicitString implements ExplicitValue {

    private final String value;
    private EntityReference stringEntityReference;

    public ExplicitString(String value) {
        this.value = value;
    }

    @Override
    public ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext) {
        return getStringEntity(section, semanticContext).toScore(section, score, semanticContext);
    }

    private EntityReference getStringEntity(FunctionSection section, SemanticContext semanticContext) {
        if(stringEntityReference == null) {
            section.append(new SummonCommand(semanticContext.getModule().minecraft.getTypeManager().entity.get("area_effect_cloud"), new CoordinateSet(0, 0, 0, Coordinate.Type.RELATIVE), new TagCompound(new TagString("CustomName", "\"" + CommandUtils.escape(value) + "\""))));
            stringEntityReference = new EntityReference(new CraftrEntity((Unit) semanticContext.getAnalyzer().getLangPackage().getSubSymbolTable().getMap().get("String"), new Selector(Selector.BaseSelector.ALL_ENTITIES, new NameArgument(value), new LimitArgument(1))));
        }
        return stringEntityReference;
    }

    @Override
    public NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext) {
        NBTCompoundBuilder cb = new NBTCompoundBuilder();

        cb.put(path, new TagString(value));

        return new NBTReference(entity, path);
    }

    @Override
    public String toString() {
        return "\"" + CommandUtils.escape(value) + "\"";
    }

    public String getValue() {
        return value;
    }

    @Override
    public Number asNumber() {
        return null;
    }
}
