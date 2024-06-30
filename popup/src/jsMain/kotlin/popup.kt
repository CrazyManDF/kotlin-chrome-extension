import androidx.compose.runtime.*
import chrome.others.Options
import chrome.others.QueryInfo
import chrome.others.UpdateInfo
import chrome.others.UpdateProperties
import chrome.tabs.Tab
import com.example.template.data.Message
import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Element
import org.w3c.dom.events.EventListener
import org.w3c.dom.url.URL


fun main() {

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = "AIzaSyBFzoI12kpLELfwajucg9QiYdiRDRIktRg"
    )

    val chat = generativeModel.startChat(
        listOf(content(role = "user") { text("请将以下文本翻译成中文。") })
    )

    groupTabsView(chat)
}

@Composable
fun setGemini(chat: Chat) {
// https://github.com/PatilShreyas/generative-ai-kmp?tab=readme-ov-file
    var textInputState by remember { mutableStateOf("请将以下文本翻译成中文") }
    var textResultState by remember { mutableStateOf("") }
    val messageList = remember { mutableStateListOf<String>() }

//    GlobalScope.launch {
//        val data = chrome.storage.local.get<String>("gemini_text")
//        textInputState = data
//    }

    TextArea {
        value(textInputState)
        onInput { input ->
            textInputState = input.value
        }
    }

    Button({
        onClick {
            messageList.add(textInputState)
            GlobalScope.launch {
                val msg = content(role = "user"){ text(textInputState)}
                textInputState = ""
                chat.sendMessageStream(msg).collect { chunk ->
                    println("AI 结果：${chunk.text}")
                    textResultState = chunk.text ?: ""
                    messageList.add(textResultState)
                }
//                chrome.storage.local.set("gemini_text", textInputState)
            }
        }
    }) {
        Text("发送")
    }

    for (msg in messageList) {
        H4 {
            Text(value = msg)
        }
    }
}

/**
 * tab分组布局
 */
fun groupTabsView(chat: Chat) {

    renderComposable("root") {

        setGemini(chat)

//        sendMessageView()

        H1 { Text("Google Dev Docs") }
        Button({
            id("group_tabs")
            onClick {
                queryTabs { tabs ->
                    // 分组
                    chrome.tabs.group(Options { tabIds = tabs.map { it.id!! }.toTypedArray() }) { group ->
                        chrome.tabGroups.update(group, chrome.tabGroups.UpdateProperties { title = "DOCS" }, {})
                    }
                }
            }
        }) {
            Text("Group Tabs")
        }
        Ul { }


    }

    renderComposable("li_template") {
        Li {
            A {
                H3(attrs = {
                    classes("title")
                    style { }
                }) {
                    Text("Tab Title")
                }
                P(attrs = { classes("pathname") }) {
                    Text("Tab Pathname")
                }
            }
        }
    }

    fillTabsData()

//    document.body!!.append.div {
//        h1 {
//            + "Welcome to Kotlin/JS!"
//        }
//        p {
//            +"Fancy joining this year's "
//            a("https://kotlinconf.com/") {
//                +"KotlinConf"
//            }
//            +"?"
//        }
//        textArea(
//            rows = "5",
//            cols = "30"
//        ) {
//            + "Fancy joining this year's Fancy joining this year'sFancy joining this year'sFancy joining this year'sFancy joining this year's"
//        }
//    }
}

private fun queryTabs(callback: (Array<Tab>) -> Unit) {
    chrome.tabs.query(QueryInfo {
        url = arrayOf(
            "https://developer.chrome.com/docs/webstore/*",
            "https://developer.chrome.com/docs/extensions/*"
        )
    }) { tabs ->
        callback(tabs)
    }
}

private fun fillTabsData() {
    queryTabs { tabs ->
        tabs.sortWith(compareBy({ it.title }))
        // 标签数据
        val template = document.getElementById("li_template")
        val elements = hashSetOf<Element>()
        for (tab in tabs) {

            println("标签${tab.id}：${tab.title}")
            println("标签${tab.id}：${tab.url}")
            val pathName = URL(tab.url!!).pathname

            val title = tab.title?.split("-")?.get(0)?.trim()
            val pathname = pathName.slice("/docs".length..<pathName.length)

            val element = template?.firstElementChild?.cloneNode(true) as Element
            element.querySelector(".title")?.textContent = title
            element.querySelector(".pathname")?.textContent = pathname
            element.querySelector("a")?.addEventListener("click", EventListener {
                chrome.tabs.update(tab.id, UpdateProperties { active = true }, {})
                chrome.windows.update(tab.windowId, UpdateInfo { focused = true }, {})
            })
            elements.add(element)
        }
        document.querySelector("ul")?.append(*(elements.toTypedArray()))
    }
}


/**
 * 测试发送消息布局
 */
@Composable
private fun sendMessageView() {

    var result by remember { mutableStateOf("") }

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

@NoLiveLiterals
private fun sendMessage(onResponse: (Message) -> Unit) {
    val message = Message(action = "POPUP_CLICK", message = "Message from popup")
    chrome.runtime.sendMessage(
        extensionId = null,
        message = Json.encodeToString(message),
        options = null,
        responseCallback = { response ->
            val responseMessage = Json.decodeFromString<Message>(response.toString())
            onResponse(responseMessage) // Message from background to popup
        }
    )
}