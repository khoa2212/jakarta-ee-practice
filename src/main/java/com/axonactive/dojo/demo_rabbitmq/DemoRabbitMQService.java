package com.axonactive.dojo.demo_rabbitmq;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.producer.RabbitMQProducer;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.mapper.EmployeeMapper;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Stateless
public class DemoRabbitMQService {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private RabbitMQProducer rabbitMQProducer;

    private final ObjectMapper mapper = new ObjectMapper();

    public static final String JAVA_EE_MSG_KEY = "java.ee.general.com";
    public static final String JAVA_CORE_MSG_KEY = "java.core.general.com";
    public static final String DESIGN_PATTERN_MSG_KEY = "design-pattern.general.com";
    public static final String NOT_MATCHING_MSG_KEY = "java.collection.general.com.vn";

    public void demoRabbitMQ(long employeeId, String exchange) throws EntityNotFoundException, IOException, TimeoutException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        EmployeeDTO employeeDTO =  this.employeeMapper.toDTO(employee);
        mapper.findAndRegisterModules();
        String message = mapper.writeValueAsString(employeeDTO);

        switch (exchange) {
            case "fanout": {
                rabbitMQProducer.start(BuiltinExchangeType.FANOUT);
                rabbitMQProducer.send(message, "");
                rabbitMQProducer.closeChannel();
                break;
            }
            case "topic": {
                rabbitMQProducer.start(BuiltinExchangeType.TOPIC);
                rabbitMQProducer.send("[1] A new Java EE topic is published", JAVA_EE_MSG_KEY);
                rabbitMQProducer.send("[2] A new Java Core topic is published", JAVA_CORE_MSG_KEY);
                rabbitMQProducer.send("[3] A new Design Pattern topic is published", DESIGN_PATTERN_MSG_KEY);
                rabbitMQProducer.send("[4] Not matching any routing key", NOT_MATCHING_MSG_KEY);
                rabbitMQProducer.closeChannel();
                break;
            }
            default: {
                break;
            }
        }
    }
}
