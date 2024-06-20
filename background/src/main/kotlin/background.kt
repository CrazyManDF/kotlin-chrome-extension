import chrome.action.BadgeTextDetails
import chrome.action.setBadgeText
import chrome.browserAction.TabDetails
import chrome.omnibox.DefaultSuggestResult
import chrome.omnibox.SuggestResult
import chrome.runtime.InstalledDetails
import chrome.scripting.CSSInjection
import chrome.scripting.InjectionTarget
import chrome.tabs.CreateProperties
import chrome.tabs.Tab
import com.example.template.data.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun BadgeTextDetails(block: BadgeTextDetails.() -> Unit) =
    (js("{}") as BadgeTextDetails).apply(block)

const val extensions = "https://developer.chrome.com/docs/extensions"
const val webstore = "https://developer.chrome.com/docs/webstore"

const val URL_CHROME_EXTENSIONS_DOC = "https://developer.chrome.com/docs/extensions/reference/api/"
const val NUMBER_OF_PREVIOUS_SEARCHES = 4

fun main() {
    println("====Background script initialized")
    setBadgeInfo()

    sendResponseMessage()

    displaySuggestions()
}

fun displaySuggestions() {
    chrome.omnibox.onInputChanged.addListener { text, suggest ->
        chrome.omnibox.setDefaultSuggestion((js("{}") as DefaultSuggestResult).apply {
            description = "Enter a Chrome API or choose from past searches"
        }, {})

        chrome.storage.local.get("apiSuggestions"){ apiSuggestions ->
            val array = apiSuggestions["apiSuggestions"] as Array<String>
            val data = array.map { api ->
                (js("{}") as SuggestResult).apply{
                    content = api
                    description = "Open chrome.${api} API"
                }
            }.toTypedArray()
            suggest(data)
        }
    }
    chrome.omnibox.onInputEntered.addListener({ input, disposition ->
        chrome.tabs.create((js("{}") as CreateProperties).apply {
            url = URL_CHROME_EXTENSIONS_DOC + input
        }, {})
        // 保存关键词
        updateHistory(input)
    })
}

fun updateHistory(input: String) {
    chrome.storage.local.get("apiSuggestions"){ apiSuggestions ->
        val array = apiSuggestions["apiSuggestions"] as Array<String>
        val newArray = array.toMutableList().apply {
            add(0, input)
        }.slice(0..< NUMBER_OF_PREVIOUS_SEARCHES).toTypedArray()
        val data = "apiSuggestions" to newArray
        chrome.storage.local.set(kotlin.js.json(data), {})
    }
}

fun setBadgeInfo() {
    chrome.runtime.onInstalled.addListener<InstalledDetails> {
        // 将默认建议保存到存储空间
        if (it.reason == "install"){
            val data = Pair("apiSuggestions", arrayOf("tabs", "storage", "scripting"))
            chrome.storage.local.set(kotlin.js.json(data), {})
        }
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