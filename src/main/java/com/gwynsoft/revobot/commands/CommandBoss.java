package com.gwynsoft.revobot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/19/2016.
 */
public enum CommandBoss implements ICommand {
    INTERFACE;

    public void invoke(MessageReceivedEvent event, String[] params) {

    }

    public String[] getAliases() {
        return new String[] { "boss" };
    }
}
