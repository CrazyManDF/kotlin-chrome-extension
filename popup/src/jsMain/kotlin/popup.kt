import androidx.compose.runtime.*
import com.example.template.data.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable("root") {
        var result by remember { mutableStateOf("") }

        Li {
            A {
                H3 { Text("Tab Title") }
                P { Text("Tab Pathname") }
            }
        }
        H1 { Text("Google Dev Docs") }
        Button {
            Text("Group Tabs")
        }
        Ul {  }
        H3 {
            Text("Hello Extensions")
        }
        Button({
            onClick {
                sendMessage {
                    result = "Message result: ${it.message}"
                }
            }
        }) {
            Text("Send message")
        }

        Text(result)
    }
}


@NoLiveLiterals
private fun sendMessage(onResponse: (Message) -> Unit) {
    val message = Message(action = "POPUP_CLICK", message = "Message from popup")
    chrome.runtime.sendMessage(
        message = Json.encodeToString(message),
        responseCallback = { response ->
            val responseMessage = Json.decodeFromString<Message>(response.toString())
            onResponse(responseMessage) // Message from background to popup
        }
    )
}