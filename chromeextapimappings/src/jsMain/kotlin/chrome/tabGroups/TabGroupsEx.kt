package chrome.tabGroups

enum class Color {
    grey,
    blue,
    red,
    yellow,
    green,
    pink,
    purple,
    cyan,
    orange,
}

fun UpdateProperties(block: UpdateProperties.() -> Unit): UpdateProperties =
    (js("{}") as UpdateProperties).apply(block)