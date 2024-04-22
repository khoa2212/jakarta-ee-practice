package com.axonactive.dojo.demo_kafka;

import com.axonactive.dojo.base.config.KafkaConfig;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dto.EmployeeMessageDTO;
import com.axonactive.dojo.relative.dto.RelativeMessageDTO;
import com.axonactive.dojo.base.producer.KafkaService;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.mapper.EmployeeMapper;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.relative.dao.RelativeDAO;
import com.axonactive.dojo.relative.entity.Relative;
import com.axonactive.dojo.relative.mapper.RelativeMapper;
import com.axonactive.dojo.relative.message.RelativeMessage;
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

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private EmployeeMapper employeeMapper;

    public void demoKafka(Long id, String key) throws EntityNotFoundException {
        Employee employee = employeeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));
        EmployeeMessageDTO employeeMessageDTO = employeeMapper.toEmployeeMessageDTO(employee);

        KafkaProducer<String, EmployeeMessageDTO> producerEmployee = new KafkaProducer<>(KafkaService.getProducerProperties());
        ProducerRecord<String, EmployeeMessageDTO> recordEmployee = key == null
                ? new ProducerRecord<>(KafkaConfig.TOPIC, employeeMessageDTO)
                : new ProducerRecord<>(KafkaConfig.TOPIC, key, employeeMessageDTO);
        KafkaService.send(recordEmployee, producerEmployee);
        producerEmployee.flush();
        producerEmployee.close();

        Relative relative = relativeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(RelativeMessage.RELATIVE_NOT_FOUND));
        RelativeMessageDTO relativeMessageDTO = relativeMapper.toRelativeMessageDTO(relative);

        KafkaProducer<String, RelativeMessageDTO> producerRelative = new KafkaProducer<>(KafkaService.getProducerProperties());
        ProducerRecord<String, RelativeMessageDTO> recordRelative = key == null
                ? new ProducerRecord<>(KafkaConfig.TOPIC2, relativeMessageDTO)
                : new ProducerRecord<>(KafkaConfig.TOPIC2, key, relativeMessageDTO);
        KafkaService.send(recordRelative, producerRelative);
        producerRelative.flush();
        producerRelative.close();
    }
}
