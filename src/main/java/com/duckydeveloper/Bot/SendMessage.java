package com.duckydeveloper.Bot;

import com.duckydeveloper.DcMain;
import com.duckydeveloper.Globals;
import com.duckydeveloper.Utils.Webhook.webhookSender;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SendMessage extends ListenerAdapter {

    public static void sendPlayerMessage(String msg, String channelID, String player) {

        TextChannel channel = DcMain.jda.getTextChannelById(channelID);

        if (!Globals.isMcblocked()) {
            if (channel != null) {
                channel.retrieveWebhooks().queue((webhooks) -> {
                    for (Webhook webhook : webhooks) {
                        if (webhook.getName().equals(player)) {
                            webhookSender.sendToWebhook(webhook.getUrl(), msg);
                            return;
                        }
                    }

                    channel.createWebhook(player).queue((createdWebhook) -> {
                        webhookSender.sendToWebhook(createdWebhook.getUrl(), msg);
                    });
                });
            }
        }
    }
}
