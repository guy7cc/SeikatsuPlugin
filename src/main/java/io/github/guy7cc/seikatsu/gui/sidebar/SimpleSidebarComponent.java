package io.github.guy7cc.seikatsu.gui.sidebar;

public class SimpleSidebarComponent extends SidebarComponent {
    public SimpleSidebarComponent(String text, String tag) {
        super(tag);
        setText(text);
    }

    @Override
    public void tick(int globalTick) {

    }
}
