package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SendMessagePage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        response = WebServer.getTop("Send Message", httpExchange.getRemoteAddress().toString()) +
                "<font size = '6'>Send Message.</font>";

        response += "<form action='recmessage.ijen'>";
        response += "Recipient: <input type = 'text' name = 'recipient'><br>";
//        response += "Subject: <input type = 'text' name = 'subject'><br>";
        response += "<textarea name = 'body' style = 'width:500px; height:150px;'></textarea><br>";
        response += "<input type = 'submit' value = 'Send Message'>";
        response += "</form";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();

    }
}
