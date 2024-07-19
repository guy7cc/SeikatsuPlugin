package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ServerEventHandler implements Listener {
    @EventHandler
    public void onServerCommand(ServerCommandEvent event){
        if(SeikatsuPlugin.forbiddenCommands.matcher(event.getCommand()).find()){
            event.setCancelled(true);
            event.getSender().sendMessage("§cそのコマンドは無効化されています。");
        }
    }
}
