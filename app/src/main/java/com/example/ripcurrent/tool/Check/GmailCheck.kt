fun changeGmailAddress(gmail:String): String? {
    val gmailPattern = Regex("^(?!.*\\.\\.)[a-zA-Z0-9.]+@gmail\\.com\$")
    return when {
        gmail=="invalid"->null
        gmailPattern.matches(gmail) -> gmail // 已經是有效的 Gmail 地址
        gmail.isNotEmpty() -> "$gmail@gmail.com" // 在非空的情況下附加 @gmail.com
        else -> "invalid" // 其他情況，可能是空字串或無效格式
    }
}