package com.gwynsoft.revobot;

import com.gwynsoft.revobot.data.items.ItemDatabase;
import com.gwynsoft.revobot.handlers.CommandHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Landon on 12/18/2016.
 */
public class RevoBot {

    private static JDA jda = null;

    private static void log4j2Init() {

    }

    private static void setupJDA() {
        try {
            String config = new String(Files.readAllBytes(Paths.get("config/bot.json")));

            JSONObject configJson = new JSONObject(config);
            String  botToken = configJson.getString("botToken");
            String      game = configJson.getString("game");

            jda = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .setBulkDeleteSplittingEnabled(false)
                    .addListener(new CommandHandler())
                    .buildBlocking();

            jda.getPresence().setGame(Game.of(game));
        } catch (IOException e) {
            log( "Unable to locate config file.");
        } catch (InterruptedException e) {
            log( "Interrupted!");
        } catch (RateLimitedException e) {
            log( "Rate limited!");
        } catch (LoginException e) {
            log("Couldn't log in to bot account!");
        }
    }

    public static void log(String msg) {

    }

    public static void main(String[] args) {
        try {
            log4j2Init();
            setupJDA();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

