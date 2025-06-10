package ownersApp.config;

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
import ownersApp.dto.OwnerRequest;
import ownersApp.dto.OwnerResponse;
import java.util.Map;
@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String OWNER_REQUESTS = "owner-requests";
    public static final String OWNER_RESPONSES = "owner-responses";

    @Bean
    public NewTopic ownerRequests() {
        return TopicBuilder.name(OWNER_REQUESTS).build();
    }

    @Bean
    public NewTopic ownerResponses() {
        return TopicBuilder.name(OWNER_RESPONSES).build();
    }

    @Bean
    public JsonSerializer<OwnerRequest> ownerRequestJsonSerializer() {
        JsonSerializer<OwnerRequest> ser = new JsonSerializer<>();
        ser.setAddTypeInfo(false);
        return ser;
    }

    @Bean
    public JsonSerializer<OwnerResponse> ownerResponseJsonSerializer() {
        JsonSerializer<OwnerResponse> ser = new JsonSerializer<>();
        ser.setAddTypeInfo(false);
        return ser;
    }



    @Bean
    public ProducerFactory<String, OwnerResponse> ownerResponseProducerFactory(
            KafkaProperties props,
            JsonSerializer<OwnerResponse> serializer) {
        Map<String, Object> cfg = props.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(
                cfg,
                new StringSerializer(),
                serializer
        );
    }

    @Bean
    public KafkaTemplate<String, OwnerResponse> ownerResponseKafkaTemplate(
            ProducerFactory<String, OwnerResponse> pf) {
        return new KafkaTemplate<>(pf);
    }


    @Bean
    public ConsumerFactory<String, OwnerRequest> ownerRequestConsumerFactory(
            KafkaProperties props) {
        JsonDeserializer<OwnerRequest> deserializer =
                new JsonDeserializer<>(OwnerRequest.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> cfg = props.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(
                cfg,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OwnerRequest>
    ownerKafkaListenerContainerFactory(
            ConsumerFactory<String, OwnerRequest> cf,
            KafkaTemplate<String, OwnerResponse> ownerResponseKafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OwnerRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setReplyTemplate(ownerResponseKafkaTemplate);
        return factory;
    }

}