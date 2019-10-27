package com.example.konturtest.screen.common

import androidx.annotation.StringRes

/**
 * Created by Vladimir Kraev
 */
interface ErrorView {

    fun showError(@StringRes message: Int)

}