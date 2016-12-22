package com.gwynsoft.revobot.utils;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Landon on 12/18/2016.
 */
public class TextUtil {
    public static String getRawText(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    public static String getURLToText(String url) throws IOException {
        return IOUtils.toString(new URL(url), "UTF-8");
    }

    public static void sendMessage(MessageReceivedEvent event, String message) {
        MessageChannel channel = event.isFromType(ChannelType.PRIVATE) ?
                event.getPrivateChannel() :
                event.getChannel();
        channel.sendMessage(message).queue();
    }

    public static void sendPublicAndPrivate(MessageReceivedEvent event, String publicMessage, String privateMessage) {
        if(publicMessage != null && !publicMessage.equals("")) {
            MessageChannel pub = event.getChannel();
            pub.sendMessage(publicMessage).queue();
        }
        if(event.getAuthor().hasPrivateChannel()) {
            event.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage(privateMessage).queue();
            });
        } else {
            event.getAuthor().getPrivateChannel().sendMessage(privateMessage).queue();
        }
    }
}
