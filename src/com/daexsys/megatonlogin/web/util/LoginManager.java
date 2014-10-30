package com.daexsys.megatonlogin.web.util;

import com.daexsys.megatonlogin.web.pages.forum.Post;
import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.pages.people.Privelige;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static Map<String, Person> usersLoggedIn = new HashMap<String, Person>();
    private static Map<String, Person> usersByName = new HashMap<String, Person>();

    static {
        Person administrator = new Person("Administrator");
        administrator.setPrivelige(Privelige.ADMIN);
        usersByName.put("Administrator", administrator);
    }

    public static void login(String ip, String username) {
        ip = ip.split("\\:")[0];

        Person person = null;

        if(usersByName.containsKey(username)) {
            person = usersByName.get(username);
        } else {
            person = new Person(username);
        }

        usersLoggedIn.put(ip, person);
        usersByName.put(username, usersLoggedIn.get(ip));
    }

    public static void logout(String ip) {
        ip = ip.split("\\:")[0];

        Person person = usersLoggedIn.get(ip);

        usersLoggedIn.remove(ip);
    }

    public static Person getUserFromName(String username) {
        if(usersByName.containsKey(username)) {
            return usersByName.get(username);
        }

        Person newPerson = new Person(username);
        usersByName.put(username, newPerson);
        return newPerson;
    }

    public static Person getUser(String ip) {
        ip = ip.split("\\:")[0];

        if(ip.equalsIgnoreCase("Administrator")) {
            return usersByName.get("Administrator");
        }

        if(usersLoggedIn.containsKey(ip)) {
            Person person = usersLoggedIn.get(ip);
            person.activity();
            return person;
        } else {
            return new Person("Anonymous");
        }
    }

    public static String getUsername(String ip) {
        ip = ip.split("\\:")[0];

        return getUser(ip).getUsername();
    }
}
