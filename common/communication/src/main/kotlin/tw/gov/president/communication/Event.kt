package tw.gov.president.communication

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

//Ref : https://stackoverflow.com/questions/64529032/deserialize-json-array-with-different-values-type-with-kotlinx-serialization-lib
//Ref : https://github.com/Kotlin/kotlinx.serialization/issues/699
//Ref : https://stackoverflow.com/questions/66690712/kotlinx-serialization-polymorphic-serializer-was-not-found-for-missing-class-di
//@Serializable(with = ModuleSerializer::class)
@Serializable
abstract class Event(val action: Action)

//object ModuleSerializer : JsonContentPolymorphicSerializer<Module>(Module::class) {
//    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Event> {
//        return when (element.jsonObject["action"]?.jsonPrimitive?.content) {
//            Action.JOIN_CHAT.action_name -> JoinChat.serializer()
//            Action.SEND_MESSAGE.action_name -> CommunicationPack.serializer()
//            Action.PARTICIPANTS_UPDATE.action_name -> UpdateParticipants.serializer()
//            else -> throw Exception("Unknown Module: key 'action' not found or does not matches any module type")
//        }
//    }
//}