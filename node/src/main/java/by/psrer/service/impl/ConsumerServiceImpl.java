package by.psrer.service.impl;

import by.psrer.service.CallbackService;
import by.psrer.service.ConsumerService;
import by.psrer.service.CommandService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static by.psrer.model.RabbitQueue.BUTTON_CALLBACK;
import static by.psrer.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Service
@Log4j
public final class ConsumerServiceImpl implements ConsumerService {
    private CommandService mainService;
    private CallbackService callbackService;

    public ConsumerServiceImpl(final CommandService mainService, final CallbackService callbackService) {
        this.mainService = mainService;
        this.callbackService = callbackService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(final Update update) {
        log.debug("NODE: Text message is received");
        mainService.handleCommand(update);
    }

    @Override
    @RabbitListener(queues = BUTTON_CALLBACK)
    public void consumeCallback(final CallbackQuery callbackQuery) {
        log.debug("NODE: Callback is received");
        callbackService.handleCallback(callbackQuery);
    }
}
