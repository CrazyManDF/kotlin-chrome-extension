import chrome.action.setBadgeText
import chrome.others.*
import chrome.runtime.InstalledDetails
import chrome.storage.get
import chrome.storage.set
import chrome.tabs.Tab
import com.example.template.data.Message
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.floor
import kotlin.random.Random

const val extensions = "https://developer.chrome.com/docs/extensions"
const val webstore = "https://developer.chrome.com/docs/webstore"

const val URL_CHROME_EXTENSIONS_DOC = "https://developer.chrome.com/docs/extensions/reference/api/"
const val NUMBER_OF_PREVIOUS_SEARCHES = 4

const val ALARM_NAME = "tip"

fun main() {
    println("====Background script initialized")
    setBadgeInfo()

    setAlarm()

    sendResponseMessage()

    displaySuggestions()
}

/**
 * 定时更新tip
 */
fun setAlarm() {
    createAlarm()
    chrome.alarms.onAlarm.addListener<Unit> { updateTip() }
}

fun displaySuggestions() {
    chrome.omnibox.onInputChanged.addListener { text, suggest ->
        chrome.omnibox.setDefaultSuggestion(DefaultSuggestResult {
            description = "Enter a Chrome API or choose from past searches"
        }, {})

        GlobalScope.launch {
            val array = chrome.storage.local.get<Array<String>>("apiSuggestions")
            val data = array.map { api ->
                SuggestResult {
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

fun updateHistory(input: String) = GlobalScope.launch {
    val array = chrome.storage.local.get<Array<String>>("apiSuggestions")
    val newArray = array.toMutableList().apply {
        add(0, input)
    }.slice(0..<NUMBER_OF_PREVIOUS_SEARCHES).toTypedArray()
    chrome.storage.local.set("apiSuggestions", newArray)
}

fun setBadgeInfo() {
    chrome.runtime.onInstalled.addListener<InstalledDetails> {
        // 将默认建议保存到存储空间
        if (it.reason == "install") {
            GlobalScope.launch {
                chrome.storage.local.set("apiSuggestions", arrayOf("tabs", "storage", "scripting"))
            }
        }
        val textDetails = BadgeTextDetails {
            text = "OFF"
        }
        setBadgeText(textDetails)
    }
    chrome.action.onClicked.addListener<Tab> { tab ->
        // 用这个网址测试 https://developer.chrome.com/docs/webstore/publish

        if (tab.url?.startsWith(extensions) == true ||
            tab.url?.startsWith(webstore) == true
        ) {
            // 更改标签
            chrome.action.getBadgeText(TabDetails {
                tabId = tab.id
            }).then {
                if (it == "ON") "OFF" else "ON"
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
        println("Background receives a message: $request")
        if (request == ALARM_NAME) {
           MainScope().launch {
                val data = chrome.storage.local.get<String>(ALARM_NAME)
                println("返回: $data")
                sendResponse(data)
           }
        } else {
            val message = Json.decodeFromString<Message>(request.toString())
            val response = when (message.action) {
                "POPUP_CLICK" -> Message(action = "BG_RESPONSE", message = "Message from background to popup")
                "CONTENT_SCRIPT_CLICK" -> Message(
                    action = "BG_RESPONSE",
                    message = "Message from background to content script"
                )

                else -> Message(action = "ERROR", message = "Action not handled")
            }
            sendResponse(Json.encodeToString(response))
        }
    }
}

fun createAlarm() {
    chrome.alarms.getAlarm(ALARM_NAME).then { alarm ->
        if (alarm == null) {
            chrome.alarms.create(ALARM_NAME, AlarmCreateInfo {
                delayInMinutes = 1.0
                periodInMinutes = 2.0
            })
            updateTip()
        }
    }
}



fun updateTip() {
    GlobalScope.launch {
        val httpClient = HttpClient()
        val response = httpClient.get("https://extension-tips.glitch.me/tips.json")
        val tipsStr: String = response.body()
        val tips = Json.decodeFromString<Array<String>>(tipsStr)
        val randomIndex = floor(Random.nextFloat() * tips.size).toInt()
        println("randomIndex = $randomIndex")
        println("randomIndex = ${tips[randomIndex]}")
        chrome.storage.local.set(ALARM_NAME, tips[randomIndex])
    }
}


// https://extension-tips.glitch.me/tips.json
const val JSON_DATA = """
    [
  "<p>All extensions must have a <a href=\"https://developer.chrome.com/docs/extensions/mv3/single_purpose/#three\">single purpose</a>.</p>",
  "<p>Develop your extension using <a href=\"https://www.google.com/chrome/beta/\">Chrome Beta</a> to test the latest upcoming platform features.</p>",
  "<p>Follow the latest news about Chrome extensions in <a href=\"https://developer.chrome.com/docs/extensions/whatsnew/\">What's new?</a></p>",
  "<p>Provide meaningful <a href=\"https://developer.chrome.com/docs/webstore/manage/#provide-user-support\">customer support</a> for your extension.</p>",
  "<p>Localize your extension with the <a href=\"https://developer.chrome.com/docs/extensions/reference/i18n/\">i18n API.</a></p>",
  "<p>Ensure your <a href=\"https://developer.chrome.com/docs/webstore/best_listing/\">store listing</a> is easy to use and understand for users and up-to-date.</p>",
  "<p>Minimize your <a href=\"https://developer.chrome.com/docs/extensions/mv3/declare_permissions/\">permissions</a> to only what is necessary to support your main feature.</p>",
  "<p>Test your extension thoroughly before submitting an update to the store.</p>",
  "<p>Remember that adding a new permission that  <a href=\"https://developer.chrome.com/docs/extensions/mv3/permission_warnings/#permissions_with_warnings\">triggers a warning</a> will disable your extension.</p>",
  "<p>Use<a href=\"https://developer.chrome.com/docs/extensions/reference/permissions/\"> optional permissions</a> to allow the user to enable additional extension features.</p>",
  "<p>Interact with the Chrome developer community to get feedback in the <a href=\"https://groups.google.com/a/chromium.org/g/chromium-extensions\">Chromium Google group</a>.</p>",
  "<p>Make sure your extension has a clear and concise user interface that is easy to understand.</p>",
  "<p>Add a <a href=\"https://developer.chrome.com/docs/webstore/cws-dashboard-listing/#graphic-assets\">YouTube video</a> to your extension store listing that explains how to use your extension.</p>",
  "<p>Learn about the<a href=\"https://developer.chrome.com/docs/webstore/review-process/#review-time-factors\"> notable factors that increase Chrome Web Store review times.</a></p>",
  "<p>Bundle all third-party libraries and frameworks in the extension package. Remote code is not supported</p>",
  "<p>Create an onboarding experience by opening a new page when the extension is first installed using runtime.onInstalled()</p>",
  "<p>Consider user privacy and security when designing your extension.</p>",
  "<p>Provide users with the ability to customize the behavior and appearance of your extension.</p>"
]
"""