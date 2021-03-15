package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotInlineQuery {
    private final InlineQuery inlineQuery;

    public BotInlineQuery(InlineQuery inlineQuery) {
        this.inlineQuery = inlineQuery;
    }

    public AnswerInlineQuery answer() {
        return sendInlineAnswer();
    }

    private AnswerInlineQuery sendInlineAnswer() {
        List<InlineQueryResult> results = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            InlineQueryResultArticle article = new InlineQueryResultArticle();
            InputTextMessageContent messageContent = new InputTextMessageContent();
            messageContent.setMessageText(i + ")" + inlineQuery.getQuery());
            article.setInputMessageContent(messageContent);
            article.setId(Integer.toString(i));
            article.setTitle(Integer.toString(i));
            article.setReplyMarkup(sendKeyboard());
            results.add(article);
        }

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(86400);
        answerInlineQuery.setResults(results);

        return answerInlineQuery;
    }

    private InlineKeyboardMarkup sendKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Тык");
        inlineKeyboardButton1.setCallbackData("Button \"Тык\" has been pressed");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
