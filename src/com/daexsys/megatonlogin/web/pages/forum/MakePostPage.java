package com.daexsys.megatonlogin.web.pages.forum;

import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MakePostPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        String[] spl = uri.toString().split("\\?");
        String[] params = spl[1].split("\\&");

        String[] parts1 = params[0].split("\\=");
        String[] parts2 = params[1].split("\\=");

        String postBody = parts1[1];
        String url = parts2[1].replaceAll("%2520", "%20");

        System.out.println("Post: "+postBody);
        System.out.println("URL: "+url);

        Person person = LoginManager.getUser(httpExchange.getRemoteAddress().toString());

        Post post = new Post(httpExchange.getRemoteAddress().toString(), postBody.replaceAll("\\+"," "));
        ForumPage.forum.get(url).addPost(post);
        person.addForumPost(post);

        System.out.println(uri);
        String response = "<script language = 'javascript'>window.location.href = '/forum?thread=" + url + "'</script>";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();

        ForumPage.save();
    }
}
