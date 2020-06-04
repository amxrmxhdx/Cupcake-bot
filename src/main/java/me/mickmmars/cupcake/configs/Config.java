package me.mickmmars.cupcake.configs;

import me.mickmmars.cupcake.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Config {

    public String token;
    public Map<String, ServerConfig> servers;

    public Config(File file) {
        try {
            String json = Files.readString(file.toPath(), StandardCharsets.US_ASCII);
            Config config = Main.gson.fromJson(json, Config.class);
            this.token = config.token;
            this.servers = config.servers;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(File file) {
        try {
            String json = Main.gson.toJson(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            file.delete();
            file.createNewFile();
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config createDefaultConfig(File file) throws IOException {
        String json = Files.readString(file.toPath(), StandardCharsets.US_ASCII);
        Config config = Main.gson.fromJson(json, Config.class);
        config.token = "TOKEN_HERE";
        config.servers = new HashMap<>();
        return config;
    }

}
