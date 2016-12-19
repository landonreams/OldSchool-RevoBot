package com.gwynsoft.revobot.commands;


import com.gwynsoft.revobot.data.players.GameMode;
import com.gwynsoft.revobot.data.players.Player;
import com.gwynsoft.revobot.handlers.CommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Landon on 12/18/2016.
 */
public enum CommandHiscore implements ICommand {
    INSTANCE;

    public String[] getAliases() {
        return new String[]{ "hiscores", "hiscore", "hs" };
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] params) {
        String message = "";
        boolean good = true;
        if(params.length > 4) {
            // Too many parameters.
            good = false;
            message = "Woah, slow down! Too many parameters - double check the help page and make sure your name is in **\"quotation marks\"** if it has spaces!";
        } else if(params != null && params.length > 0) {
            // Start out with no info.
            String playerName = null;
            GameMode gameMode = null;
            Player.PrintingFormat printFmt = Player.PrintingFormat.SIMPLE;
            boolean bVirtual = false;
            for (String param : params) {
                // Iterate through all the parameters
                if (param.charAt(0) == '-') {
                    // Is it an option, or a name?
                    if (param.length() < 2) {
                        // Parameter is too short
                        good = false;
                    } else if (param.charAt(1) == 'v') {
                        // Virtual levels instead of true levels
                        bVirtual = true;
                    } else if (param.charAt(1) == 'f') {
                        // Full output instead of lite output
                        printFmt = Player.PrintingFormat.DETAILED;
                    } else {
                        if (gameMode == null) {
                            // Haven't assigned the gamemode yet - can do so now.
                            gameMode = GameMode.fromName(param);
                        } else {
                            // Already picked a gamemode.
                            message = "You can only pick one gamemode, sadly.";
                            good = false;
                        }
                    }
                    if (!good) {
                        if (message == null)
                            // No idea what else went wrong.
                            message = String.format("Something's wrong with **\"%s\"**! Check the help page and make sure you typed it right.", param);
                        break;
                    }
                } else {
                    // Otherwise, it's gotta be a name.
                    if (playerName == null) {
                        // No name = good! Give 'im one.
                        String[] name = param.replace("_", " ").split(" ");
                        StringBuilder nameBuilder = new StringBuilder();
                        for(String s : name) {
                            nameBuilder.append(StringUtils.capitalize(s)).append(" ");
                        }
                        playerName = nameBuilder.toString().trim();
                    } else {
                        // Can't do that.
                        message = "Unexpected word! Make sure that you enclose your name in \"s if it has spaces in it!";
                        good = false;
                        break;
                    }
                }
            }
            if (good) {
                // Only go on to get the stats if it's good.
                if(gameMode == null) gameMode = GameMode.DEFAULT;
                Player user = Player.lookup(playerName, gameMode);
                if (user == null) {
                    message = String.format("Sorry, I couldn't find player \"%s\"!", playerName);
                } else {
                    message = user.toDisplay(printFmt, bVirtual);
                }
            }
        }
        CommandHandler.sendMessage(event, message);
    }
}
