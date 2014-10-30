package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.util.LinkUtil;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class ProfilePage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        String[] spl = uri.toString().split("\\?");
        String name = spl[1].split("\\=")[1];

        Person requester = LoginManager.getUser(httpExchange.getRemoteAddress().toString());
        Person person = LoginManager.getUserFromName(name);

        String response = "";

        // If this is requesters profile
        if(person.getUsername().equalsIgnoreCase(requester.getUsername())) {
            response = WebServer.getTop(name, httpExchange.getRemoteAddress().toString()) +
                    "</center>" +
                   person.getIconTag() + "<font size = '6'>" + name + "</font> - " + person.getDescription() + " - " + person.getPrivilage() +
                    "<br> "+ getStats(person) +
                    "<hr>" + getDescriptionModule(person) +
                    "<hr>" + getAvatarModule(person) +
                    "<p>"+ getFriendsModule(person);

        } else {
            person.pageView();

            response = WebServer.getTop(name, httpExchange.getRemoteAddress().toString()) +
                    "</center>" +
                    person.getIconTag() + "<font size = '6'>" + name + "</font> - " + person.getDescription() + " - " + person.getPrivilage() +
                    "<br> "+ getStats(person) +
                    "<hr>" + getDescriptionModule(person) +
                    getAvatarModule(person) +
                    "<p>" + getFriendsModule(person);

            if(!requester.isFriend(person)) {
                // Add as a friend button
                response += "<a href='/addfriend.ijen?name=" + person.getUsername() + "'>Add as a friend</a>";
            } else if(!person.isFriend(requester)) {
                response += "Your friend request has been sent!";
            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }

    public String getStats(Person person) {
        String ret = "<b>";

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        ret += numberFormat.format(person.getProfilePageviews()) + " pageviews - ";
        ret += numberFormat.format(person.getForumPostCount()) + " forum posts";

        ret += "</b>";

        return ret;
    }

    public String getDescriptionModule(Person person) {
        String response = "<font size = '5'><b>About:</b></font><br>";

        response += person.aboutMe();

        return response + "<hr>";
    }

    public String getFriendsModule(Person person) {
        String response = "<font size = '5'><b>Friends:</b></font><br>";

        if(person.getFriends().size() == 0) {
            response += "This person has no friends added!<br>";
        } else {
            for (Person person1 : person.getFriends()) {
                if (person1.isFriend(person)) {
                    response += person1.getIconTag() + LinkUtil.usernameToLink(person1.getUsername()) + "<br>";
                }
            }
        }
        return response;
    }

    public String getAvatarModule(Person person) {
        return "</center><font size = '5'><b>Avatar:</b></font><br><center>" + getAvatar(person) + "</center><hr>";
    }

    public String getAvatar(Person person) {
        return "<font color='white' size = '5'>"+person.getUsername()+"</font><br><table table-layout='fixed' width = 30 height = 30 bgcolor = '#"+ WebServer.hexCodeOfName(person.getUsername())+"'><tr><td></tr></td></table>";
    }

//    public String getVisitorList(Person person) {
//        String response = "<b>Last page visitors:</b><br>";
//
//        if(person.getFriends().size() == 0) {
//            response = "Nobody has viewed this page recently!";
//        } else {
//            for (Person person1 : person.getFriends()) {
//                if (person1.isFriend(person)) {
//                    response += LinkUtil.usernameToLink(person1.getUsername()) + "<br>";
//                }
//            }
//        }
//        return response;
//    }
}