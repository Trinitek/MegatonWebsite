package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.TemporaryDatabase;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.daexsys.megatonlogin.web.WebServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthPage implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        String[] spl = uri.toString().split("\\?");
        String[] parts = spl[1].split("\\&");

        String name = parts[0].split("\\=")[1];

        System.out.println("Logging in...");

        if(WebServer.playerExists(name.toLowerCase())) {
            String pass = parts[1].split("\\=")[1];

            String hash = TemporaryDatabase.getPass(name);

            String response = "";

            if(Integer.parseInt(hash) == pass.hashCode()) {
                System.out.println("success");
                LoginManager.login(httpExchange.getRemoteAddress().toString(), name);
                response = WebServer.getTop("Login success!", httpExchange.getRemoteAddress().toString()) + "You are now logged in as " + name + "!";
            } else {
                response = WebServer.getTop("Login failed!", httpExchange.getRemoteAddress().toString()) + "Incorrect login details.";
            }

            httpExchange.sendResponseHeaders(200, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
            httpExchange.getResponseBody().close();
        } else {
            String derp =
                    "" +
                            WebServer.getTop("Login Failure", httpExchange.getRemoteAddress().toString()) +
                            "Unknown user!" +
                            "<a href = '/'>Back to home</a></center>";


            httpExchange.sendResponseHeaders(200, derp.length());
            httpExchange.getResponseBody().write(derp.getBytes());
            httpExchange.getResponseBody().close();
        }
    }
}