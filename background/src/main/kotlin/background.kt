import chrome.action.BadgeTextDetails
import chrome.browserAction.TabDetails
import chrome.runtime.InstalledDetails
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

    chrome.runtime.onInstalled.addListener<InstalledDetails> {
        val textDetails = BadgeTextDetails  {
            text = "OFF"
        }
        chrome.action.setBadgeText(textDetails)
    }

    chrome.action.onClicked.addListener<Tab> { tab ->
        println("====onClicked")
        if (tab.url?.startsWith(extensions) == true ||
            tab.url?.startsWith(webstore) == true){
            val nextState = chrome.action.getBadgeText((js("{}") as TabDetails).apply {
                tabId = tab.id
            }).then {
                println("====onClicked==$it")
                if( it == "ON") "OFF" else "ON"
            }.then {
                val textDetails = BadgeTextDetails {
                    tabId = tab.id
                    text = it
                }
                chrome.action.setBadgeText(textDetails)
            }
        }
    }

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