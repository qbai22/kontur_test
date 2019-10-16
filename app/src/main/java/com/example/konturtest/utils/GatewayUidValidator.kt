package xunison.com.smarthomeapp.utils

/**
 * Created by Vladimir Kraev
 */
class GatewayUidValidator {

    companion object {
        private const val PATTERN = "^[\\d\\w]{20}$"

        fun validate(value: String?): Boolean {
            value?.let {
                return it.matches(PATTERN.toRegex())
            } ?: return false
        }
    }

}