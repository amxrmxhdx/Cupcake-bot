package me.mickmmars.cupcake.listeners;

import me.mickmmars.cupcake.Main;
import me.mickmmars.cupcake.configs.ServerConfig;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.util.ArrayList;

public class GuildJoinListener implements ServerJoinListener {

    @Override
    public void onServerJoin(ServerJoinEvent event) {
        Main.config.servers.put(event.getServer().getIdAsString(), new ServerConfig(new ArrayList<>()));
    }

}