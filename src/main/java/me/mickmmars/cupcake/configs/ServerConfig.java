package me.mickmmars.cupcake.configs;

import java.util.List;
import java.util.Map;

public class ServerConfig {

    public List<String> upvoteChannels;
    public String upvoteEmote;
    public String downvoteEmote;
    public List<String> autoRoles;
    public List<String> suggestChannels;
    public Map<String, Object> extras;

    public ServerConfig(List<String> upvoteChannels, String upvoteEmote, String downvoteEmote, List<String> autoRoles, List<String> suggestChannels, Map<String, Object> extras) {
        this.upvoteChannels = upvoteChannels;
        this.upvoteEmote = upvoteEmote;
        this.downvoteEmote = downvoteEmote;
        this.autoRoles = autoRoles;
        this.suggestChannels = suggestChannels;
        this.extras = extras;
    }

}
