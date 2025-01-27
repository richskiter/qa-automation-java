package com.tcs.edu;

import com.tcs.edu.decorator.TimestampMessageDecorator;
import com.tcs.edu.enums.Doubling;
import com.tcs.edu.enums.MessageOrder;
import com.tcs.edu.interfaces.MessageService;
import com.tcs.edu.printer.ConsolePrinter;
import com.tcs.edu.service.MessageServiceSaver;
import com.tcs.edu.interfaces.MessageServiceSaverInterface;
import com.tcs.edu.enums.Severity;
import com.tcs.edu.domain.Message;
import com.tcs.edu.service.OrderedDistinctedMessageService;

import java.util.Collection;
import java.util.UUID;


class Application {
    public static void main(String[] args) {
        MessageServiceSaverInterface serviceSaver = new MessageServiceSaver();

        Message message1 = new Message(Severity.MAJOR, "Message1");
        Message message2 = new Message(Severity.MINOR, "Message2");
        Message message3 = new Message(Severity.REGULAR, "Message3");

        final UUID key1 = serviceSaver.save(message1);
        final UUID key2 = serviceSaver.save(message2);
        final UUID key3 = serviceSaver.save(message3);

        System.out.println("\r\n\r\n Messages found by Id:");
        System.out.println(serviceSaver.findByPrimaryKey(key1));
        System.out.println(serviceSaver.findByPrimaryKey(key2));
        System.out.println(serviceSaver.findByPrimaryKey(key3));

        final Collection<Message> allMessages = serviceSaver.findAll();

        MessageService service = new OrderedDistinctedMessageService(
                new ConsolePrinter(),
                new TimestampMessageDecorator()
        );

        System.out.println("\r\n\r\n Print messages:");
        service.log(MessageOrder.DESC, Doubling.DISTINCT, message1, message2, message2);

        System.out.println("\r\n\r\n Print one saved message:");
        service.log(serviceSaver.findByPrimaryKey(key1));

        System.out.println("\r\n\r\n Print all saved message:");
        for (Message currentMessage : allMessages) {
            service.log(currentMessage);
        }

        System.out.println("\r\n\r\n Print messages with severity");
        for (Message currentMessage : serviceSaver.findBySeverity(Severity.MAJOR)) {
            service.log(currentMessage);
        }
    }
}