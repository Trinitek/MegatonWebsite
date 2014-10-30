package com.daexsys.megatonlogin.web;

import com.daexsys.megatonlogin.LoginServer;
import com.daexsys.megatonlogin.TemporaryDatabase;
import com.daexsys.megatonlogin.web.pages.forum.*;
import com.daexsys.megatonlogin.web.pages.people.*;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Formatter;

public class WebServer {
    public static final String theSite = "http://dxvpn.phaeton.co";
    public static final String assets = "http://dxvpn.phaeton.co:8080";

    public static void start() {
        LoginServer.logger.info("Starting web server");
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

            //Global pages
            httpServer.createContext("/", new HomePage());
            httpServer.createContext("/news", new NewsPage());
            httpServer.createContext("/servers", new ServerListPage());
            httpServer.createContext("/forum", new ForumPage());

            //Auth pages
            httpServer.createContext("/login", new LoginPage());
            httpServer.createContext("/logout", new LogoutPage());

            // Thread control
            httpServer.createContext("/stickythread", new StickyThread());
            httpServer.createContext("/lockthread", new LockThread());

            // Message pages
            httpServer.createContext("/mymessages", new MessagesPage());
            httpServer.createContext("/sendmessage", new SendMessagePage());

            httpServer.createContext("/recmessage.ijen", new SendMessageScript());
            httpServer.createContext("/addfriend.ijen", new AddFriendScript());

            // Used to make accounts
            httpServer.createContext("/makeaccount.ijen", new MakeAccountPage());
            httpServer.createContext("/people.ijen", new ProfilePage());

            // Used to make forum threads
            httpServer.createContext("/makethread.ijen", new MakeThreadPage());

            // Used to login
            httpServer.createContext("/auth.ijen", new AuthPage());

            // Used to make posts on forum threads
            httpServer.createContext("/makepost.ijen", new MakePostPage());

            // Used to determinate the potential color of a player.
            httpServer.createContext("/playercolor.ijen", new Response3());
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class HomePage implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String derp =
                    "" +
                            getTop("Megaton Account Creation Page", httpExchange.getRemoteAddress().toString()) +

                            "<center><img src = '"+assets+"/screenshot.png'><p>"+

                            "<font size = 7><b>"+
                            "<img src = '"+assets+"/undandylion.png'>Create a Megaton Account." +
                            "</b></font>"+

                            "<font size = 5>"+
                            "<form action = 'makeaccount.ijen' method='GET'>" +
                            "Username: <input type = 'textbox' name = 'name'><br>" +
                            "Password: <input type = 'password' name = 'pass'><br>" +
                            "<input type = 'submit' value = 'Create'>" +
                            "</form>" +
                            "</font>"+


                            "<hr>"+
                            "<b><font size = 6>Tweets from @Cactose</font></b>"+
                            "<p>"+
                            "<a class=\"twitter-timeline\" href=\"https://twitter.com/Cactose\" data-widget-id=\"523531660348768256\">Tweets by @Cactose</a>\n" +
                            "<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>" +
                            "<hr>" +
                            "<font size = 3><a href = 'https://github.com/Cactose/Megaton'>It's on github!</a>";


            httpExchange.sendResponseHeaders(200, derp.length());
            httpExchange.getResponseBody().write(derp.getBytes());
            httpExchange.getResponseBody().close();
        }
    }

    static class MakeAccountPage implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            java.net.URI uri = httpExchange.getRequestURI();

            String[] spl = uri.toString().split("\\?");
            String[] parts = spl[1].split("\\&");

            String name = parts[0].split("\\=")[1];

            if(!playerExists(name.toLowerCase())) {
                String pass = parts[1].split("\\=")[1];

                TemporaryDatabase.addPlayer(name, pass.hashCode() + "");

                String derp =
                        "" +
                                getTop("Account successfully created.", httpExchange.getRemoteAddress().toString())+

                                "<script language = 'javascript'>" +
                                "document.cookie = 'cookie1='" +
                                "</script>"+

                                "<center>" +
                                "<font size = 6><b>"+
                                "Congratulations, "+name+"!</b></font>"+
                                "<font size = 5>"+
                                "<p><font color = 'white'>" +name+ "<font color = 'black'>"+
                                "<table table-layout='fixed' width = 30 height = 30 bgcolor = '#"+hexCodeOfName(name)+"'><tr><td></tr></td></table><p>"+
                                "Your account has been created.<br>" +
                                "Now <a href = '"+assets+"/Megaton.exe'>download the launcher</a> to start playing today!<p>" +

                                "</font>"+
                                "<a href = '/'>Back to home</a></center>";


                httpExchange.sendResponseHeaders(200, derp.length());
                httpExchange.getResponseBody().write(derp.getBytes());
                httpExchange.getResponseBody().close();
            } else {
                String derp =
                        "" +
                                getTop("Account Creation Failure", httpExchange.getRemoteAddress().toString()) +
                                "<center><font size = 6><b>"+
                                "<h1>Account creation failed!</b></font><br>" +
                                "<font size = 5>"+
                                "Sorry! Account "+name + " has already been created!<p>" +
                                "</font>"+
                                "<a href = '/'>Back to home</a></center>";


                httpExchange.sendResponseHeaders(200, derp.length());
                httpExchange.getResponseBody().write(derp.getBytes());
                httpExchange.getResponseBody().close();
            }
        }
    }

    static class Response3 implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            java.net.URI uri = httpExchange.getRequestURI();

            String[] spl = uri.toString().split("\\?");
            String[] parts = spl[1].split("\\&");

            String name = parts[0].split("\\=")[1];

            //06x
            String derp = "<html><body bgcolor = '#"+hexCodeOfName(name)+"'></html>";
            httpExchange.sendResponseHeaders(200, derp.length());
            httpExchange.getResponseBody().write(derp.getBytes());
            httpExchange.getResponseBody().close();

        }
    }

    static class NewsPage implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String derp =
                    "" +
                            getNewsTop("Megaton News") +
                            "Megaton Launcher released - 2014-10-17";


            httpExchange.sendResponseHeaders(200, derp.length());
            httpExchange.getResponseBody().write(derp.getBytes());
            httpExchange.getResponseBody().close();
        }
    }

    static class ServerListPage implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response =
                    "" +
                            getTop("Server List", httpExchange.getRemoteAddress().toString())
                            + "</center><font size = 6><b>>Server list:</b></font><p><font size = 4> " + LoginServer.getServerList();


            httpExchange.sendResponseHeaders(200, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
            httpExchange.getResponseBody().close();
        }
    }

    public static String hexCodeOfName(String name) {
        Color color = new Color(name.hashCode());

        Formatter f = new Formatter(new StringBuffer(""));
        f.format("%02x", color.getRed());
        f.format("%02x", color.getGreen());
        f.format("%02x", color.getBlue());
        return  f.toString();


    }

    public static boolean playerExists(String player) {
        if(TemporaryDatabase.getPass(player.toLowerCase()).equals("")) return false;
        return true;
    }

    public static String getTop(String title, String ip) {
        Person person = LoginManager.getUser(ip);
        String username = person.getUsername();

        String display = "";

        String login = "You are not logged in! <a href = '/login'>Login here!</a>";

        if(username.equalsIgnoreCase("Anonymous")) {
            display = login;
        } else {
            display = "Logged in as <a href = '/people.ijen?name=" + username +"'>"+username+"</a> - "+
                    "<a href = '/mymessages'>(" + person.getNumberOfMessages() + ") messages</a> - "+
            "<a href = '/logout'>Logout</a>";
        }

        return "<html>" +
                "<link href='http://fonts.googleapis.com/css?family=Iceland' rel='stylesheet' type='text/css'>" +
                "<title>"+title+"</title>" +

                "<body background = '"+assets+"/cobblestone2.png'><center>"+
                "<table table-format='fixed' width = 1100 bgcolor = '000000''><td>"+
                "<font color = 'white' align = 'right'>"+
                "<center><p><p><p><p>"+
                display + "</font><br>" +
                "<img src = '"+assets+"/mega.png'></td>" +
                "</table>" +

                "<table table-format='fixed' width = 1100 background='"+assets+"/cobblestone.png'><td><font face = 'Iceland'>"+
                "<center>" +

                "<font size = 4> <font face = 'Tahoma'>"+
                "<a href = '/'>Home</a> - " +
                "<a href = '"+assets+"/Megaton.exe'>Launcher (Windows)</a> - " +
                "<a href = '"+assets+"/Megaton.jar'>Launcher (Linux / Mac OS)</a>" +
                " - <a href = '"+assets+"/Server.zip'>Server Software</a>" +
                " - <a href = '/servers'>Server List</a>" +
                " - <a href = '/forum'>Forum</a>" +
                "</font>"+
                "<br>Official Server IP: dxvpn.phaeton.co</center><p>" +
                "<hr>";
    }

    public static String getNewsTop(String title) {
        return "<html>" +
                "<title>"+title+"</title>" +
                "<body background = '"+assets+"/cobblestone2.png'>" +
                "<center>"+
                "<table table-format='fixed' width = 500 bgcolor = '000000''><td>"+
                "<font align='right'>Currently logged in as: Anonymous</font>"+
                "<font color = 'ffffff'><center><p><p><p><p>"+
                "<img src = '"+assets+"/mega.png'></td>" +
                "</table><table table-format='fixed' width = 500 background='"+assets+"/cobblestone.png'><td><font face = 'Tahoma'><center>";
    }
}
