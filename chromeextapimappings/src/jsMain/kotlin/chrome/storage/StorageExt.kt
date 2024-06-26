package chrome.storage

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.json

suspend inline fun <reified T> StorageArea.get(key: String): T = suspendCoroutine { c ->
    getAsync(key){
        c.resume(it[key] as T)
    }
}

suspend fun StorageArea.set(key: String, value: Any?): Unit = suspendCoroutine { c ->
    setAsync(json(key to value)) {
        c.resume(Unit)
    }
}