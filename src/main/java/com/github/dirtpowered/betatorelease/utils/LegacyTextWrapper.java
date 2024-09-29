package com.github.dirtpowered.betatorelease.utils;

public class LegacyTextWrapper {
    private static final int CHAT_WINDOW_WIDTH = 320;
    private static final int CHAT_STRING_LENGTH = 119;
    private static final char COLOR_CHAR = '§';

    private static final int[] CHARACTER_WIDTHS = new int[]{
            1, 9, 9, 8, 8, 8, 8, 7, 9, 8, 9, 9, 8, 9, 9, 9,
            8, 8, 8, 8, 9, 9, 8, 9, 8, 8, 8, 8, 8, 9, 9, 9,
            4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6,
            7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6,
            3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6,
            6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6,
            6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6,
            8, 9, 9, 6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9, 9,
            8, 7, 7, 8, 7, 8, 8, 8, 7, 8, 8, 7, 9, 9, 6, 7,
            7, 7, 7, 7, 9, 6, 7, 8, 7, 6, 6, 9, 7, 6, 7, 1
    };

    public static String[] wrapText(String text) {
        StringBuilder out = new StringBuilder();
        char defaultColor = 'f';
        int lineWidth = 0, lineLength = 0;

        for (int i = 0; i < text.length(); i++) {
            char charAt = text.charAt(i);

            if (charAt == COLOR_CHAR && i < text.length() - 1) {
                if (lineLength + 2 > CHAT_STRING_LENGTH) {
                    out.append('\n').append(COLOR_CHAR).append(defaultColor);
                    lineLength = 2;
                }
                defaultColor = text.charAt(++i);
                out.append(COLOR_CHAR).append(defaultColor);
                lineLength += 2;
                continue;
            }
            // check if the character doesn't exceed the array length, otherwise skip it
            if (charAt < CHARACTER_WIDTHS.length) {
                int width = CHARACTER_WIDTHS[charAt];
                if (lineLength + 1 > CHAT_STRING_LENGTH || lineWidth + width >= CHAT_WINDOW_WIDTH) {
                    out.append('\n').append(defaultColor != 'f' ? COLOR_CHAR + "" + defaultColor : "");
                    lineLength = defaultColor != 'f' ? 2 : 0;
                    lineWidth = 0;
                } else {
                    lineWidth += width;
                }
                out.append(charAt);
                lineLength++;
            }
        }
        return out.toString().split("\n");
    }
}