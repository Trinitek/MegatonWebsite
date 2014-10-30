package com.daexsys.megatonlogin.web.pages.forum;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MakeThreadPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        String op = httpExchange.getRemoteAddress().toString();
        String title = "";
        String body = "";

        String[] spl = uri.toString().split("\\?");

        String[] params = spl[1].split("\\&");

        String[] parts1 = params[0].split("\\=");
        String[] parts2 = params[1].split("\\=");

        title = parts1[1].replaceAll("\\+"," ");
        body = parts2[1].replaceAll("\\+"," ");

        ForumPage.addThread(new Thread(title, op), new Post(op, body));

//        String response = Server.getTop("Forum Post Made") + "Posted. <a href = '/forum'>Return to forum</a>";
        String response = "<script language = 'javascript'>window.location.href = '/forum'</script>";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();

        ForumPage.save();
    }
}