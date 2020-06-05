package me.mickmmars.cupcake.listeners;

import me.mickmmars.cupcake.Main;
import me.mickmmars.cupcake.configs.ServerConfig;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.util.concurrent.ExecutionException;

public class MemberJoinListener implements ServerMemberJoinListener {

    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        if (!event.getUser().isBot()) {
            ServerConfig config = Main.config.servers.get(event.getServer().getIdAsString());
            for (String s : config.autoRoles) {
                try {
                    event.getServer().getRoleById(s).get().addUser(event.getUser(), "Cupcake autorole").get();
                } catch (InterruptedException | ExecutionException e) {
                    event.getServer().getOwner().sendMessage("I did not have the permission to give " + event.getUser().getMentionTag() + " the role " + event.getServer().getRoleById(s).get().getMentionTag() + ". Required permission **Role management**");
                }
            }
        }
    }

}
