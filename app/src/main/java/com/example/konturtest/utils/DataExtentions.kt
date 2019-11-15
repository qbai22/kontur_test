package xunison.com.smarthomeapp.utils


/**
 * Created by Vladimir Kraev
 */


val Int.toTimeString: String
	get() = (if (this <= 9) "0$this"
	else this.toString())

val Int.to24HourFormatFromPM: Int
	get() = (if (this < 12) this + 12
	else this)
