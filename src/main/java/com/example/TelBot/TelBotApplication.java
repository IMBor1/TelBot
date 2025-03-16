package com.example.TelBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelBotApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TelBotApplication.class, args);
		TelegramBot bot = context.getBean(TelegramBot.class);

		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot((LongPollingBot) bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
