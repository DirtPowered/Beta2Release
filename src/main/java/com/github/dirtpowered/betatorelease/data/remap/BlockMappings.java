package com.github.dirtpowered.betatorelease.data.remap;

import com.github.dirtpowered.betaprotocollib.utils.ItemUtil;
import com.github.dirtpowered.betatorelease.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockMappings {
    private final static int MAX_SUPPORTED_BLOCK_ID = 97;
    private final static int DEFAULT_BLOCK_ID = 1;
    private final static int DEFAULT_ITEM_ID = 1;
    private final static Map<Integer, RemappedBlock> blockCache = new ConcurrentHashMap<>();
    private final static Map<Integer, RemappedItem> itemCache = new ConcurrentHashMap<>();

    private static Map<String, String> blockMappings = new HashMap<>();
    private static Map<String, String> itemMappings = new HashMap<>();

    private static void copyDefaultConfigFile(File configFile) {
        try {
            InputStream defaultConfigStream = BlockMappings.class.getResourceAsStream("/mappings.json");
            if (defaultConfigStream == null)
                throw new IllegalStateException("Default mappings.json not found");

            Files.copy(defaultConfigStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Main.LOGGER.error("Failed to copy default mappings.json", e);
        }
    }

    private static int packBlockKey(int blockId, int blockData) {
        return (blockId << 4) | blockData & 0xF;
    }

    public static RemappedBlock getRemappedBlock(int blockId, int blockData) {
        if (blockData < 0 || blockData > 15)
            throw new IllegalArgumentException("block data must be between 0 and 15");

        int key = packBlockKey(blockId, blockData);
        RemappedBlock cachedBlock = blockCache.get(key);
        if (cachedBlock != null)
            return cachedBlock;

        String keyStr = blockId + ":" + blockData;
        String remapped = blockMappings.get(keyStr);
        if (remapped == null) {
            if (blockId >= MAX_SUPPORTED_BLOCK_ID)
                Main.LOGGER.warn("Missing block mapping for block {}:{}", blockId, blockData);

            remapped = blockId + ":" + blockData;
        }
        String[] parts = remapped.split(":");

        int id = Integer.parseInt(parts[0]);
        int data = Integer.parseInt(parts[1]);

        RemappedBlock remappedBlock = (id >= MAX_SUPPORTED_BLOCK_ID)
                ? new RemappedBlock(DEFAULT_BLOCK_ID)
                : new RemappedBlock(id, data);

        blockCache.put(key, remappedBlock);
        return remappedBlock;
    }

    public static RemappedItem getRemappedItem(int itemId, int itemData) {
        // skip remapping for damageable items, we need to preserve item data (durability)
        if (ItemUtil.isDamageable(itemId))
            return new RemappedItem(itemId, itemData);

        int key = packBlockKey(itemId, itemData);

        RemappedItem cachedItem = itemCache.get(key);
        if (cachedItem != null)
            return cachedItem;

        String keyStr = itemId + ":" + itemData;
        String remapped = itemMappings.get(keyStr);
        if (remapped == null) {
            // handle items with durability by trying to find the base item
            String itemDataRemapped = itemMappings.get(itemId + ":0"); // find base item

            if (itemDataRemapped != null)
                return new RemappedItem(Integer.parseInt(itemDataRemapped.split(":")[0]), itemData);

            // if item data is not found, use default
            Main.LOGGER.warn("Missing item mapping for item {}:{}", itemId, itemData);
            remapped = DEFAULT_ITEM_ID + ":" + 0;
        }
        String[] parts = remapped.split(":");

        RemappedItem remappedItem = new RemappedItem(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        itemCache.put(key, remappedItem);
        return remappedItem;
    }

    public static RemappedBlock getRemappedBlock(int blockId) {
        return getRemappedBlock(blockId, 0);
    }

    public static void init() {
        File configFile = new File("mappings.json");

        if (!configFile.exists())
            copyDefaultConfigFile(configFile);

        try (FileReader reader = new FileReader(configFile)) {
            Type mappingsType = new TypeToken<Mappings>() {
                // empty
            }.getType();

            Mappings mappings = new Gson().fromJson(reader, mappingsType);

            blockMappings = mappings.getMappings().getBlocks();
            itemMappings = mappings.getMappings().getItems();
        } catch (IOException e) {
            Main.LOGGER.error("Failed to load mappings.json", e);
        }
    }

    @Setter
    @Getter
    public static class Mappings {
        private NestedMappings mappings;
    }

    @Setter
    @Getter
    public static class NestedMappings {
        private Map<String, String> blocks;
        private Map<String, String> items;
    }

    public record RemappedBlock(int blockId, int blockData) {
        public RemappedBlock(int blockId) {
            this(blockId, 0);
        }
    }

    public record RemappedItem(int itemId, int itemData) {
        // empty
    }
}