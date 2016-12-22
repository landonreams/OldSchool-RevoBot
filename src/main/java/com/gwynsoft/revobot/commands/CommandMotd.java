package com.gwynsoft.revobot.commands;

import com.gwynsoft.revobot.utils.CommandFileReader;
import com.gwynsoft.revobot.utils.TextUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/22/2016.
 */
public enum CommandMotd implements ICommand {
    INSTANCE;

    public String[] getAliases() {
        return new String[]{ "motd" };
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] params) {
        String directory = "docs/motd.txt";
        StringBuilder message = new StringBuilder();
        CommandFileReader.ReturnCode code = CommandFileReader.readToStringBuilder(message, directory);

        if(code == CommandFileReader.ReturnCode.FILE_NOT_FOUND) {
            message.append("Sorry, I couldn't find the message of the day. Let an admin know so they can fix it!");
        }

        if(!message.toString().equals("")) {
            TextUtil.sendMessage(event, message.toString());
        }
    }
}
