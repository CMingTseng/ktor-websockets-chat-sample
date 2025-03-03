package tw.gov.president.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action")
data class CommunicationPack(
     val author: String = "",val text: String = ""
):Event(Action.SEND_MESSAGE)