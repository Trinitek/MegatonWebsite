package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.pages.forum.Post;
import com.daexsys.megatonlogin.web.util.LinkUtil;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.daexsys.megatonlogin.web.util.ParameterManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AddFriendScript implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ParameterManager parameterManager = new ParameterManager(httpExchange.getRequestURI().toString());

        Person userThatSend = LoginManager.getUser(httpExchange.getRemoteAddress().toString());

        String friendToadd = parameterManager.getValue("name");

        userThatSend.setFriend(LoginManager.getUserFromName(friendToadd));

        String response = "<script language = 'javascript'>window.location.href = '/mymessages'</script>";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }
}