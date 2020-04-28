package club.pigplan.piggyplanner.common.infrastructure.config

import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Configuration
class GraphQLConfigurations {
    @Bean
    fun hooks() = CustomSchemaGeneratorHooks()

    class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
        override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
            UUID::class -> graphqlUUIDType
            else -> null
        }
    }
}

internal val graphqlUUIDType = GraphQLScalarType.newScalar()
        .name("UUID")
        .description("A type representing a formatted java.util.UUID")
        .coercing(UUIDCoercing)
        .build()

private object UUIDCoercing : Coercing<UUID, String> {
    override fun parseValue(input: Any?): UUID = UUID.fromString(
            serialize(
                    input
            )
    )

    override fun parseLiteral(input: Any?): UUID? {
        val uuidString = (input as? StringValue)?.value
        return UUID.fromString(uuidString)
    }

    override fun serialize(dataFetcherResult: Any?): String = dataFetcherResult.toString()
}