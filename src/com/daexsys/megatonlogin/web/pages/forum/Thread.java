package com.daexsys.megatonlogin.web.pages.forum;

import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.util.LinkUtil;
import com.daexsys.megatonlogin.web.util.LoginManager;

import java.util.ArrayList;
import java.util.List;

public class Thread {
    private String name = "DefaultThread";

    private boolean locked = false;
    private boolean stickied = false;

    private String creator;
    private List<Post> posts = new ArrayList<Post>();

    public Thread(String name, String creator) {
        this.name = name;
        this.creator = LoginManager.getUsername(creator);
    }

    public boolean isStickied() {
        return stickied;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setStickied(boolean stickied) {
        this.stickied = stickied;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public String getName() {
        return name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Post getLastPost() {
        return posts.get(posts.size() - 1);
    }

    public int getNumberOfPosts() {
        return getPosts().size();
    }

    public Person getLastPoster() {
        return posts.get(getPosts().size() - 1).getUser();
    }

    public String getThreadCreator() {
        return creator;
    }

    public String format() {
        return "<a href = '?thread=" + getName() + "'><b>"+LinkUtil.removeGarbage(getName())+"</b></a> by " + getThreadCreator() +
                " - " +
                "Posts: " + getNumberOfPosts()
                +
                " - " +
                "Last post by " + LinkUtil.usernameToLink(getLastPost().getUsername())
                + (isStickied() ? " - <b>STICKIED</b>" : "")
                + (isLocked() ? " - <b>LOCKED</b>" : "")
                + "<hr>";

    }
}
