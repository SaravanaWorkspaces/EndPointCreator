package com.obopay.endpointcreator

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
@author Saravana kumar Chinnaraj
@Date 11,June,2019
@company Obopay
@version 1.0
 */
class Utils {

    companion object {
        private const val HTTP = "http://"
        private const val HTTPS = "https://"
        private val DOMAIN_PATTERN = "[a-zA-Z0-9-_]+\\.[a-zA-Z.]{2,256}".toRegex()
        private const val WORLD_WIDE_WEB = "www"
        private const val DOT = "."

        fun checkDomainName(domainName: String): String? {
            if (domainName.isNullOrEmpty()) {
                return "Enter domain (i.e: xyz.com)"
            } else if (!isValidDomainName(domainName)) {
                return "Enter valid domain name (i.e: xyz.com)"
            }
            return null
        }

        /** Checks given domainName is valid domain name or not. **/
        private fun isValidDomainName(domainName: String): Boolean {
            return DOMAIN_PATTERN.matches(domainName)
        }

        fun dismissKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun createFullURL(secureProtocol: Boolean, supportWWW: Boolean, domain: String, path: String?): String {
            if (domain.isNullOrEmpty()) {
                return ""
            }
            var protocol = HTTP
            if (secureProtocol) {
                protocol = HTTPS
            }

            val sb = StringBuilder()
            sb.append(protocol)

            if (supportWWW) {
                sb.append(WORLD_WIDE_WEB)
                    .append(DOT)
            }
            sb.append(domain)

            if (!path.isNullOrEmpty()) {
                sb.append(path)
            }
            return sb.toString()
        }

        fun hasWorldWideWeb(url: String): Boolean {
            return url.contains(WORLD_WIDE_WEB)
        }

        fun isSecured(url: String): Boolean {
            return HTTPS.contains(url);
        }

        fun removeWorldWide(domain: String): String {
            return domain.replace(WORLD_WIDE_WEB + DOT, "")
        }
    }
}