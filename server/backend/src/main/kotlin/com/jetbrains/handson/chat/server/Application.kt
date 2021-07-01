package com.jetbrains.handson.chat.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.gov.president.communication.Action
import tw.gov.president.communication.CommunicationPack
import tw.gov.president.communication.Event
import tw.gov.president.communication.JoinChat
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
//        val connections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())
        val authors = Collections.synchronizedSet(LinkedHashSet<String>())
        val previousMessages: ArrayList<CommunicationPack> = ArrayList()
        webSocket("/chat") {
            println("New user coming !! wait auth !! ")
            val thisConnection = Connection(this)
            connections += thisConnection
            val system_welcome = "Chat_System"
            val author = getRandomString(4)
            println("Show Random author name $author")

            try {
                val welcome =
                    "You ($author) are connected! There are ${connections.count()} users here."
                val communicationpack = CommunicationPack(system_welcome, welcome)
                send(Json.encodeToString(communicationpack))
                authors.add(author)
                val joininfo = JoinChat(previousMessages, author)
                send(Json.encodeToString(joininfo))
////                connections.forEach { socket -> socket.outgoing.send(Frame.Text(Gson().toJson(UpdateParticipants(authors))))}
//                for (frame in incoming) {
//                    frame as? Frame.Text ?: continue
//                    val receivedText = frame.readText()
//                    //FIXME !!!
//                    try {
//                        val cp = Json.decodeFromString<CommunicationPack>(receivedText)
//                        val author =cp.author
//                        val text = cp.text
//                        println("$author say : $text")
//                    } catch (e: Exception) {
//                        try {
//                            val jo = Json.decodeFromString<JoinChat>(receivedText)
//                            //                    println("Show Client get JoinChat  : $jo  ")
//                            val author =jo.author
//                            val previousMessages  = jo.previousMessages
//                            previousMessages.forEach {
//                                println("$author say : ${it.text}")
//                            }
//                        } catch (e: Exception) {
//
//                        }
//                    }
////                    val cp = Json.decodeFromString<CommunicationPack>(receivedText)
////                    println("Show Server get message  : $cp ")
////                    if (cp.action == Action.SEND_MESSAGE.action_name) {
////                        val textWithUsername = "[${thisConnection.name}]: ${cp.data}"
////                        connections.forEach {
////                            val communicationpack =
////                                CommunicationPack(Action.SEND_MESSAGE.action_name, textWithUsername)
////                            it.session.send(Json.encodeToString(communicationpack))
////                        }
////                    } else {
////
////                    }
//                }
                while (true) {
                    val frame = incoming.receive()
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    try {
                        val cp = Json.decodeFromString<CommunicationPack>(receivedText)
                        previousMessages.add(cp)
                        val author = cp.author
                        val text = cp.text
                        println("$author say : $text")
                        connections.forEach {
                            val communicationpack = CommunicationPack(author, text)
                            it.session.send(Json.encodeToString(communicationpack))
                        }
                    } catch (e: Exception) {
                        try {
                            val jo = Json.decodeFromString<JoinChat>(receivedText)
                            //                    println("Show Client get JoinChat  : $jo  ")
                            val author = jo.author
                            val previousMessages = jo.previousMessages
                            previousMessages.forEach {
                                println("$author say : ${it.text}")
                            }
                        } catch (e: Exception) {

                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}

fun getRandomString(length: Int): String {
//        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
//        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val charset = ('a'..'z') + ('A'..'Z')
//        return (1..length)
//            .map { charset.random() }
//            .joinToString("")
    return List(length) { charset.random() }
        .joinToString("")
}
