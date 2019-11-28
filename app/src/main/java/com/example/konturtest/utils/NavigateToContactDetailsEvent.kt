package com.example.konturtest.utils

/**
 * Created by Vladimir Kraev
 */

/**
 * Class to observe navigation event to [ContactDetailsFragment]
 */
class NavigateToContactDetailsEvent(contactId: String) : Event<String>(contactId)
