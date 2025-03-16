package com.rudemoc.convert.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.rudemoc.convert.config.BotConfig;

import lombok.AllArgsConstructor;

import java.io.InputStream; 
import java.io.FileOutputStream; 
import java.io.IOException;

@SuppressWarnings("deprecation")
@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig = new BotConfig();

    @Override
    public String getBotUsername() {
        return botConfig.botName;
    }

    @Override
    public String getBotToken() {
        return botConfig.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasDocument()) {
        	long chatId = update.getMessage().getChatId();
            // Get the file ID of the largest photo size
            String fileId = update.getMessage().getDocument().getFileId();
            
            try {
                // Get the file path
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(fileId);
                File file = execute(getFileMethod);

                // Download the file
                downloadFile(file.getFilePath(), "downloaded_image.jpg");
                System.out.println("Image downloaded successfully.");
                
                String inputPath = "downloaded_image.jpg";
                String outputPath = "output_image.png";
                
                // Create an instance of ImageConverter
                
                ImageConverter converter = new ImageConverter();
                
                // Convert the image
                converter.convertJPGtoPNG(inputPath, outputPath);
                
                sendImage(chatId, outputPath);
                
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    private void sendImage(long chatId, String imagePath) {
    	SendDocument sendPhoto = new SendDocument();
        sendPhoto.setChatId(chatId);
        sendPhoto.setDocument(new InputFile(new java.io.File(imagePath)));
        sendPhoto.setCaption("Фотка");

        try {
            execute(sendPhoto);
            System.out.println("Image sent successfully.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    private void downloadFile(String filePath, String destination) throws IOException {
        try (InputStream inputStream = new java.net.URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath).openStream();
             FileOutputStream outputStream = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}