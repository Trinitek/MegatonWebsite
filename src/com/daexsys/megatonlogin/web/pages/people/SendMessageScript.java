package com.daexsys.megatonlogin.web.pages.people;

import com.daexsys.megatonlogin.web.WebServer;
import com.daexsys.megatonlogin.web.pages.forum.Post;
import com.daexsys.megatonlogin.web.util.LinkUtil;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.daexsys.megatonlogin.web.util.ParameterManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SendMessageScript implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ParameterManager parameterManager = new ParameterManager(httpExchange.getRequestURI().toString());


        String userThatSend = LoginManager.getUser(httpExchange.getRemoteAddress().toString()).getUsername();
        System.out.println("USER "+userThatSend);

        String recipient = parameterManager.getValue("recipient");
        String subject = parameterManager.getValue("subject");
        String body = parameterManager.getValue("body").replaceAll("\\+", " ");
        body = LinkUtil.removeGarbage(body);

        System.out.println("Recipient: "+recipient);
        System.out.println("Body: "+body);

        body = LinkUtil.removeGarbage(body);

        Person person = LoginManager.getUserFromName(recipient);
        person.sendMessage(new Post(httpExchange.getRemoteAddress().toString(), body));

        String response = "<script language = 'javascript'>window.location.href = '/mymessages'</script>";

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }
}
