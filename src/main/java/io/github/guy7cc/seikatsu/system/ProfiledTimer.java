package io.github.guy7cc.seikatsu.system;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ProfiledTimer {
    private String currentProfile = "";
    private final Stack<String> profileStack = new Stack<>();
    private final Stack<Long> timeStack = new Stack<>();
    private final Set<String> profileSet = new HashSet<>();
    private final Map<String, Tracker> trackerMap = new HashMap<>();

    public ProfiledTimer() {

    }

    public void push(@NotNull String profile) {
        if (currentProfile.length() == 0) currentProfile = profile;
        else currentProfile += "." + profile;
        profileStack.push(profile);
        timeStack.push(System.nanoTime());
    }

    public void pop() {
        if (profileStack.isEmpty()) {
            SeikatsuPlugin.log.warn("ProfiledTimer popped too many!");
            return;
        }
        long elapsedNano = System.nanoTime() - timeStack.pop();
        profileSet.add(currentProfile);

        if (trackerMap.containsKey(currentProfile)) {
            trackerMap.get(currentProfile).push(elapsedNano);
        }

        String top = profileStack.pop();
        if (profileStack.isEmpty()) currentProfile = "";
        else currentProfile = currentProfile.substring(0, currentProfile.length() - top.length() - 1);
    }

    public void popPush(String profile) {
        pop();
        push(profile);
    }

    public Set<String> getProfileSet() {
        return profileSet;
    }

    public void startTracker(@NotNull String profile) {
        trackerMap.put(profile, new Tracker(20));
    }

    public void stopTracker(@NotNull String profile) {
        trackerMap.remove(profile);
    }

    public void clearTracker() {
        trackerMap.clear();
    }

    public Tracker getTracker(@NotNull String profile) {
        return trackerMap.get(profile);
    }

    public static class Tracker {
        private int size;
        private Queue<Long> queue;

        public Tracker(int size) {
            this.size = size;
            queue = new ArrayDeque<>(size);
        }

        protected void push(long time) {
            queue.add(time);
            while (queue.size() > size) queue.poll();
        }

        public long getMax() {
            return queue.stream().max(Long::compareTo).orElse(0L);
        }

        public long getMin() {
            return queue.stream().min(Long::compareTo).orElse(0L);
        }

        public double getAvr() {
            if (queue.isEmpty()) return 0L;
            double avr = 0;
            for (long time : queue) {
                avr += time / (double) queue.size();
            }
            return avr;
        }
    }
}
