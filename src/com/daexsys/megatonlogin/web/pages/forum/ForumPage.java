package com.daexsys.megatonlogin.web.pages.forum;

import com.daexsys.megatonlogin.web.pages.people.Person;
import com.daexsys.megatonlogin.web.pages.people.Privelige;
import com.daexsys.megatonlogin.web.util.LoginManager;
import com.daexsys.megatonlogin.web.WebServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumPage implements HttpHandler {
    public static Map<String, Thread> forum = new HashMap<String, Thread>();
    public static List<Thread> threads = new ArrayList<Thread>();

    public ForumPage() {
        load();
    }

    public static void addThread(Thread thread, Post op) {
        forum.put(thread.getName().replaceAll(" ", "%20"), thread);
        threads.add(thread);
        thread.addPost(op);
    }

    public static void addThread(Thread thread) {
        forum.put(thread.getName().replaceAll(" ", "%20"), thread);
        threads.add(thread);
    }

    public static void organizeThreads() {
        List<Thread> stickyThreads = new ArrayList<Thread>();

        List<Thread> organized = new ArrayList<Thread>();

        for(Thread thread : threads) {
            if(thread.isStickied()) {
                stickyThreads.add(thread);
            }
        }

        while(threads.size() - 1 > 0) {
            long newestTime = 0;
            Thread newestThread = null;

            for(Thread thread : threads) {
                if(thread.getLastPost().getTimePosted() > newestTime) {
                    newestTime = thread.getLastPost().getTimePosted();
                    newestThread = thread;
                }
            }

            threads.remove(newestThread);
            organized.add(newestThread);
        }

        for(Thread thread : stickyThreads) {
            threads.remove(thread);
        }

        stickyThreads.addAll(organized);
        threads = stickyThreads;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        java.net.URI uri = httpExchange.getRequestURI();

        String[] spl = uri.toString().split("\\?");

        String response = "";

        Person person = LoginManager.getUser(httpExchange.getRemoteAddress().toString());

        boolean isMod = false;

        if(person.getPrivilage() == Privelige.ADMIN || person.getPrivilage() == Privelige.MOD) {
            isMod = true;
        }

        if(uri.toString().equalsIgnoreCase("/forum")) {
            response += WebServer.getTop("Forum", httpExchange.getRemoteAddress().toString()) + "</center>" +
                    "<b><font size = '6'>General Discussion Forum:</font></b><br>" +
//                    "<a href='/'>Back to home</a><br>" +
                    "Because there has to be somewhere where anything goes.<hr>";

            for(Thread thread : threads) {
                response += thread.format();
            }

            response +=
                            getPostingMessage(httpExchange.getRemoteAddress().toString()) + "<p>";

            String username = LoginManager.getUsername(httpExchange.getRemoteAddress().toString());
            if(!username.equalsIgnoreCase("Anonymous")) {
                response += "<form action='makethread.ijen'>" +
                        "Title:<input type = 'text' name='title' style = 'width:500px;' ><br>" +
                        "Text:<textarea name='body' style = 'width:500px; height:150px'></textarea><br>" +
                        "<input type = 'submit' value='Create Thread'>" +
                        "</form>";
            }
        } else {
            String[] parts = spl[1].split("\\=");

            String threadName = parts[1];

            Thread thread = forum.get(threadName);

            if (thread != null) {
                response += WebServer.getTop("Forum", httpExchange.getRemoteAddress().toString()) + "</center>" +
                        "<b><font size = '5'>" + thread.getName() + "</font></b>" +
                        (thread.isStickied() ? " - <b>STICKIED</b>" : "")
                        + (thread.isLocked() ? " - <b>LOCKED</b>" : "") +

                        "<br><a href='/forum'>Back to forum</a>";

                if(isMod) {
                    response += "- <a href='/stickythread?name=" + thread.getName() + "'>Toggle stickied</a>" +
                            "- <a href='/lockthread?name=" + thread.getName() + "'>Toggle lock</a>";
                }
                        response += "<hr>";

                for (Post post : thread.getPosts()) {
                    response += post.format() + "<hr>";
                }

                System.out.println(uri);

                String val = uri.toString().split("\\=")[1];
                System.out.println(val);

                String username = LoginManager.getUsername(httpExchange.getRemoteAddress().toString());

                response +=
                        getPostingMessage(httpExchange.getRemoteAddress().toString()) + "<p>";
                if(!username.equalsIgnoreCase("Anonymous")) {
                    response +=

                                    "<form action='makepost.ijen'>" +
                                    "Text:<br><textarea style = 'width:500px; height:150px' name='text'></textarea><br>" +
                                    "<input type = 'hidden' name = 'loc' value = " + val + ">" +
                                    "<input type = 'submit' value='Post'>" +
                                    "</form>";
                }
            } else {
                response += WebServer.getTop("Thread Not Found", httpExchange.getRemoteAddress().toString()) + "The thread '" + threadName + "' was not found! Oh noes! <a href='/forum'>Back home</a>";
            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }

    public String getPostingMessage(String url) {
        String username = LoginManager.getUsername(url);

        if(username.equalsIgnoreCase("Anonymous")) {
            return "You are not logged in, and therefore cannot post.";
        } else {
            return "<font size = '5'>Post as " + username+"</font>";
        }
    }

    public static void save() {
        Gson gson = new Gson();

        File file = new File("threads/");
        file.mkdirs();

        for(Thread thread : threads) {
            String json = gson.toJson(thread);
            File threadFile = new File("threads/"+thread.getName());

            try {
                threadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                PrintStream printStream = new PrintStream(new FileOutputStream(threadFile));
                printStream.println(json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load() {
        Gson gson = new Gson();

        File file = new File("threads/");
        file.mkdirs();

        for(File loadedFile : file.listFiles()) {
            String loaded = "";

            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(loadedFile));
                loaded = dataInputStream.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread thread = gson.fromJson(loaded, Thread.class);
            addThread(thread);
        }
    }
}
