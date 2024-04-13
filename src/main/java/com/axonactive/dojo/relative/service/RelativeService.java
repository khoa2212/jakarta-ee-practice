package com.axonactive.dojo.relative.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.messagebroker.KafkaMessageBroker;
import com.axonactive.dojo.base.messagebroker.MessageBrokerConfig;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.relative.dao.RelativeDAO;
import com.axonactive.dojo.relative.dto.RelativeDTO;
import com.axonactive.dojo.relative.dto.RelativeListResponseDTO;
import com.axonactive.dojo.relative.entity.Relative;
import com.axonactive.dojo.relative.mapper.RelativeMapper;
import com.axonactive.dojo.relative.rest.RelativeResource;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Stateless
public class RelativeService {

    private static final Logger logger = LogManager.getLogger(RelativeResource.class);

    @Inject
    private RelativeDAO relativeDAO;

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private RelativeMapper relativeMapper;

    @Inject
    private KafkaMessageBroker kafkaMessageBroker;

    public RelativeListResponseDTO findRelativesByEmployeeId(long employeeId, int pageNumber, int pageSize) throws EntityNotFoundException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        List<Relative> relatives = this.relativeDAO.findRelativesByEmployeeId(employee.getId(), offset, pageSize);
        long totalCount = this.relativeDAO.findTotalCount(employee.getId());

        return RelativeListResponseDTO
                .builder()
                .relatives(this.relativeMapper.toListDTO(relatives))
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }

    public RelativeListResponseDTO findRelivesByEmployeesNotAssigned(int pageNumber, int pageSize) {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        List<RelativeDTO> relativeDTOS = relativeMapper.toListDTO(relativeDAO.findRelivesByEmployeesNotAssigned(offset, pageSize));
        long totalCount = relativeDAO.findTotalCountRelivesByEmployeesNotAssigned();

        return RelativeListResponseDTO
                .builder()
                .relatives(relativeDTOS)
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }

    public void consumeMessage() {
        KafkaConsumer<String, String> consumer = kafkaMessageBroker.getConsumer();
        consumer.subscribe(Collections.singleton(MessageBrokerConfig.TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                System.out.println("Receive new metadata \n" +
                        "Topic: " + record.topic() + "\n" +
                        "Key: " + record.key() + "\n" +
                        "Value: " + record.value() + "\n" +
                        "Partition: " + record.partition() + "\n" +
                        "Offset: " + record.offset() + "\n" +
                        "Timestamp: " + record.timestamp());
            });
        }
    }
}
