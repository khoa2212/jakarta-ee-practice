package com.axonactive.dojo.relative.messagebroker;

import com.axonactive.dojo.base.messagebroker.BasicConsumeLoop;
import com.axonactive.dojo.relative.dto.RelativeMessageDTO;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Properties;

@Getter
public class RelativeConsumer extends BasicConsumeLoop<RelativeMessageDTO> {
    private int countMessage = 0;

    public RelativeConsumer(Properties config, List<String> topics) {
        super(config, topics);
    }

    @Override
    public void process(ConsumerRecord<String, RelativeMessageDTO> record) {
        System.out.println("Key: " + record.key());
        System.out.println("Full name: " + record.value().getFullName());

        this.countMessage += 1;
        if (countMessage == 5) {
            try {
                shutdown();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
