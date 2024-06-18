import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.template.data.Message
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Element
import org.w3c.dom.HTMLHeadingElement
import kotlin.math.round


fun main() {

    println("Content script initialized")

    countReadingTime()
    createSendMessage()
}


/**
 * 获取article标签中的文本，计算大概的阅读时间
 */
fun countReadingTime() {
    val article = document.querySelector("article")
    if (article != null){
        val text = article.textContent ?: ""
//        val  wordMatchRegExp = Regex("""[^\s]+""")
        val  wordMatchRegExp = Regex("""[\u4e00-\u9fa5]""")
        val words = wordMatchRegExp.findAll(text)
        val wordCount = words.count()
        println("==========1===$text")
        println("==========2===$wordCount")
        val readingTime = round( wordCount / 200f)

        val badge = document.createElement("p")
        badge.id = "badgeId"
        badge.classList.add("color-secondary-text", "type--caption")
        badge.textContent = "⏱️ $readingTime min read"

        val heading = article.querySelector("h2")
        val date = article.querySelector("time")?.parentNode as? Element

        (date ?: heading)?.insertAdjacentElement("afterend", badge)
    }
}

/**
 * 成发送消息
 */
fun createSendMessage() {

    val element = document.createElement("div")
    element.id = "rootDialog"

    document.body?.appendChild(element)

    renderComposable(element) {
        var result by remember { mutableStateOf("") }
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

private fun sendMessage(onResponse: (Message) -> Unit) {
    val message = Message(action = "CONTENT_SCRIPT_CLICK", message = "Message from content script")
    chrome.runtime.sendMessage(
        message = Json.encodeToString(message),
        responseCallback = { response ->
            val responseMessage = Json.decodeFromString<Message>(response.toString())
            onResponse(responseMessage)
        }
    )
}