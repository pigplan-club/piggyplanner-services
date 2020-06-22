package club.pigplan.piggyplanner.common.infrastructure.config

import com.mongodb.MongoClient
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.extensions.mongo.DefaultMongoTemplate
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.serialization.json.JacksonSerializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.function.BiFunction

@Configuration
class AxonConfig {

    @Bean
    fun tokenStore(client: MongoClient): TokenStore =
            MongoTokenStore.builder()
                    .mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(client).build())
                    .serializer(JacksonSerializer.builder().build())
                    .build()

    @Bean
    fun sagaStore(client: MongoClient): SagaStore<Any> =
            MongoSagaStore.builder()
                    .mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(client).build())
                    .serializer(JacksonSerializer.builder().build())
                    .build()

    @Bean
    @Primary
    fun configureEventBus(eventBus: EventBus): EventBus {
        eventBus.registerDispatchInterceptor(EventLoggingDispatchInterceptor())
        return eventBus
    }
}

private val logger = LoggerFactory.getLogger(EventLoggingDispatchInterceptor::class.java)

class EventLoggingDispatchInterceptor : MessageDispatchInterceptor<EventMessage<*>> {
    override fun handle(messages: List<EventMessage<*>>): BiFunction<Int, EventMessage<*>, EventMessage<*>> {
        return BiFunction { index: Int, event: EventMessage<*> ->
            logger.info("Event: index={} metadata={} identifier={} payloadType={} payload={}",
                    index,
                    event.metaData,
                    event.identifier,
                    event.payloadType,
                    event.payload
            )
            event
        }
    }
}