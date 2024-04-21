package com.axonactive.dojo.demo_kafka;

import com.axonactive.dojo.base.config.KafkaConfig;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.kafka_serializer.relative.RelativeMessageDTO;
import com.axonactive.dojo.base.producer.KafkaMessageBroker;
import com.axonactive.dojo.relative.dao.RelativeDAO;
import com.axonactive.dojo.relative.entity.Relative;
import com.axonactive.dojo.relative.mapper.RelativeMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DemoKafkaService {
    @Inject
    private RelativeDAO relativeDAO;

    @Inject
    private RelativeMapper relativeMapper;

    public void demoKafka(Long id, String key) throws EntityNotFoundException {
        Relative relative = relativeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Relative not found"));
        RelativeMessageDTO relativeMessageDTO = relativeMapper.toRelativeMessageDTO(relative);

        KafkaProducer<String, RelativeMessageDTO> producer = new KafkaProducer<>(KafkaMessageBroker.getProducerProperties());
        ProducerRecord<String, RelativeMessageDTO> record = key == null
                ? new ProducerRecord<>(KafkaConfig.TOPIC, relativeMessageDTO)
                : new ProducerRecord<>(KafkaConfig.TOPIC, key, relativeMessageDTO);
        KafkaMessageBroker.send(record, producer);
        producer.flush();
        producer.close();
    }
}
