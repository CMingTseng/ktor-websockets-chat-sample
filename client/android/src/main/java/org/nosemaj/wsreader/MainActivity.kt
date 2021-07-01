package org.nosemaj.wsreader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

import org.json.JSONObject
import org.nosemaj.wsreader.databinding.ActivityMainBinding
import tw.gov.president.communication.Action
import tw.gov.president.communication.CommunicationPack
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var commands: Flow<String>
    private lateinit var statusWindow: TextView
    private var lineNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        commands = binding.button.clicks()
            .map { binding.command.text.toString() }
        statusWindow = binding.status
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        connect()
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.cancel("Pausing activity")
    }

    private fun connect() {
//        val url = Url(getString(R.string.websocket_url))
        val url = Url(BuildConfig.websocket_url)
        val client = HttpClient(OkHttp) { install(WebSockets) }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                connect(client, url)
            } catch (e: Throwable) {
                addStatus(R.string.websocket_failure, e.message!!)
            }
        }
    }

    private suspend fun connect(ktor: HttpClient, u: Url) {
        ktor.webSocket(
            method = HttpMethod.Get,
            host = "192.168.43.74",
            port = 8080,
            path = "/chat"
        ) {
            addStatus(R.string.websocket_connected, u.toString())
            awaitAll(async {
                commands
                    .onEach { addStatus(R.string.websocket_sent_command, it) }
                    .map { command ->
                        val communicationpack =
                            CommunicationPack(Action.SEND_MESSAGE.action_name, command)
                        kotlinx.serialization.json.Json.encodeToString(communicationpack)
                    }
                    .collect {
                        outgoing.send(Frame.Text(it))
                    }
            }, async {
                incoming.consumeEach {
                    when (it) {
                        is Frame.Text ->{
                            val receivedText =it.readText()
                            val cp = kotlinx.serialization.json.Json.decodeFromString<CommunicationPack>(receivedText)
                            if (cp.action == Action.SEND_MESSAGE.action_name) {
                                cp.data?.let { info ->
                                    addStatus(R.string.websocket_received_message, info)
                                }
                            } else {

                            }
                        }
                        is Frame.Close -> {
                            addStatus(R.string.websocket_closed, closeReason.await()!!.message)
                        }
                        else -> {
                        }
                    }
                }
            })
        }
        //FIXME Get error Handshake failed !!
//        ktor.wss(Get, u.host, u.port, u.encodedPath) {
//            addStatus(R.string.websocket_connected, u.toString())
//            awaitAll(async {
//                commands
//                    .onEach { addStatus(R.string.websocket_sent_command, it) }
//                    .map { command ->
////                        JSONObject()
////                            .put("action", "sendmessage")
////                            .put("data", command)
////                            .toString()
//                        val communicationpack =
//                            CommunicationPack(Action.SEND_MESSAGE.action_name, command)
//                        kotlinx.serialization.json.Json.encodeToString(communicationpack)
//                    }
//                    .collect {
//                        outgoing.send(Frame.Text(it))
//                    }
//            }, async {
//                incoming.consumeEach {
//                    when (it) {
//                        is Frame.Text ->
//                            addStatus(R.string.websocket_received_message, it.readText())
//                        is Frame.Close -> {
//                            Log.e("ktor.wss", "Show weqrqewr ${closeReason.await()!!.message}")
//                            addStatus(R.string.websocket_closed, closeReason.await()!!.message)
//                        }
//
//                        else -> {
//                        }
//                    }
//                }
//            })
//        }
    }

    private fun addStatus(@StringRes resId: Int, arg: String) {
        val status = getString(resId, arg)
        val line = getString(R.string.status_line, lineNumber, status)
        statusWindow.append(line)
        lineNumber++
    }

    private fun View.clicks() = callbackFlow {
        setOnClickListener {
            offer(Unit)
        }
        awaitClose { setOnClickListener(null) }
    }
}
