package me.mickmmars.cupcake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mickmmars.cupcake.configs.Config;
import me.mickmmars.cupcake.listeners.GuildJoinListener;
import me.mickmmars.cupcake.listeners.MemberJoinListener;
import me.mickmmars.cupcake.listeners.MessageListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

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
                config.save(cfgFile);
            } else {
                config = new Config(cfgFile);
            }

            String token = config.token;

            api = new DiscordApiBuilder().setToken(token).login().join();

            api.updateActivity(ActivityType.LISTENING, "your heart <3");

            api.addListener(new MessageListener());
            api.addListener(new GuildJoinListener());
            api.addListener(new MemberJoinListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
