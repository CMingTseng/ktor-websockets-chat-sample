package tw.gov.president.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action")
data class UpdateParticipants(val participants: Set<String>) : Event(Action.PARTICIPANTS_UPDATE)