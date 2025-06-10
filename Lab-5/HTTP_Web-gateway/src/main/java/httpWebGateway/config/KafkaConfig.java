package httpWebGateway.config;

import httpWebGateway.dto.OwnerRequest;
import httpWebGateway.dto.OwnerResponse;
import httpWebGateway.dto.PetRequest;
import httpWebGateway.dto.PetResponse;
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
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String PET_REQUESTS = "pet-requests";
    public static final String PET_RESPONSES = "pet-responses";
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
    public NewTopic petRequests() {
        return TopicBuilder.name(PET_REQUESTS).build();
    }

    @Bean
    public NewTopic petResponses() {
        return TopicBuilder.name(PET_RESPONSES).build();
    }

    @Bean
    public JsonSerializer<PetRequest> petRequestJsonSerializer() {
        JsonSerializer<PetRequest> ser = new JsonSerializer<>();
        ser.setAddTypeInfo(false);
        return ser;
    }

    @Bean
    public JsonDeserializer<PetResponse> petResponseJsonDeserializer() {
        JsonDeserializer<PetResponse> d = new JsonDeserializer<>(PetResponse.class);
        d.addTrustedPackages("*");
        d.ignoreTypeHeaders();
        return d;
    }

    @Bean
    public ProducerFactory<String, PetRequest> petRequestProducerFactory(
            KafkaProperties props,
            JsonSerializer<PetRequest> serializer) {
        Map<String, Object> cfg = props.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(cfg, new StringSerializer(), serializer);
    }

    @Bean
    public ConsumerFactory<String, PetResponse> petResponseConsumerFactory(
            KafkaProperties props,
            JsonDeserializer<PetResponse> deserializer) {
        Map<String, Object> cfg = props.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(cfg, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, PetResponse> petRepliesContainer(
            ConsumerFactory<String, PetResponse> cf) {
        ContainerProperties containerProps = new ContainerProperties(PET_RESPONSES);
        containerProps.setGroupId("pet-replies-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProps);
    }

    @Bean
    public ReplyingKafkaTemplate<String, PetRequest, PetResponse> petReplyingKafkaTemplate(
            ProducerFactory<String, PetRequest> pf,
            ConcurrentMessageListenerContainer<String, PetResponse> repliesContainer) {
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PetResponse>
    petKafkaListenerContainerFactory(
            ConsumerFactory<String, PetResponse> cf
    ) {
        ConcurrentKafkaListenerContainerFactory<String, PetResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }






    @Bean
    public JsonSerializer<OwnerRequest> ownerRequestJsonSerializer() {
        JsonSerializer<OwnerRequest> ser = new JsonSerializer<>();
        ser.setAddTypeInfo(false);
        return ser;
    }

    @Bean
    public JsonDeserializer<OwnerResponse> ownerResponseJsonDeserializer() {
        JsonDeserializer<OwnerResponse> d = new JsonDeserializer<>(OwnerResponse.class);
        d.addTrustedPackages("*");
        d.ignoreTypeHeaders();
        return d;
    }

    @Bean
    public ProducerFactory<String, OwnerRequest> ownerRequestProducerFactory(
            KafkaProperties props,
            JsonSerializer<OwnerRequest> serializer) {
        Map<String, Object> cfg = props.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(cfg, new StringSerializer(), serializer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, OwnerResponse> ownerRepliesContainer(
            ConsumerFactory<String, OwnerResponse> cf) {
        ContainerProperties containerProps = new ContainerProperties(OWNER_RESPONSES);
        containerProps.setGroupId("owner-replies-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProps);
    }

    @Bean
    public ReplyingKafkaTemplate<String, OwnerRequest, OwnerResponse> ownerReplyingKafkaTemplate(
            ProducerFactory<String, OwnerRequest> pf,
            ConcurrentMessageListenerContainer<String, OwnerResponse> repliesContainer) {
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }



    @Bean
    public ConsumerFactory<String, OwnerResponse> ownerRequestConsumerFactory(
            KafkaProperties props) {
        JsonDeserializer<OwnerResponse> deserializer =
                new JsonDeserializer<>(OwnerResponse.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> cfg = props.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(
                cfg,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OwnerResponse>
    ownerKafkaListenerContainerFactory(
            ConsumerFactory<String, OwnerResponse> cf
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OwnerResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }

}