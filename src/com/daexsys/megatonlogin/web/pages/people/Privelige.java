package com.daexsys.megatonlogin.web.pages.people;

public enum Privelige {
    STANDARD("Registered user"),
    MOD("Moderator"),
    ADMIN("Site Administrator");

    private String title;

    private Privelige(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
