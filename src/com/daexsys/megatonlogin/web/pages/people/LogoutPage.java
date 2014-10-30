package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LogoutPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        LoginManager.logout(httpExchange.getRemoteAddress().toString());

        response = WebServer.getTop("Logout Page", httpExchange.getRemoteAddress().toString()) +
                "<font size = '6'>Logged out.</font>";
        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();

    }
}
