package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.Utils;
import ru.tayrinn.telegram.coopdance.models.CallbackData;
import ru.tayrinn.telegram.coopdance.models.Commands;
import ru.tayrinn.telegram.coopdance.models.Dance;
import ru.tayrinn.telegram.coopdance.models.Dances;

/**
 * Класс для обработки коллбеков - событий при нажатии на кнопки бота
 */
public class CallbackQueryHandler extends BotCommandsHandler<CallbackQuery> {

    private final Dances dances;
    private CallbackData callbackData; // данные, которые зашиваются в кнопку при создании
    private CallbackQuery callbackQuery;
    private String messageId;

    public CallbackQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory, Dances dances) {
        super(telegramCommandsExecutor, keyboardFactory);
        this.dances = dances;
    }

    @Override
    public void handle(CallbackQuery data) {
        callbackQuery = data;
        String callData = data.getData();
        messageId = data.getInlineMessageId();
        callbackData = Commands.parseCommand(callData);

        parseCommand();
    }

    private void parseCommand() {

        switch (callbackData.command) {
            case Commands.ADD_GIRL_AND_BOY:
            case Commands.ADD_BOY_AND_GIRL:
                String utf = Utils.convertToUtf8String(Commands.toJson(callbackData));
                telegramCommandsExecutor.sendChatMessage(callbackQuery.getChatInstance(), utf);
                sendInlineAnswer(utf, callbackQuery); break;
            case Commands.ADD_GIRL :
            case Commands.ADD_BOY :
                addSingleDancer();
                break;
        }
    }

    private void addSingleDancer() {
        Dance dance = dances.addDance(callbackData.command, messageId);
        dance.processCommand(callbackData.command, callbackQuery.getFrom());

        EditMessageText newMessage = new EditMessageText();
        newMessage.setInlineMessageId(messageId);
        newMessage.setReplyMarkup(keyboardFactory.createStarterKeyboard(dance.message));
        newMessage.setParseMode(ParseMode.HTML);
        newMessage.setText(dance.toString());

        telegramCommandsExecutor.send(newMessage);
    }

    private void sendInlineAnswer(String command, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setUrl("t.me/CoopDanceBot?start=" + command);

        telegramCommandsExecutor.send(answerCallbackQuery);
    }
}
