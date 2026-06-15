package com.gauravd70.ecommerce.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitMQConfigurations {
    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_ACTIONS_QUEUE = "product.actions.queue";
    public static final String PRODUCT_ACTIONS_ROUTING_KEY = "product.actions.routing.key";

    @Bean
    public DirectExchange getExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE, true, false);
    }

    @Bean(PRODUCT_ACTIONS_QUEUE)
    public Queue getQueue() {
        return QueueBuilder.durable(PRODUCT_ACTIONS_QUEUE).build();
    }

    @Bean
    public Binding getActionsQueueBinding(DirectExchange exchange, @Qualifier(PRODUCT_ACTIONS_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(PRODUCT_ACTIONS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter getMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
