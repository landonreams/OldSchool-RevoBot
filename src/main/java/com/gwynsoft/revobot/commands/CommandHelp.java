package com.gwynsoft.revobot.commands;


import com.gwynsoft.revobot.handlers.CommandHandler;
import com.gwynsoft.revobot.utils.TextReader;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/18/2016.
 */
public enum CommandHelp implements ICommand {
    INSTANCE;

    public String[] getAliases() {
        return new String[]{ "help", "h", "motd" };
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] params) {
        String helpString = null;

        StringBuilder directory = new StringBuilder();

        directory.append("help/");
        if(event.getMessage().getContent().toLowerCase().contains("motd")) {
            directory.append("motd.txt");
        } else if(params == null || params.length == 0) {
            directory.append("default.txt");
        } else {
            directory.append(String.join("/", params).toLowerCase()).append(".txt");
        }
        try {
            helpString = TextReader.getRawText(directory.toString());
        } catch (Exception e) {
            helpString = "Sorry, I couldn't find that help page. Are you sure you spelled it right?";
        } finally {
            if(helpString != null) {
                CommandHandler.sendMessage(event, helpString);
            }
        }
    }
}