package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Social.Friend;

import java.util.ArrayList;

public class SocialManager {

    private ArrayList<Friend> friends = new ArrayList<>();

    public SocialManager() {

    }

    public void addFriend(String name) {
        this.friends.add(new Friend(name));
    }

    public void removeFriend(String name) {
        this.friends.removeIf(f -> f.getName().equalsIgnoreCase(name));
    }

    public boolean isFriend(String name) {
        return this.friends.stream().anyMatch(f -> f.getName().equalsIgnoreCase(name));
    }
}
