package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LoginPage implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("login page");
        String response = "";

        response = WebServer.getTop("Login Page", httpExchange.getRemoteAddress().toString()) +
                "</center>" +
                "<font size = '6'>Login</font>" +

                "<form action='auth.ijen'>" +
                "Username: <input type = 'text' name='user'><br>" +
                "Password: <input type = 'password' name='pass'><br>" +
                "<input type = 'submit' value='Login'>" +
                "</form>";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }
}
