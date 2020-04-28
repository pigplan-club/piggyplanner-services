package club.pigplan.piggyplanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PiggyPlannerServicesApplication

fun main(args: Array<String>) {
    runApplication<PiggyPlannerServicesApplication>(*args)
}
