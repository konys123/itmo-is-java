package petsApp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import petsApp.dto.PetRequest;
import petsApp.dto.PetResponse;


import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String PET_REQUESTS = "pet-requests";
    public static final String PET_RESPONSES = "pet-responses";

    @Bean
    public NewTopic petRequests() {
        return TopicBuilder.name(PET_REQUESTS).build();
    }

    @Bean
    public NewTopic petResponses() {
        return TopicBuilder.name(PET_RESPONSES).build();
    }

    @Bean
    public JsonDeserializer<PetRequest> petRequestJsonDeserializer() {
        JsonDeserializer<PetRequest> d = new JsonDeserializer<>(PetRequest.class);
        d.addTrustedPackages("*");
        return d;
    }

    @Bean
    public JsonSerializer<PetResponse> petResponseJsonSerializer() {
        JsonSerializer<PetResponse> ser = new JsonSerializer<>();
        ser.setAddTypeInfo(false);
        return ser;
    }

    @Bean
    public KafkaTemplate<String, PetResponse> petResponseKafkaTemplate(
            ProducerFactory<String, PetResponse> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<String, PetResponse> petResponseProducerFactory(
            KafkaProperties props,
            JsonSerializer<PetResponse> serializer) {
        Map<String, Object> cfg = props.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(
                cfg,
                new StringSerializer(),
                serializer
        );
    }

    @Bean
    public ConsumerFactory<String, PetRequest> petRequestConsumerFactory(
            KafkaProperties props,
            JsonDeserializer<PetRequest> deserializer) {
        Map<String, Object> cfg = props.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(cfg, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PetRequest> petKafkaListenerContainerFactory(
            ConsumerFactory<String, PetRequest> cf,
            KafkaTemplate<String, PetResponse> petResponseKafkaTemplate
            ) {
        ConcurrentKafkaListenerContainerFactory<String, PetRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setReplyTemplate(petResponseKafkaTemplate);
        return factory;
    }
}