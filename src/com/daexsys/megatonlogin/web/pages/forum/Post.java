package com.daexsys.megatonlogin.web.pages.forum;

import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.util.LinkUtil;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A post of any kind, be it on a forum, a comment, an inbox message...
 */
public class Post {
    private static int NEW_POST_ID = 1;
    private String username = "null";
    private String text = "null";
    private long timePosted;
    private int postID;

    public Post(String username, String text) {
        this.username = LoginManager.getUsername(username);

        postID = NEW_POST_ID;
        NEW_POST_ID++;

        this.text = LinkUtil.removeGarbage(text);
        timePosted = System.currentTimeMillis();
    }

    public int getPostID() {
        return postID;
    }

    public String getUsername() {
        return username;
    }

    public Person getUser() {
        return LoginManager.getUserFromName(getUsername());
    }

    public String getText() {
        return text;
    }

    public long getTimePosted() {
        return timePosted;
    }

    public String getHumanReadableDate() {
        return new SimpleDateFormat("MM/dd/YYYY HH:mm", Locale.ENGLISH).format(getTimePosted());
    }

    public String format() {
        return "<b>" + getUser().getIconTag() + LinkUtil.usernameToLink(getUsername()) + "</b>" +
                " said on "+getHumanReadableDate()+":<br>" + getText();
    }
}
