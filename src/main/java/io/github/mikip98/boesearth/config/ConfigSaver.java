package io.github.mikip98.boesearth.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigSaver {
    public static void saveConfigToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "boe-s-earth.json");

        // Create a JSON object to store the configuration
        JsonObject configJson = new JsonObject();

        configJson.addProperty("snowOnLeavesBlockstate", ModConfig.snowOnLeavesBlockstate);
        configJson.addProperty("vinesOnLeavesBlockstate", ModConfig.vinesOnLeavesBlockstate);
        configJson.addProperty("carpetOnLeavesBlockstate", ModConfig.carpetOnLeavesBlockstate);
        configJson.addProperty("correctSnowWithTime", ModConfig.correctSnowWithTime);
        configJson.addProperty("correctVinesWithTime", ModConfig.correctVinesWithTime);

        configJson.addProperty("vinePriority", ModConfig.vinePriority.toString());

        configJson.addProperty("VinePrioritiseLeaves", ModConfig.VinePrioritiseLeaves);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
