package com.gwynsoft.revobot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/18/2016.
 */
public enum CommandPrice implements ICommand {
    INSTANCE;

    public String[] getAliases() {
        return new String[] { "price", "ge" };
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] params) {

    }
}