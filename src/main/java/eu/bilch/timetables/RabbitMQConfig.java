package eu.bilch.timetables;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "bahn.exchange";
    public static final String QUEUE_NAME = "bahn.fahrten";
    public static final String ROUTING_KEY = "fahrten";

    @Bean Exchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();
    }
}