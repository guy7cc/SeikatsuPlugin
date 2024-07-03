package io.github.guy7cc.seikatsu.system;

public class Const {
    public static int[] MAX_XP = new int[]{
            120, 192, 306, 489, 781,
            1248, 1717, 2361, 3249, 4469,
            6148, 7830, 9972, 12700, 16174,
            20599, 25027, 30407, 36944, 44887,
            54537, 64191, 75555, 86922, 99999
    };

    public static String getRank(int level){
        String[] rank = new String[]{ "§f【赤ちゃん】", "§f【子供】", "§a【一人暮らし】", "§a【会社員】", "§b【家庭持ち】", "§b【一軒家住まい】", "§d【家政婦】", "§c【家】", "§4【ママ】"};
        if(1 <= level && level < 25) return rank[(level - 1) / 3];
        else if(level >= 25) return rank[8];
        else return rank[0];
    }
}
