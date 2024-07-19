package io.github.guy7cc.seikatsu.system;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.entity.Player;

public class OnlinePlayerStatus implements Tickable {
    private final Player player;
    private int elapsedTick = 0;
    private int sprintTick = 0;
    private int brokenBlock = 0;
    private int placedBlock = 0;
    private int consumedItem = 0;
    private double damageDone = 0D;
    private int expReceived = 0;

    public OnlinePlayerStatus(Player player){
        this.player = player;
    }

    @Override
    public void tick(int globalTick) {
        if(player.isSprinting()) ++sprintTick;
        else ++elapsedTick;
        if(elapsedTick >= 6000) {
            elapsedTick -= 6000;
            addXp(1);
        }
        if(sprintTick >= 200) {
            sprintTick -= 200;
            addXp(1);
        }
    }

    public void onBlockBreak(){
        ++brokenBlock;
        if(brokenBlock >= 3) {
            brokenBlock -= 3;
            addXp(1);
        }
    }

    public void onBlockPlace(){
        ++placedBlock;
        if(placedBlock >= 5) {
            placedBlock -= 5;
            addXp(1);
        }
    }

    public void onDamageEntity(double value){
        damageDone += value;
        if(damageDone > 10D){
            damageDone -= 10D;
            addXp(1);
        }
    }

    public void onItemConsume(){
        ++consumedItem;
        if(consumedItem >= 3){
            consumedItem -= 3;
            addXp(1);
        }
    }

    public void onExpChange(int value){
        expReceived += value;
        if(expReceived >= 30){
            expReceived -= 30;
            addXp(1);
        }
    }

    private void addXp(int value){
        OfflinePlayerStatus offlineStatus = SeikatsuPlugin.offlinePlayerStatus.get(player);
        if(offlineStatus == null) return;
        offlineStatus.addXp(value);
    }
}
