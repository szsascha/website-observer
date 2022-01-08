package com.github.szsascha.websiteobserver.notification;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TelegramNotificationService implements NotificationService {

    private final TelegramBot bot;

    private final String chatId;

    public TelegramNotificationService(
            @Value("${notification.telegram.bot.token}") String botToken,
            @Value("${notification.telegram.bot.chatId}") String chatId
    ) {
        this.bot = new TelegramBot(botToken);
        this.chatId = chatId;
    }

    @Override
    public void notifyWithMessage(String message) {
        final SendResponse response = bot.execute(new SendMessage(Long.valueOf(chatId), message));
        if (!response.isOk()) {
            log.error("Couldn't send notification. Error: {} {}", response.errorCode(), response.description());
        }
    }

}
