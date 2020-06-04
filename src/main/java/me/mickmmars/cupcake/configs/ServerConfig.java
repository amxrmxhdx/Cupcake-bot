package me.mickmmars.cupcake.configs;

import java.util.List;

public class ServerConfig {

    public List<String> upvoteChannels;
    public String upvoteEmote;
    public String downvoteEmote;

    public ServerConfig(List<String> upvoteChannels, String upvoteEmote, String downvoteEmote) {
        this.upvoteChannels = upvoteChannels;
        this.upvoteEmote = upvoteEmote;
        this.downvoteEmote = downvoteEmote;
    }

}
