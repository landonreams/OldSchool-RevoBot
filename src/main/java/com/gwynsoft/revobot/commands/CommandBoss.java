package com.gwynsoft.revobot.commands;

import com.gwynsoft.revobot.data.entity.BossList;
import com.gwynsoft.revobot.data.entity.Monster;
import com.gwynsoft.revobot.data.items.RatedItem;
import com.gwynsoft.revobot.utils.TextUtil;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Landon on 12/19/2016.
 */
public enum CommandBoss implements ICommand {
    INSTANCE;

    public void invoke(MessageReceivedEvent event, String[] params) {
        String bossName = String.join(" ", params);

        StringBuilder message = new StringBuilder();

        if(bossName == null || bossName.equals("") || bossName.equalsIgnoreCase("list")) {
            message.append("Here's a list of bosses that you can ask about:");
            for(Monster boss : BossList.INSTANCE.getBosses()) {
                message.append(String.format("%n- %s", boss.getName()));
            }
            if(event.isFromType(ChannelType.PRIVATE)) {
                TextUtil.sendMessage(event, message.toString());
            } else {
                TextUtil.sendPublicAndPrivate(event, null, message.toString());
            }
        } else {
            Monster boss = BossList.INSTANCE.getBoss(bossName);
            if (boss == null) {
                message.append(String.format("Could not find boss %s.", bossName));
            } else {
                message.append(String.format("**%s**%n", boss.getName()));
                message.append(String.format("%s%n%nDrops:%n", boss.getWikiPage()));
                for (RatedItem item : boss.getDrops()) {
                    message.append(String.format("- %s%n", item.toString()));
                }
                if (boss.getRelatedItems().length > 0) {
                    message.append(String.format("%nRelated Items:%n"));
                    for (RatedItem item : boss.getRelatedItems()) {
                        message.append(String.format("- %s%n", item.item().toString()));
                    }
                }

            }
            TextUtil.sendMessage(event, message.toString());
        }
    }

    public String[] getAliases() {
        return new String[] { "boss" };
    }
}
