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
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                val welcome="You are connected! There are ${connections.count()} users here."
                val communicationpack = CommunicationPack(Action.SEND_MESSAGE.action_name, welcome)
                send(Json.encodeToString(communicationpack))
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    println("Show Server get received Text : $receivedText ")
                    val cp = Json.decodeFromString<CommunicationPack>(receivedText)
                    println("Show Server get message  : $cp ")
                    if (cp.action == Action.SEND_MESSAGE.action_name) {
                        val textWithUsername = "[${thisConnection.name}]: ${cp.data}"
                        connections.forEach {
                            val communicationpack =
                                CommunicationPack(Action.SEND_MESSAGE.action_name, textWithUsername)
                            it.session.send(Json.encodeToString(communicationpack))
                        }
                    } else {

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
