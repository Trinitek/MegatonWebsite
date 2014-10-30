package com.daexsys.megatonlogin.web.pages.forum;

import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.pages.people.Privelige;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.daexsys.megatonlogin.web.util.ParameterManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LockThread implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Person sender = LoginManager.getUser(httpExchange.getRemoteAddress().toString());

        if(sender.getPrivilage() == Privelige.STANDARD) {
            return;
        }

        ParameterManager parameterManager = new ParameterManager(httpExchange.getRequestURI().toString());

        String name = parameterManager.getValue("name");
        String response = "<script language = 'javascript'>window.location.href = '/forum?thread=" + name + "'</script>";

        ForumPage.forum.get(name).setLocked(!ForumPage.forum.get(name).isLocked());

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();

        ForumPage.save();
    }
}
