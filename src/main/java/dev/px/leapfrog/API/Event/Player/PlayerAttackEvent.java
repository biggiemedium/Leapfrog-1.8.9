package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.entity.Entity;

public class PlayerAttackEvent extends Event {

    private Entity entity;

    public PlayerAttackEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
