package club.pigplan.piggyplanner

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PiggyPlannerServicesApplicationTests {

    @Test
    fun contextLoads() {

        assertDoesNotThrow("Should not throw Exception") {
            PiggyPlannerServicesApplication()
        }
    }
}

internal class PiggyPlannerServicesApplicationKtTest {
    @Test
    fun mainLoads() {
        assertDoesNotThrow("Should not throw Exception") {
            main(arrayOf())
        }
    }
}