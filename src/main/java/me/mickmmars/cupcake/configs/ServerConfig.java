package me.mickmmars.cupcake.configs;

import java.util.List;

public class ServerConfig {

    public List<String> upvoteChannels;
    public String upvoteEmote;
    public String downvoteEmote;
    public List<String> autoRoles;

    public ServerConfig(List<String> upvoteChannels, String upvoteEmote, String downvoteEmote, List<String> autoRoles) {
        this.upvoteChannels = upvoteChannels;
        this.upvoteEmote = upvoteEmote;
        this.downvoteEmote = downvoteEmote;
        this.autoRoles = autoRoles;
    }

}
