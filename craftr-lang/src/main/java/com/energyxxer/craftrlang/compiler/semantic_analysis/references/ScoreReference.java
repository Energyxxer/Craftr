package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.scoreboard.ScorePlayersOperation;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;

public class ScoreReference implements DataReference {
    private LocalScore score;

    public ScoreReference(LocalScore score) {
        this.score = score;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score) {
        if(!score.equals(this.score)) {
            function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ASSIGN, this.score));
            return new ScoreReference(score);
        }
        return this;
    }
}
