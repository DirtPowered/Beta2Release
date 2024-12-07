package com.github.dirtpowered.betatorelease.utils;

import com.github.dirtpowered.betaprotocollib.data.BetaItemStack;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    private final static int[] DAMAGEABLE_ITEMS = {
            256, 257, 258, 259, 261, 267, 268, 269, 270, 271, 272, 273, 274, 275,
            276, 277, 278, 279, 283, 284, 285, 286, 290, 291, 292, 293, 294, 298,
            299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312,
            313, 314, 315, 316, 317, 346, 359
    };

    private final static int[] ALLOWED_CACHE_BLOCKS = new int[]{
            0, 64, 71, 96, 167
    };

    public static String toHex(int i) {
        return "0x" + String.format("%2s", Integer.toHexString(i)).replace(' ', '0');
    }

    public static BetaItemStack[] convertItemStacks(ItemStack[] itemStacks) {
        return Arrays.stream(itemStacks).map(Utils::itemStackToBetaItemStack).toArray(BetaItemStack[]::new);
    }

    public static ItemStack betaItemStackToItemStack(BetaItemStack itemStack) {
        return new ItemStack(itemStack.getBlockId(), itemStack.getAmount(), itemStack.getData());
    }

    public static BetaItemStack itemStackToBetaItemStack(ItemStack itemStack) {
        if (itemStack == null)
            return new BetaItemStack(0, 0, 0);

        BlockMappings.RemappedItem remap = BlockMappings.getRemappedItem(itemStack.getId(), itemStack.getData());
        return new BetaItemStack(remap.itemId(), itemStack.getAmount(), remap.itemData());
    }

    public static boolean isDamageable(int itemId) {
        return Arrays.stream(DAMAGEABLE_ITEMS).anyMatch(i -> i == itemId);
    }

    public static boolean isAllowedCacheBlock(int blockId) {
        return Arrays.stream(ALLOWED_CACHE_BLOCKS).anyMatch(i -> i == blockId);
    }

    public static String[] getLegacySignLines(CompoundTag tag) {
        Map<String, Tag> strings = tag.getValue();
        String[] signLines = new String[4];

        for (int line = 0; line < 4; ++line) {
            try {
                AtomicReference<String> nTag = new AtomicReference<>((String) strings.get("Text" + (line + 1)).getValue());
                // replace unsupported chars
                nTag.get().chars().forEach(c -> {
                    if (!LegacyTextWrapper.isCharSupported((char) c)) {
                        nTag.set(nTag.get().replace((char) c, LegacyTextWrapper.UNKNOWN_CHAR_PLACEHOLDER));
                    }
                });
                signLines[line] = StringUtils.substring(LangStorage.translate(nTag.get(), false), 0, 15);
            } catch (Exception e) {
                signLines[line] = "error";
            }
        }
        return signLines;
    }

    public static String stripUnsupportedColors(String message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '§') {
                if (i + 1 < message.length()) {
                    char next = message.charAt(i + 1);
                    if (next == 'k' || next == 'l' || next == 'm' || next == 'n' || next == 'o' || next == 'r') {
                        i++;
                        continue;
                    }
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static int toAbsolutePos(double pos) {
        return (int) (pos * 32.0D);
    }

    public static boolean isDoor(int blockId) {
        return blockId == 64 || blockId == 71;
    }

    public static boolean isTrapDoor(int blockId) {
        return blockId == 96 || blockId == 167;
    }

    public static boolean isSign(int blockId) {
        return blockId == 63 || blockId == 68;
    }

    public static int toAbsoluteRotation(float rotation) {
        return (int) (rotation * 256.0F / 360.0F);
    }

    public static int toBetaVelocity(double vec) {
        return (int) (vec * 8000.0D);
    }
}