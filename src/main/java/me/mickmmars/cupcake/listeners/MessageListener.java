package me.mickmmars.cupcake.listeners;

import me.mickmmars.cupcake.Main;
import me.mickmmars.cupcake.configs.ServerConfig;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.ExecutionException;

public class MessageListener implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getAttachments().size() == 1 && event.getMessage().getReadableContent().length() == 0) {
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.upvoteChannls.contains(event.getChannel().getIdAsString())) {
                CustomEmoji upvote = event.getApi().getCustomEmojiById("718163352165285938").get();
                CustomEmoji downvote = event.getApi().getCustomEmojiById("718163352144052314").get();
                try {
                    event.getMessage().addReaction(upvote).get();
                    event.getMessage().addReaction(downvote).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        if (event.getMessageContent().equalsIgnoreCase("c!addupvotechannel")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessage().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.upvoteChannls.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is already an upvote-channel.");
                return;
            }
            serverConfig.upvoteChannls.add(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully added <#" + event.getChannel().getId() + "> as an upvote-channel.");
        }
        if (event.getMessageContent().equalsIgnoreCase("c!removeupvotechannel")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessage().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (!serverConfig.upvoteChannls.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is already an upvote-channel.");
                return;
            }
            serverConfig.upvoteChannls.remove(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully removed <#" + event.getChannel().getId() + "> from being an upvote-channel.");
        }
    }

}
