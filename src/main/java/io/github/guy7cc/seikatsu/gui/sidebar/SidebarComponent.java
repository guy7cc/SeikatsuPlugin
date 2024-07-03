package io.github.guy7cc.seikatsu.gui.sidebar;

import java.util.function.Consumer;
import io.github.guy7cc.seikatsu.system.Tickable;

public abstract class SidebarComponent implements Tickable {
    public final String tag;
    private String text = "";
    protected Consumer<String> refreshTextCallback;

    public SidebarComponent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!this.text.equals(text) && refreshTextCallback != null) refreshTextCallback.accept(text);
        this.text = text;
    }

    protected void registerSidebar(Sidebar sidebar, int index) {
        refreshTextCallback = t -> sidebar.refreshText(index, t);
    }
}
