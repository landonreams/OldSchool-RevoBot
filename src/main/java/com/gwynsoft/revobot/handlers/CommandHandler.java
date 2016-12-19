package com.gwynsoft.revobot.handlers;

import com.gwynsoft.revobot.RevoBot;
import com.gwynsoft.revobot.commands.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Landon on 12/18/2016.
 */
public class CommandHandler extends ListenerAdapter {

    private final HashMap<String, ICommand> _COMMANDS;

    public CommandHandler() {
        _COMMANDS = new HashMap<>();

        reg(CommandHelp.INSTANCE);
        reg(CommandHiscore.INSTANCE);
        reg(CommandPrice.INSTANCE);
    }

    private void reg(ICommand command) {
        for(String s : command.getAliases()) {
            _COMMANDS.put(s,command);
        }
    }

    private ICommand getCommand(String key) {
        if (_COMMANDS.containsKey(key.toLowerCase())) {
            return _COMMANDS.get(key.toLowerCase());
        } else {
            return null;
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String msg = event.getMessage().getRawContent();
            boolean isPrivate = event.isFromType(ChannelType.PRIVATE);
            boolean isMention = msg.startsWith(event.getJDA().getSelfUser().getAsMention());
            if (isMention || isPrivate) {
                // Gross regex to split string
                String[] fullMessage = msg.toLowerCase().split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");

                int startAt = isMention ? 1 : 0;

                if (fullMessage.length > startAt) {
                    ICommand cmd = getCommand(fullMessage[startAt].toLowerCase());
                    if (cmd != null) {
                        String[] params = null;
                        try {
                            params = Arrays.copyOfRange(fullMessage, startAt + 1, fullMessage.length);
                        } finally {
                            RevoBot.log(String.format("Calling \"%s\" with args: %s ", fullMessage[startAt], Arrays.toString(params)));
                            cmd.invoke(event, params);
                        }
                    }
                } else if (fullMessage.length == 1) {
                    getCommand("help").invoke(event, null);
                }
            }
            if(isMention && isPrivate) {
                sendMessage(event, "\n\n*By the way, you don't need to @mention me in a DM! Just say the command.*");
            }
        }
    }

    public static void sendMessage(MessageReceivedEvent event, String message) {
        MessageChannel channel = event.isFromType(ChannelType.PRIVATE) ?
                event.getPrivateChannel() :
                event.getChannel();
        channel.sendMessage(message).queue();
    }
}