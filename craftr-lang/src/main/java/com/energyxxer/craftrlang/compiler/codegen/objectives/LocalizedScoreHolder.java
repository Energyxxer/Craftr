package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.ScoreHolder;

public class LocalizedScoreHolder {
    private final LocalizedObjective objective;
    private final ScoreHolder scoreHolder;
    private LocalScore localScore;

    public LocalizedScoreHolder(LocalizedObjective objective, ScoreHolder scoreHolder) {
        this.objective = objective;
        this.scoreHolder = scoreHolder;
    }

    public LocalizedObjective getLocalizedObjective() {
        return objective;
    }

    public ScoreHolder getScoreHolder() {
        return scoreHolder;
    }

    public LocalScore getLocalScore() {
        if(localScore == null) localScore = new LocalScore(objective.getObjective(), scoreHolder);
        return localScore;
    }
}
