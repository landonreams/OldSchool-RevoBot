package com.gwynsoft.revobot.commands;


import com.gwynsoft.revobot.handlers.CommandHandler;
import com.gwynsoft.revobot.utils.CommandFileReader;
import com.gwynsoft.revobot.utils.TextUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/18/2016.
 */
public enum CommandHelp implements ICommand {
    INSTANCE;

    public String[] getAliases() {
        return new String[]{ "help", "h", "?" };
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] params) {
        StringBuilder directory = new StringBuilder().append("docs/help/");
        StringBuilder message = new StringBuilder();

        if(params == null || params.length == 0) {
            directory.append("default.txt");
        } else {
            directory.append(String.join("/", params).toLowerCase()).append(".txt");
        }

        CommandFileReader.ReturnCode code = CommandFileReader.readToStringBuilder(message, directory.toString());

        if(code == CommandFileReader.ReturnCode.FILE_NOT_FOUND) {
            message.append("Sorry, I couldn't find that help file. Are you sure you spelled it right?");
        }

        if(!message.toString().equals("")) {
            TextUtil.sendMessage(event, message.toString());
        }
    }
}