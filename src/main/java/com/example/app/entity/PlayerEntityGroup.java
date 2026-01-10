package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentEntityDead;
import com.example.app.event.IEventComponent;

public class PlayerEntityGroup extends EntityGroup{

    // CLASS VARIABLES
    private final Player player;

    public PlayerEntityGroup(GameCanvas gc, Player player) {
        super(gc);

        this.player = player;
    }

    @Override
    public void update() {
        makeEntitiesMove(gc, player.getWorldPosition());
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity killed, LivingEntity killer)){
            if (killed == player){
                killGroup();
            }
        }
    }
}
