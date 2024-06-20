import chrome.action.setBadgeText
import chrome.others.*
import chrome.runtime.InstalledDetails
import chrome.storage.get
import chrome.storage.set
import chrome.tabs.Tab
import com.example.template.data.Message
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
        chrome.omnibox.setDefaultSuggestion(DefaultSuggestResult{
            description = "Enter a Chrome API or choose from past searches"
        }, {})

        GlobalScope.launch {
            val array = chrome.storage.local.get<Array<String>>("apiSuggestions")
            val data = array.map { api ->
                SuggestResult{
                    content = api
                    description = "Open chrome.${api} API"
                }
            }.toTypedArray()
            suggest(data)
        }
    }
    chrome.omnibox.onInputEntered.addListener({ input, disposition ->
        chrome.tabs.create(CreateProperties {
            url = URL_CHROME_EXTENSIONS_DOC + input
        }, {})
        // 保存关键词
        updateHistory(input)
    })
}

fun updateHistory(input: String) = GlobalScope.launch{
    val array = chrome.storage.local.get<Array<String>>("apiSuggestions")
        val newArray = array.toMutableList().apply {
            add(0, input)
        }.slice(0..< NUMBER_OF_PREVIOUS_SEARCHES).toTypedArray()
        chrome.storage.local.set("apiSuggestions", newArray)
}

fun setBadgeInfo() {
    chrome.runtime.onInstalled.addListener<InstalledDetails> {
        // 将默认建议保存到存储空间
        if (it.reason == "install"){
            GlobalScope.launch {
                chrome.storage.local.set("apiSuggestions", arrayOf("tabs", "storage", "scripting"))
            }
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
            chrome.action.getBadgeText(TabDetails {
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
                        chrome.scripting.insertCSS(CSSInjection {
                            files = arrayOf("focus-mode.css")
                            target = InjectionTarget { tabId = tab.id }
                        }, {})
                    }
                    "OFF" -> {
                        chrome.scripting.removeCSS(CSSInjection {
                            files = arrayOf("focus-mode.css")
                            target = InjectionTarget { tabId = tab.id }
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