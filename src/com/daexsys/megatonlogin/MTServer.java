package com.daexsys.megatonlogin;

public class MTServer {
    private String ip;
    private int port;

    private int playersOnline;
    private int maxPlayers;

    private String serverName;

    private long lastPing;

    public MTServer(String serverName, String ip, String port, int playersOnline, int maxPlayers) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = Integer.parseInt(port);

        this.playersOnline = playersOnline;
        this.maxPlayers = maxPlayers;

        lastPing = System.currentTimeMillis();
    }

    public String getServerName() {
        return serverName;
    }

    public void ping(int playersOnline, int maxPlayers) {
        this.playersOnline = playersOnline;
        this.maxPlayers = maxPlayers;
        lastPing = System.currentTimeMillis();
    }

    public boolean isAlive() {
        System.out.println(System.currentTimeMillis());
        System.out.println(lastPing + 2000);
        System.out.println(lastPing);
        return !(System.currentTimeMillis() > lastPing + 2000);
    }

    public String getIP(){
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getPlayersOnline() {
        return playersOnline;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String toString() {
        return getServerName() + " - IP: " + getIP() + " - Online players: " + getPlayersOnline()+"/"+getMaxPlayers();
    }
}
