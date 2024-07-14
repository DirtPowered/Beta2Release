package com.github.dirtpowered.betatorelease.data.remap;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class BlockMappings {
    private final static int MAX_SUPPORTED_BLOCK_ID = 97;
    private final static int DEFAULT_BLOCK_ID = 1;
    private final static int DEFAULT_ITEM_ID = 1;
    private final static Map<String, RemappedBlock> blockCache = new HashMap<>();
    private final static Map<String, RemappedItem> itemCache = new HashMap<>();
    private static Map<String, String> blockMappings = new HashMap<>();
    private static Map<String, String> itemMappings = new HashMap<>();

    private static void copyDefaultConfigFile(File configFile) {
        try {
            InputStream defaultConfigStream = BlockMappings.class.getResourceAsStream("/mappings.json");
            Preconditions.checkNotNull(defaultConfigStream, "Default mappings.json not found");

            Files.copy(defaultConfigStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.error(e, "Failed to copy default mappings.json");
        }
    }

    public static RemappedBlock getRemappedBlock(int blockId, int blockData) {
        Preconditions.checkArgument(blockData >= 0 && blockData <= 15, "block data must be between 0 and 15");

        String key = blockId + ":" + blockData;
        if (blockCache.containsKey(key))
            return blockCache.get(key);

        String remapped = blockMappings.get(key);
        if (remapped == null) {
            if (blockId >= MAX_SUPPORTED_BLOCK_ID)
                Logger.warn("Missing block mapping for block {}:{}", blockId, blockData);

            remapped = blockId + ":" + blockData;
        }
        String[] parts = remapped.split(":");

        int id = Integer.parseInt(parts[0]);
        int data = Integer.parseInt(parts[1]);

        RemappedBlock remappedBlock = (id >= MAX_SUPPORTED_BLOCK_ID) // check if block is supported
                ? new RemappedBlock(DEFAULT_BLOCK_ID)
                : new RemappedBlock(id, data);

        blockCache.put(key, remappedBlock);
        return remappedBlock;
    }

    public static RemappedItem getRemappedItem(int itemId, int itemData) {
        Preconditions.checkArgument(itemData >= 0 && itemData <= 15, "item data must be between 0 and 15");

        String key = itemId + ":" + itemData;
        if (itemCache.containsKey(key))
            return itemCache.get(key);

        String remapped = itemMappings.get(key);
        if (remapped == null) {
            Logger.warn("Missing item mapping for item {}:{}", itemId, itemData);
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
            Logger.error(e, "Failed to load mappings.json");
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