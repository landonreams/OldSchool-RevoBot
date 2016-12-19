package com.gwynsoft.revobot.data.players;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Landon on 12/18/2016.
 */
public enum GameMode {
    DEFAULT("", "Standard", '\0'),
    IRONMAN("_ironman", "Ironman", 'i'),
    ULTIMATE("_ultimate", "Ultimate Ironman", 'u'),
    HARDCORE("_hardcore_ironman", "Hardcore Ironman", 'h'),
    DEADMAN("_deadman", "Deadman", 'd'),
    SEASONAL("_seasonal", "Seasonal Deadman", 's');

    private final String urlFormat;
    private final String urlStub;
    private final String longName;
    private final char identifier;
    private static final String urlBase = "http://services.runescape.com/m=hiscore_oldschool%s/index_lite.ws?player=";

    GameMode(String url, String name, char identifier) {
        this.urlFormat = String.format(urlBase, url) + "%s";
        this.longName = name;
        this.identifier = identifier;
        this.urlStub = url;
    }

    @Override
    public String toString() {
        return longName;
    }

    public String shortName() {
        int space = longName.indexOf(' ');
        if(space > -1) {
            return longName.substring(0, space);
        } else {
            return longName;
        }
    }

    public String getUrlStub() {
        return urlStub;
    }

    public URL getUrl(String playerName) throws MalformedURLException {
        return new URL(String.format(urlFormat, playerName.replace(" ", "_")));
    }

    public static GameMode fromName(String name) {
        for(GameMode t : GameMode.values()) {
            if(t.identifier == Character.toLowerCase(name.charAt(1)) )
                return t;
        }
        return GameMode.DEFAULT;
    }
}

