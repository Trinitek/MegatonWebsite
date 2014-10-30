package com.daexsys.megatonlogin;

import java.io.*;
import java.util.HashMap;

public class TemporaryDatabase {
//    private static HashMap<String, String> userToKey = new HashMap<String, String>();
//
//    static {
//        userToKey.put("Cactose", "goldenlight".hashCode() + "");
//        userToKey.put("Trin", "explosions".hashCode() + "");
//        userToKey.put("GCCSUS", "skrub".hashCode() + "");
//        userToKey.put("Eh", "kangaroos".hashCode() + "");
//    }
//
//    public static String getPass(String user) {
//        if(userToKey.containsKey(user)) {
//            return userToKey.get(user);
//        }
//
//        if(user.equalsIgnoreCase("shutdownsecretphrase")) {
//            System.exit(0);
//        }
//
//        return "pass".hashCode() + "";
//    }

    public static String getPass(String username) {
        username = username.toLowerCase();

        try {
            FileInputStream fileInputStream = new FileInputStream("passwds/"+username+".dat");
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);

            String str = dataInputStream.readLine();
            dataInputStream.close();
            return str;

        } catch (IOException e) {
//            e.printStackTrace();
        }

        return "";
    }

    public static void addPlayer(String username, String password) {
        username = username.toLowerCase();

        try {
            PrintStream printStream = new PrintStream(new FileOutputStream("passwds/"+username+".dat"));
            printStream.println(password);
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
