import androidx.compose.runtime.*
import chrome.others.Options
import chrome.others.QueryInfo
import chrome.others.UpdateInfo
import chrome.others.UpdateProperties
import chrome.tabs.Tab
import com.example.template.data.Message
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Element
import org.w3c.dom.events.EventListener
import org.w3c.dom.url.URL

fun main() {
    groupTabsView()
}

/**
 * tab分组布局
 */
fun groupTabsView() {

    renderComposable("root") {

        sendMessageView()

        H1 { Text("Google Dev Docs") }
        Button({
            id("group_tabs")
            onClick {
                queryTabs{ tabs ->
                    // 分组
                    chrome.tabs.group(Options { tabIds = tabs.map { it.id!! }.toTypedArray()}) { group ->
                        chrome.tabGroups.update(group, chrome.tabGroups.UpdateProperties{ title = "DOCS" }, {})
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
        tabs.sortWith(compareBy({it.title}))
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