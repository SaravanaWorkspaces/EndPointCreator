package com.obopay.endpointcreator

/**

Instance of the {@link EndPoint} to hold a URL.

@author Saravana kumar Chinnaraj
@Date 14,June,2019
@company Obopay
@version 1.0
 */
data class EndPoint(var protocol: String, var domain: String, var path: String) {

    fun hasWorldWideWeb(): Boolean {
        return domain.contains("www")
    }

    fun isSecured(): Boolean {
        return protocol.equals("https")
    }

    override fun toString(): String {
        return protocol + domain + path
    }
}