package com.gwynsoft.revobot.commands;

import com.gwynsoft.revobot.data.items.ItemDatabase;
import com.gwynsoft.revobot.data.items.RSItem;
import com.gwynsoft.revobot.utils.TextUtil;
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
        String itemName = String.join(" ", params);

        RSItem item = ItemDatabase.INSTANCE.getItem(itemName);

        StringBuilder message = new StringBuilder();

        if(item != null) {
             if (!itemName.equalsIgnoreCase(item.getName())) {
                message.append(String.format("Could not find item **%s** - Assuming **%s**.%n%n", itemName, item.getName()));
            }
            if (item.isTradeable()) {
                message.append(String.format("**%s**: GE - %,d gp | HA - %,d gp", item.getName(), item.getGePrice(), item.getHighAlch()));
            } else {
                message.append(String.format("**%s**: GE - Untradeable |  HA - %,d gp", item.getName(), item.getHighAlch()));
            }
        } else {
            message.append(String.format("Could not find item **%s**.", itemName));
        }

        TextUtil.sendMessage(event, message.toString());
    }
}