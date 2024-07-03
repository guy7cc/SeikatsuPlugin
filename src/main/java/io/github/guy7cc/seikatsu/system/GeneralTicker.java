package io.github.guy7cc.seikatsu.system;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class GeneralTicker extends BukkitRunnable {
    private int globalTick = 0;

    private final List<Tickable> tickables;

    public final ProfiledTimer timer;

    public GeneralTicker(ProfiledTimer timer, Tickable... tickables) {
        this.timer = timer;
        this.tickables = Arrays.stream(tickables).toList();
    }

    @Override
    public void run() {
        timer.push("Seikatsu");
        ++globalTick;
        for (Tickable tickable : tickables) {
            timer.push(tickable.getClass().getSimpleName());
            tickable.tick(globalTick);
            timer.pop();
        }
        timer.pop();
    }
}
