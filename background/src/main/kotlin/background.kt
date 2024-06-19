import chrome.action.BadgeTextDetails
import chrome.action.setBadgeText
import chrome.browserAction.TabDetails
import chrome.runtime.InstalledDetails
import chrome.scripting.CSSInjection
import chrome.scripting.InjectionTarget
import chrome.tabs.Tab
import com.example.template.data.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun BadgeTextDetails(block: BadgeTextDetails.() -> Unit) =
    (js("{}") as BadgeTextDetails).apply(block)

const val extensions = "https://developer.chrome.com/docs/extensions"
const val webstore = "https://developer.chrome.com/docs/webstore"

fun main() {
    println("Background script initialized")

    setBadgeInfo()

    sendResponseMessage()

}

fun setBadgeInfo() {
    chrome.runtime.onInstalled.addListener<InstalledDetails> {
        val textDetails = BadgeTextDetails  {
            text = "OFF"
        }
        setBadgeText(textDetails)
    }
    chrome.action.onClicked.addListener<Tab> { tab ->
        // 用这个网址测试 https://developer.chrome.com/docs/webstore/publish

        if (tab.url?.startsWith(extensions) == true ||
            tab.url?.startsWith(webstore) == true){
            // 更改标签
            chrome.action.getBadgeText((js("{}") as TabDetails).apply {
                tabId = tab.id
            }).then {
                if( it == "ON") "OFF" else "ON"
            }.then {
                val textDetails = BadgeTextDetails {
                    tabId = tab.id
                    text = it
                }
                setBadgeText(textDetails)

                // 添加或移除样式表
                when (it) {
                    "ON" -> {
                        chrome.scripting.insertCSS((js("{}") as CSSInjection).apply {
                            files = arrayOf("focus-mode.css")
                            target = (js("{}") as InjectionTarget).apply { tabId = tab.id }
                        }, {})
                    }
                    "OFF" -> {
                        chrome.scripting.removeCSS((js("{}") as CSSInjection).apply {
                            files = arrayOf("focus-mode.css")
                            target = (js("{}") as InjectionTarget).apply { tabId = tab.id }
                        }, {})
                    }
                    else -> {

                    }
                }
            }
        }
    }
}

/**
 * 返回消息
 */
fun sendResponseMessage() {
    chrome.runtime.onMessage.addListener { request, sender, sendResponse ->
        println("Background receives a message: ${request}")
        val message = Json.decodeFromString<Message>(request.toString())
        val response = when (message.action) {
            "POPUP_CLICK" -> Message(action = "BG_RESPONSE", message =  "Message from background to popup")
            "CONTENT_SCRIPT_CLICK" -> Message(action = "BG_RESPONSE", message =  "Message from background to content script")
            else -> Message(action = "ERROR", message = "Action not handled")
        }
        sendResponse(Json.encodeToString(response))
    }
}