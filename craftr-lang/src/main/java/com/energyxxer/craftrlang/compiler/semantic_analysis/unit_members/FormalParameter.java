package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;

/**
 * Created by User on 5/16/2017.
 */
public class FormalParameter {
    private final Method parent;
    private DataType type;
    private String name;

    public FormalParameter(DataType type, String name) {
        this.parent = null;
        this.type = type;
        this.name = name;
    }

    public FormalParameter(Method parent, TokenPattern<?> rawParam) {
        this.parent = parent;
        this.type = DataType.parseType((rawParam.find("DATA_TYPE")).flattenTokens(), parent.getUnit().getDeclaringFile().getReferenceTable(), parent.getUnit());
        this.name = ((TokenItem) rawParam.find("PARAMETER_NAME")).getContents().value;

        if(type == DataType.VOID) {
            parent.getUnit().getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal type 'void'", rawParam.find("DATA_TYPE").getFormattedPath()));
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormalParameter that = (FormalParameter) o;

        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public DataType getType() {
        return type;
    }
}
