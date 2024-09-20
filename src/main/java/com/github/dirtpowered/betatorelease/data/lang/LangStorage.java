package com.github.dirtpowered.betatorelease.data.lang;

import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.utils.Utils;
import net.lenni0451.mcstructs.text.ATextComponent;
import net.lenni0451.mcstructs.text.serializer.TextComponentSerializer;
import net.lenni0451.mcstructs.text.utils.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LangStorage {
    private static final Map<String, String> TRANSLATIONS = new HashMap<>(3000);

    static {
        // special translations for keys that are somehow sent by viabackwards due to some issues
        TRANSLATIONS.put("entity.minecraft.hopper_minecart", "Minecart with Hopper");
        TRANSLATIONS.put("entity.minecraft.chest_minecart", "Minecart with Chest");
    }

    public static void init() {
        try (InputStream input = LangStorage.class.getClassLoader().getResourceAsStream("lang/en_US.properties")) {
            if (input == null) {
                Main.LOGGER.warn("Language file not found in resources: en_US.properties");
                return;
            }
            Properties properties = new Properties();
            properties.load(input);

            int i = 0;
            for (String key : properties.stringPropertyNames()) {
                TRANSLATIONS.put(key, properties.getProperty(key));
                i++;
            }
            Main.LOGGER.info("Loaded " + i + " translations from en_US.properties");

        } catch (IOException e) {
            Main.LOGGER.error("Failed to load language file", e);
        }
    }

    public static String translate(String text, boolean forceTranslatable) {
        ATextComponent deserialized = TextComponentSerializer.V1_12.deserializeReader(text);
        if (forceTranslatable) {
            String legacy = deserialized.asLegacyFormatString();
            return Utils.stripUnsupportedColors(TRANSLATIONS.getOrDefault(legacy, legacy));
        }
        TextUtils.setTranslator(deserialized, TRANSLATIONS::get);
        return Utils.stripUnsupportedColors(deserialized.asLegacyFormatString());
    }
}