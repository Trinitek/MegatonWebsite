package com.daexsys.megatonlogin.web.util;

public class LinkUtil {
    public static String usernameToLink(String username) {
        return "<a href = '/people.ijen?name=" + username + "'>" + username + "</a>";
    }

    public static String removeGarbage(String text) {
        text = text.replaceAll("%27","'");
        text = text.replaceAll("%2C",",");
        text = text.replaceAll("%21","!");
        text = text.replaceAll("%3F","?");

        return text;
    }
}
