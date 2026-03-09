package org.example.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic parkingEventsTopic(){
        return new NewTopic("parking-events", 3, (short) 1);
    }
}
