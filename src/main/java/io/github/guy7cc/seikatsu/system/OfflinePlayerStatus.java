package io.github.guy7cc.seikatsu.system;

import com.google.gson.JsonObject;
import io.github.guy7cc.seikatsu.io.ExtendedJsonObjectWrapper;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class OfflinePlayerStatus extends ExtendedJsonObjectWrapper {
    public OfflinePlayerStatus(Player player){
        super();
        addProperty(player.getName(), "name");
    }

    private OfflinePlayerStatus(JsonObject object){
        super(object);
    }

    @Nullable
    public static OfflinePlayerStatus fromJsonObject(JsonObject object){
        OfflinePlayerStatus status = new OfflinePlayerStatus(object);
        if(!status.getPlayerName().isEmpty()) return status;
        return null;
    }

    public String getPlayerName(){
        return getString("name");
    }

    public boolean getDisplayInfo(){
        if(has("displayInfo")) return getBoolean("displayInfo");
        setDisplayInfo(true);
        return true;
    }

    public void setDisplayInfo(boolean value){
        addProperty(value, "displayInfo");
    }

    public int getLevel(){
        if(has("level")) return getInt("level");
        setLevel(1);
        return 1;
    }

    public void setLevel(int value){
        addProperty(value, "level");
    }

    public static int getMaxXp(int level){
        if(level < 1) return Const.MAX_XP[0];
        else if(level <= Const.MAX_XP.length) return Const.MAX_XP[level - 1];
        else return Const.MAX_XP[Const.MAX_XP.length - 1];
    }

    public int getXp(){
        if(has("xp")) return getInt("xp");
        setXp(0);
        return 0;
    }

    private void setXp(int value){
        addProperty(value, "xp");
    }

    public void addXp(int value){
        int xp = getXp();
        int level = getLevel();
        if(xp + value >= getMaxXp(level)){
            setXp(xp + value - getMaxXp(level));
            setLevel(level + 1);
            Player p = Bukkit.getPlayer(getPlayerName());
            if(p != null) p.setPlayerListName(Const.getRank(level + 1) + p.getName());
            for(Player player : Bukkit.getOnlinePlayers()){
                player.spigot().sendMessage(new TextComponent("§b" + getPlayerName() + "§fが生活Lv." + (level + 1) + "に上がりました！"));
                if(!Const.getRank(level).equals(Const.getRank(level + 1))){
                    player.spigot().sendMessage(new TextComponent("§b" + getPlayerName() + "§fが" + Const.getRank(level + 1) + "§fに昇格しました！"));
                }
            }
        } else {
            setXp(xp + value);
        }
    }
}
