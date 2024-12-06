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
        configJson.addProperty("leavesWithSnowOnTopBlockstate", ModConfig.leavesWithSnowOnTopBlockstate);

        configJson.addProperty("correctSnowWithTime", ModConfig.doRandomTickSnowUpdates);
        configJson.addProperty("correctVinesWithTime", ModConfig.doRandomTickVineUpdates);
        configJson.addProperty("correctLeavesWithTime", ModConfig.doRandomTickLeavesUpdates);

        configJson.addProperty("updateSnowOnNeighborChange", ModConfig.updateSnowOnNeighborChange);
        configJson.addProperty("updateVineOnNeighborChange", ModConfig.updateVineOnNeighborChange);
        configJson.addProperty("updateLeavesOnNeighborChange", ModConfig.updateLeavesOnNeighborChange);
        configJson.addProperty("updateLeavesOnNeighborChangeOnlyAbove", ModConfig.updateLeavesOnNeighborChangeOnlyAbove);
        configJson.addProperty("maxSnowUpdateChain", ModConfig.maxSnowUpdateChain);
        configJson.addProperty("maxVineUpdateChain", ModConfig.maxVineUpdateChain);
        configJson.addProperty("maxLeavesUpdateChain", ModConfig.maxLeavesUpdateChain);

        configJson.addProperty("vinePriority", ModConfig.vinePriority.toString());

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
