@file:JsQualifier("chrome.storage")
package chrome.storage

import kotlin.js.Json


//external interface StorageArea {
//    fun getBytesInUse(callback: (bytesInUse: Number) -> Unit)
//    fun getBytesInUse(keys: String, callback: (bytesInUse: Number) -> Unit)
//    fun getBytesInUse(keys: Array<String>, callback: (bytesInUse: Number) -> Unit)
//    fun getBytesInUse(keys: Nothing?, callback: (bytesInUse: Number) -> Unit)
//    fun clear(callback: (() -> Unit)? = definedExternally /* null */)
//    fun set(items: Json, callback: (() -> Unit)?/* null */): Promise<Unit>
//    fun remove(keys: String, callback: (() -> Unit)? = definedExternally /* null */)
//    fun remove(keys: Array<String>, callback: (() -> Unit)? = definedExternally /* null */)
//    fun remove(keys: Nothing?, callback: (() -> Unit)? = definedExternally /* null */)
//    fun get(keys: String, callback: (items: Json) -> Unit): Promise<Json>
//    fun get(keys: Array<String>, callback: (items: Json) -> Unit): Promise<Json>
//    fun get(keys: Any, callback: (items: Json) -> Unit): Promise<Json>
//    fun onChanged(callback: (changes: Any) -> Unit)
//}

external interface StorageArea {
    @JsName("get")
    fun getAsync(vararg keys: String, callback: (items: Json) -> Unit)
    @JsName("set")
    fun setAsync(items: Json, callback: () -> Unit)
}

external interface LocalStorageArea : StorageArea {
    var QUOTA_BYTES: Number
}

external var local: LocalStorageArea