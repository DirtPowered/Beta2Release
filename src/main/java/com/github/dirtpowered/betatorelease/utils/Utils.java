package com.github.dirtpowered.betatorelease.utils;

import com.github.dirtpowered.betaprotocollib.data.BetaItemStack;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.SetSlotPacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.pmw.tinylog.Logger;

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
                String legacy = TextComponent.toPlainText(ComponentSerializer.parse(strings.get("Text" + (line + 1)).getValue() + ""));
                signLines[line] = StringUtils.substring(legacy, 0, 15);
            } catch (Exception e) {
                signLines[line] = "error";
            }
        }
        return signLines;
    }

    public static void updateInventory(Session session, BetaItemStack currentItem) {
        session.sendPacket(new SetSlotPacketData(-1, -1, currentItem));
    }

    public static int toAbsolutePos(double pos) {
        return floor_double(pos * 32.0D);
    }

    private static int floor_double(double var0) {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }

    private static int floor_float(float var0) {
        int var1 = (int) var0;
        return var0 < (float) var1 ? var1 - 1 : var1;
    }

    public static int toAbsoluteRotation(float rotation) {
        return floor_float(rotation * 256.0F / 360.0F);
    }

    public static double toAbsoluteOffset(double pos) {
        return pos / 32.0D;
    }

    public static int toBetaVelocity(double vec) {
        return (int) (vec * 8000.0D);
    }

    public static void debug(Object clazz) {
        Logger.info("[DEBUG] {}", ReflectionToStringBuilder.toString(clazz, ToStringStyle.JSON_STYLE));
    }
}