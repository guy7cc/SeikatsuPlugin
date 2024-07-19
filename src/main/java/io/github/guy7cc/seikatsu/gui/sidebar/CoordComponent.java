package io.github.guy7cc.seikatsu.gui.sidebar;

import io.github.guy7cc.seikatsu.system.CoordManager;

public class CoordComponent extends SimpleSidebarComponent{
    public final CoordManager.Coord coord;

    public CoordComponent(CoordManager.Coord coord) {
        super(coord.label + ": (" + coord.x + "," + coord.y + "," + coord.z + ")",  "coord");
        this.coord = coord;
    }
}
