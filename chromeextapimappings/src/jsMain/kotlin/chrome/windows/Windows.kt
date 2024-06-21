@file:JsQualifier("chrome.windows")
package chrome.windows

import chrome.tabs.WindowType

external interface QueryOptions {
    var populate: Boolean
    var windowTypes: Array<WindowType>
}

//external fun getCurrent(queryOptions: QueryOptions?, callback: Callback<Window>?)