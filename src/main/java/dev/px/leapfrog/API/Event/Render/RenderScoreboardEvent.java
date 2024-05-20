package dev.px.leapfrog.API.Event.Render;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;

public class RenderScoreboardEvent extends Event {

    private ScoreObjective objective;
    private ScaledResolution scaledResolution;

    public RenderScoreboardEvent(ScoreObjective objective, ScaledResolution scaledResolution) {
        this.objective = objective;
        this.scaledResolution = scaledResolution;
    }

    public ScoreObjective getObjective() {
        return objective;
    }

    public void setObjective(ScoreObjective objective) {
        this.objective = objective;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
