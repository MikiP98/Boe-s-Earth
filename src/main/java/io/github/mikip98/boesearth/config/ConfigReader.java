package io.github.mikip98.boesearth.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

import static io.github.mikip98.boesearth.BoesEarth.LOGGER;

public class ConfigReader {
    public static void loadConfigFromFile() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "boe-s-earth.json");

        if (configFile.exists()) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject configJson = gson.fromJson(reader, JsonObject.class);

                boolean needsUpdating = false;
                if (configJson != null) {
                    // Load the static fields from the JSON object
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "snowOnLeavesBlockstate");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "vinesOnLeavesBlockstate");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "carpetOnLeavesBlockstate");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "leavesWithSnowOnTopBlockstate");
                    ModConfig.isOnLeavesBlockstate = ModConfig.snowOnLeavesBlockstate && ModConfig.carpetOnLeavesBlockstate;

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "correctSnowWithTime");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "correctVinesWithTime");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "correctLeavesWithTime");

                    needsUpdating |= tryLoad(configJson, element -> gson.fromJson(element, VinePriority.class), "vinePriority");
                    ModConfig.VinePrioritiseLeaves = ModConfig.vinePriority == VinePriority.LEAVES;
                }

                if (needsUpdating) {
                    LOGGER.info("Updating config file to include new values and fix the broken ones");
                    ConfigSaver.saveConfigToFile();  // Update the config file to include new values
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static <T> boolean tryLoad(JsonObject configJson, Function<JsonElement, T> getter, String fieldName) {
        try {
            T value = getter.apply(configJson.get(fieldName));
            ModConfig.class.getField(fieldName).set(ModConfig.class, value);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
