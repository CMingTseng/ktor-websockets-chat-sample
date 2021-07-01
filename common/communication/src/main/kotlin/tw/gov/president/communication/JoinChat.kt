package tw.gov.president.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action")
data class JoinChat(  val previousMessages: ArrayList<CommunicationPack>,
                      val author: String = "") : Event(Action.JOIN_CHAT)