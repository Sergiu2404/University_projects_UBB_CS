package utils;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    private static String[] colors;

    public static void setNoColors(int noColors) {

        colors = new String[noColors];
    }

    public static void setColorName(int colorId, String color) {
        colors[colorId-1] = color;
    }

    public static Map<Integer, String> getNodesToColors(int[] codes) {

        Map<Integer, String> map = new HashMap<>();

        for (int index = 0; index < codes.length; index++) {

            String color = colors[codes[index]-1];
            map.put(index, color);
        }

        return map;
    }

    public static int getNoColors() {
        return colors.length;
    }
}
