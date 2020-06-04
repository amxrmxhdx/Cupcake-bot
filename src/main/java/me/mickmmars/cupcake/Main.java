package me.mickmmars.cupcake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mickmmars.cupcake.configs.Config;
import me.mickmmars.cupcake.listeners.GuildJoinListener;
import me.mickmmars.cupcake.listeners.MessageListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static Config config;

    public static Gson gson;

    public static DiscordApi api;

    public static void main(String[] args) {
        try {
            gson = new GsonBuilder().setPrettyPrinting().create();

            File cfgFile = new File("config.json");
            if (!cfgFile.exists()) {
                cfgFile.createNewFile();
                config = Config.createDefaultConfig(cfgFile);
                System.out.println("Please type your token:");
                Scanner sn = new Scanner(System.in);
                String input = sn.nextLine();
                config.token = input;
            }

            String token = config.token;

            api = new DiscordApiBuilder().setToken(token).login().join();

            api.addListener(new MessageListener());
            api.addListener(new GuildJoinListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}