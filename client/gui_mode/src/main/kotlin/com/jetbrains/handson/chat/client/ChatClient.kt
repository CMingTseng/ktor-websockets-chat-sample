package com.jetbrains.handson.chat.client

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import kotlinx.coroutines.*
import tw.gov.president.communication.CommunicationPack
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import tw.gov.president.communication.JoinChat

var author = "Defualt_Client"
suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            val receivedText = message.readText()
//            println(receivedText)
//            //FIXME fail Event Polymorphic serializer was not found for missing class discriminator ('null')
//            val event = Json{
//                ignoreUnknownKeys = true
//                isLenient = true
//                prettyPrint = true
//                allowStructuredMapKeys = true
//                encodeDefaults = true
//                classDiscriminator = "action"
//            }.decodeFromString<Event>(receivedText)
//            println("Show Client get received Text event : $event ")
            //FIXME !!!
            try {
                val cp = Json.decodeFromString<CommunicationPack>(receivedText)
                author = cp.author
                val text = cp.text
                println("$author say : $text")
            } catch (e: Exception) {
                try {
                    val jo = Json.decodeFromString<JoinChat>(receivedText)
                    //                    println("Show Client get JoinChat  : $jo  ")
                    author = jo.author
                    val previousMessages = jo.previousMessages
                    previousMessages.forEach {
                        println("$author say : ${it.text}")
                    }
                } catch (e: Exception) {

                }
            }
        }
    } catch (e: Exception) {
        println("Error while receiving: " + e.localizedMessage)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        val message = readLine() ?: ""
        if (message.equals("exit", true)) return
        try {
            val communicationpack = CommunicationPack(author, message)
            send(Json.encodeToString(communicationpack))
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            return
        }
    }
}

@KtorExperimentalAPI
fun main() {
    val client = HttpClient {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/chat") {
            val messageOutputRoutine = launch { outputMessages() }
            val userInputRoutine = launch { inputMessages() }
            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
    println("Connection closed. Goodbye!")
}