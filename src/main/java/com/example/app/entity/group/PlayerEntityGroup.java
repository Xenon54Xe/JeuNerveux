package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.entity.Player;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.IEventComponent;

public class PlayerEntityGroup extends EntityGroup{

    // CLASS VARIABLES
    private final Player player;

    public PlayerEntityGroup(GameCanvas gc, Player player) {
        super(gc);

        this.player = player;
    }

    @Override
    public void update() {
        makeEntitiesMove(player.getWorldPosition(), true);
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
