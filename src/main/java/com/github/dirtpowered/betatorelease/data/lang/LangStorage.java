package com.github.dirtpowered.betatorelease.data.lang;

import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.utils.Utils;
import net.lenni0451.mcstructs.text.ATextComponent;
import net.lenni0451.mcstructs.text.serializer.TextComponentSerializer;
import net.lenni0451.mcstructs.text.utils.TextUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LangStorage {
    private static final Map<String, String> TRANSLATIONS = new HashMap<>(3500);
    private static final String SOURCE_URL = "https://raw.githubusercontent.com/DirtPowered/minecraft-assets/1.12.2/assets/minecraft/lang/%s.lang";
    private static final String LANG_DIR = "lang";

    static {
        // special translations for keys that are somehow sent by viabackwards due to some issues
        TRANSLATIONS.put("entity.minecraft.hopper_minecart", "Minecart with Hopper");
        TRANSLATIONS.put("entity.minecraft.chest_minecart", "Minecart with Chest");
    }

    public static void init(Server server) {
        String code = server.getConfiguration().getLocale().getCode();
        String name = code + ".lang";
        Path path = Paths.get(LANG_DIR, name);

        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Main.LOGGER.info("Downloading {} language file...", code);
                try (InputStream in = new URL(String.format(SOURCE_URL, code)).openStream()) {
                    Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                    Main.LOGGER.info("Downloaded successfully");
                }
            }
        } catch (Exception e) {
            Main.LOGGER.error("Failed to create language folder or download language file for locale: {}", code, e);
            return;
        }

        // load as UTF-8 to support lang-specific characters properly
        try (InputStream input = new FileInputStream(path.toFile()); Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            int i = 0;
            for (String key : properties.stringPropertyNames()) {
                TRANSLATIONS.put(key, properties.getProperty(key));
                i++;
            }
            Main.LOGGER.info("Loaded {} translations from {}", i, name);
        } catch (IOException e) {
            Main.LOGGER.error("Failed to load language file: {}", name, e);
        }
    }

    private static String transformText(ATextComponent deserialized) {
        String legacy = deserialized.asLegacyFormatString();
        legacy = Utils.stripUnsupportedColors(legacy);
        legacy = StringUtils.stripAccents(legacy); // remove accents, as beta doesn't support them anyway
        return legacy;
    }

    public static String translate(String text, boolean forceTranslatable) {
        ATextComponent deserialized = TextComponentSerializer.V1_12.deserializeReader(text);
        if (forceTranslatable) {
            String legacy = transformText(deserialized);
            return TRANSLATIONS.getOrDefault(legacy, legacy);
        }
        TextUtils.setTranslator(deserialized, TRANSLATIONS::get);
        return transformText(deserialized);
    }
}