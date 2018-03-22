package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

/**
 * Created by User on 5/16/2017.
 */
public class FormalParameter {
    private DataType type;
    private String name;

    private LocalizedObjective assignedObjective;

    public FormalParameter(DataType type, String name) {
        this.type = type;
        this.name = name;
    }

    public FormalParameter(TokenPattern<?> rawParam, SemanticContext semanticContext) {
        this.type = DataType.parseType((rawParam.find("DATA_TYPE")).flattenTokens(), semanticContext.getUnit().getDeclaringFile().getReferenceTable(), semanticContext.getUnit());
        this.name = ((TokenItem) rawParam.find("PARAMETER_NAME")).getContents().value;

        if(type == DataType.VOID) {
            semanticContext.getUnit().getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal type 'void'", rawParam.find("DATA_TYPE")));
        }
    }

    public String getName() {
        return name;
    }

    void assignObjective(LocalizedObjective objective) {
        this.assignedObjective = objective;
    }

    LocalizedObjective getAssignedObjective() {
        if(assignedObjective == null) throw new IllegalStateException("Objective unassigned");
        return assignedObjective;
    }

    ScoreReference getScore(Method method) {
        return new ScoreReference(new LocalScore(this.getAssignedObjective().getObjective(), method.getScoreHolder()));
    }

    public boolean matches(FormalParameter formalParam) {
        return type.instanceOf(formalParam.type);
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
