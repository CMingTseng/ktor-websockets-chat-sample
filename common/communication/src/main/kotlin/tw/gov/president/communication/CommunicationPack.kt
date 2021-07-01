package tw.gov.president.communication

import kotlinx.serialization.Serializable

@Serializable
data class CommunicationPack(
    val action: String,
    val data: String?//FIXME use Any ?
)