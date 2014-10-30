package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.pages.forum.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Person {
    private String username;

    private int profilePageviews = 0;

    private long timeLastActive;
    private Privelige privelige;
    private List<Person> friends = new ArrayList<Person>();

    private List<Post> messages = new ArrayList<Post>();
    private List<Post> oldMessages = new ArrayList<Post>();

    private List<Post> forumPostsMade = new ArrayList<Post>();

    public Person(String username) {
        boolean newAccount = true;

        this.username = username;
        timeLastActive = System.currentTimeMillis();
        privelige = Privelige.STANDARD;

        if(username.equalsIgnoreCase("Administrator")) return;

        if(newAccount) {
            sendMessage(new Post("Administrator", "Welcome to the site!"));
        }

        if(username.equalsIgnoreCase("Cactose") || username.equalsIgnoreCase("Trin")) {
            privelige = Privelige.ADMIN;
        }
    }

    public String getUsername() {
        return username;
    }

    public Privelige getPrivilage() {
        return privelige;
    }

    public String getHumanReadableTimeLastActive() {
        return new SimpleDateFormat("MM/dd/YYYY HH:mm", Locale.ENGLISH).format(getTimeLastActive());
    }

    public long getTimeLastActive() {
        return timeLastActive;
    }

    public String getDescription() {
        if(getUsername().equalsIgnoreCase("Administrator")) {
            return "Registered member since the day before the universe was created.";
        }
        return "A pretty cool person.";
    }

    public void activity() {
        timeLastActive = System.currentTimeMillis();
    }

    public boolean isFriend(Person person) {
        return friends.contains(person);
    }

    public void setFriend(Person person) {
        if(!friends.contains(person)) {
            if(!person.isFriend(this)) {
                person.sendMessage(new Post("FriendService", getUsername() + " would like to add you as a friend! <a href = '/addfriend.ijen?name=" + getUsername() + "'>Confirm</a>"));
            } else {
                sendMessage(new Post("FriendService", "Woot! You and "+person.getUsername()+" are now friends"));
                person.sendMessage(new Post("FriendService", "Woot! You and " + getUsername() + " are now friends"));
            }
            friends.add(person);
        }
    }

    public void sendMessage(Post post) {
        messages.add(post);
    }

    public int getNumberOfMessages() {
        return messages.size();
    }

    public void pageView() {
        profilePageviews++;
    }

    public int getProfilePageviews() {
        return profilePageviews;
    }

    public List<Post> getNewMessages() {
        return messages;
    }

    public void setPrivelige(Privelige privelige) {
        this.privelige = privelige;
    }

    public List<Post> getOldMessages() {
        return oldMessages;
    }

    public void addForumPost(Post post) {
        forumPostsMade.add(post);
    }

    public int getForumPostCount() {
        return forumPostsMade.size();
    }

    public String getIconLocation() {
        if(ifAvatarExists()) {
            return WebServer.assets + "/html/" + getUsername() + ".png";
        } else {
            return WebServer.assets + "/html/default.png";
        }
    }

    private boolean ifAvatarExists() {
        if(getUsername().equalsIgnoreCase("Administrator") || getUsername().equalsIgnoreCase("Cactose")) {
            return true;
        }
        return false;
    }

    public String getIconTag() {
        return "<img src='"+getIconLocation()+"' width = 40 height = 40>";
    }

    public List<Person> getFriends() {
        return friends;
    }
}
