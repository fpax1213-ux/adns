package com.eyalm.adns.data.models

sealed class DnsProvider {

    abstract val id: String
    abstract val name: String
    abstract val isEnhanced: Boolean

    data class Standard(
        override val id: String,
        override val name: String,
        val hostname: String      //"dns.adguard-dns.com" etc
    ) : DnsProvider() {
        override val isEnhanced = false
    }

    data class Enhanced(
        override val id: String,
        override val name: String,
        val hostname: String      // dynamic from apiService
    ) : DnsProvider() {
        override val isEnhanced = true
    }
}


