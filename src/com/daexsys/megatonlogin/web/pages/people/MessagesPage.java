package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.pages.forum.Post;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MessagesPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        Person person = LoginManager.getUser(httpExchange.getRemoteAddress().toString());

        String response = WebServer.getTop(person.getUsername() + "'s messages", httpExchange.getRemoteAddress().toString()) +
                "</center>" +
                "<font size = '6'>" + person.getUsername() + "'s messages</font>";

        response += "<br><a href = '/sendmessage'>Send message</a><hr>";

        response += "<font size = '5'> New messages</font><hr>";
        for (Post post : person.getNewMessages()) {
            response += post.format() + "<hr>";
        }

        response += "<font size = '5'> Old messages</font><hr>";
        for(Post post : person.getOldMessages()) {
            response += post.format() + "<hr>";
        }

        for(Post post : person.getNewMessages()) {
            person.getOldMessages().add(post);
        }

        person.getNewMessages().clear();

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }
}