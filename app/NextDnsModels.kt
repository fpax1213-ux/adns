package com.eyalm.adns

data class LogResponse(
    val data: List<LogEntry>
)

data class LogEntry(
    val timestamp: String,
    val domain: String,
    val status: String, // "allowed" or "blocked"
    val tracker: String? = null
)