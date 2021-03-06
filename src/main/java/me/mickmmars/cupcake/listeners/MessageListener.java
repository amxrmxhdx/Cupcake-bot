package me.mickmmars.cupcake.listeners;

import me.mickmmars.cupcake.Main;
import me.mickmmars.cupcake.configs.ServerConfig;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.io.File;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

public class MessageListener implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getAttachments().size() == 1) {
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.upvoteChannels.contains(event.getChannel().getIdAsString())) {
                CustomEmoji upvote = event.getApi().getCustomEmojiById(serverConfig.upvoteEmote).get();
                CustomEmoji downvote = event.getApi().getCustomEmojiById(serverConfig.downvoteEmote).get();
                try {
                    event.getMessage().addReaction(upvote).get();
                    event.getMessage().addReaction(downvote).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        if (event.getMessageContent().toLowerCase().startsWith("suggestion:")) {
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.suggestChannels.contains(event.getChannel().getIdAsString())) {
                CustomEmoji upvote = event.getApi().getCustomEmojiById(serverConfig.upvoteEmote).get();
                CustomEmoji downvote = event.getApi().getCustomEmojiById(serverConfig.downvoteEmote).get();
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
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.upvoteChannels.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is already an upvote-channel.");
                return;
            }
            serverConfig.upvoteChannels.add(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully added <#" + event.getChannel().getId() + "> as an upvote-channel.");
            Main.config.save(new File("config.json"));
        }
        if (event.getMessageContent().equalsIgnoreCase("c!removeupvotechannel")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (!serverConfig.upvoteChannels.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is not an upvote-channel.");
                return;
            }
            serverConfig.upvoteChannels.remove(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully removed <#" + event.getChannel().getId() + "> from being an upvote-channel.");
            Main.config.save(new File("config.json"));
        }
        if (event.getMessageContent().startsWith("c!setupvote")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            String[] args = event.getMessageContent().split(" ");
            if (args.length == 1) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this emoji does not exist.");
                return;
            }
            CustomEmoji emoji = event.getMessage().getCustomEmojis().get(0);
            if (emoji == null) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this emoji does not exist.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            serverConfig.upvoteEmote = emoji.getIdAsString();
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully changed the upvote-emote for this discord.");
            Main.config.save(new File("config.json"));
        }
        if (event.getMessageContent().startsWith("c!setdownvote")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            String[] args = event.getMessageContent().split(" ");
            if (args.length == 1) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this emoji does not exist.");
                return;
            }
            CustomEmoji emoji = event.getMessage().getCustomEmojis().get(0);
            if (emoji == null) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this emoji does not exist.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            serverConfig.downvoteEmote = emoji.getIdAsString();
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully changed the downvote-emote for this discord.");
            Main.config.save(new File("config.json"));
        }
        if (event.getMessageContent().equalsIgnoreCase("c!info")) {
            long membersCount = 0;
            for (Server server : event.getApi().getServers())
                membersCount += server.getMemberCount();
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Cupcake")
                    .setAuthor("Cupcake-bot", null, event.getApi().getYourself().getAvatar().getUrl().toString())
                    .setColor(Color.MAGENTA)
                    .setDescription("Feeding **" + membersCount + "** people with cupcakes")
                    .addInlineField("Servers", event.getApi().getServers().size() + "")
                    .addInlineField("Members", membersCount + "")
                    .addField("Developer", "MickMMars#0666")
                    .setTimestampToNow()
                    .setFooter("Requested by " + event.getMessageAuthor().getDisplayName(), event.getMessageAuthor().getAvatar()));
        }
        if (event.getMessageContent().equalsIgnoreCase("c!help")) {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Cupcake-help")
                    .setAuthor("Cupcake-bot", null, event.getApi().getYourself().getAvatar().getUrl().toString())
                    .setColor(Color.MAGENTA)
                    .setTimestampToNow()
                    .setFooter("Requested by " + event.getMessageAuthor().getDisplayName(), event.getMessageAuthor().getAvatar())
                    .setDescription("```md\n" +
                            "* c!addUpvoteChannel - Add the current channel as an upvotechannel\n" +
                            "* c!removeUpvoteChannel - Remove the current channel from being an upvotechannel\n" +
                            "* c!info - show bot information\n" +
                            "* c!help -  this command\n" +
                            "* c!setupvote <emoji> - set this servers upvote-emoji\n" +
                            "* c!setdownvote <emoji> - set this servers downvote-emoji\n" +
                            "* c!addautorole <role1> [role2] [role3]... - add autoroles\n" +
                            "* c!removeautorole <role1> [role2] [role3]... - remove autoroles\n" +
                            "* c!addSuggestionChannel - Add the current channel as a suggestion channel\n" +
                            "* c!removeSuggestionChannel - Remove the current channel as a suggestion channel\n" +
                            "```")
                    .addField("Invite me!", event.getApi().createBotInvite(Permissions.fromBitmask(1342310464)))
            );
        }
        if (event.getMessageContent().startsWith("c!addautorole")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            if (event.getMessage().getMentionedRoles().size() == 0) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you need to give me at least one role.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            StringJoiner rj = new StringJoiner(", ");
            for (Role role : event.getMessage().getMentionedRoles()) {
                if (!serverConfig.autoRoles.contains(role.getIdAsString()))
                    serverConfig.autoRoles.add(role.getIdAsString());
                rj.add(role.getMentionTag());
            }
            Main.config.save(new File("config.json"));
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, Successfully added following roles as autoroles: " + rj.toString());
        }
        if (event.getMessageContent().startsWith("c!removeautorole")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            if (event.getMessage().getMentionedRoles().size() == 0) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you need to give me at least one role.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            StringJoiner rj = new StringJoiner(", ");
            for (Role role : event.getMessage().getMentionedRoles()) {
                serverConfig.autoRoles.remove(role.getIdAsString());
                if (serverConfig.autoRoles.contains(role.getIdAsString()))
                    rj.add(role.getMentionTag());
            }
            Main.config.save(new File("config.json"));
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, Successfully removed following roles from being autoroles: " + rj.toString());
        }
        if (event.getMessageContent().startsWith("c!addsuggestionchannel")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (serverConfig.suggestChannels.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is already a suggestion-channel.");
                return;
            }
            serverConfig.suggestChannels.add(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully added <#" + event.getChannel().getId() + "> as an suggestion-channel.");
            Main.config.save(new File("config.json"));
        }
        if (event.getMessageContent().equalsIgnoreCase("c!removesuggestionchannel")) {
            if (!event.getServer().get().hasPermission(event.getServer().get().getMemberById(event.getMessageAuthor().getId()).get(), PermissionType.ADMINISTRATOR)) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, you don't have enough permissions to perform this command.");
                return;
            }
            ServerConfig serverConfig = Main.config.servers.get(event.getServer().get().getIdAsString());
            if (!serverConfig.suggestChannels.contains(event.getChannel().getIdAsString())) {
                event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, this channel is not a suggest-channel.");
                return;
            }
            serverConfig.suggestChannels.remove(event.getChannel().getIdAsString());
            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, successfully removed <#" + event.getChannel().getId() + "> from being an suggest-channel.");
            Main.config.save(new File("config.json"));
        }
    }

}