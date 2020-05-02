package club.pigplan.piggyplanner.account.infrastructure.config

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val ACCOUNT_SNAPSHOT_THRESHOLD = 300

@Configuration
class AccountConfigurations {

    @Bean
    fun accountSnapshotTriggerDefinition(snapshotter: Snapshotter) =
            EventCountSnapshotTriggerDefinition(snapshotter, ACCOUNT_SNAPSHOT_THRESHOLD)
}
