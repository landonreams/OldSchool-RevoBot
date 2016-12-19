package com.gwynsoft.revobot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/18/2016.
 */
public interface ICommand {

    void invoke(MessageReceivedEvent event, String[] params);
    String[] getAliases();
}
