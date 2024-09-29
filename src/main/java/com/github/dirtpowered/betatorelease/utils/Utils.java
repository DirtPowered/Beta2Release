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

public class Utils {

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

    public static String[] getLegacySignLines(CompoundTag tag) {
        Map<String, Tag> strings = tag.getValue();
        String[] signLines = new String[4];

        for (int line = 0; line < 4; ++line) {
            try {
                String nTag = (String) strings.get("Text" + (line + 1)).getValue();
                signLines[line] = StringUtils.substring(LangStorage.translate(nTag, false), 0, 15);
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

    private static int floor_float(float var0) {
        int var1 = (int) var0;
        return var0 < (float) var1 ? var1 - 1 : var1;
    }

    public static boolean isDoor(int blockId) {
        return blockId == 64 || blockId == 71;
    }

    public static boolean isSign(int blockId) {
        return blockId == 63 || blockId == 68;
    }

    public static int toAbsoluteRotation(float rotation) {
        return floor_float(rotation * 256.0F / 360.0F);
    }

    public static int toBetaVelocity(double vec) {
        return (int) (vec * 8000.0D);
    }
}