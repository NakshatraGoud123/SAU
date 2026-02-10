package com.nisr.sau.utils


object UserSession {
    var username: String = ""
    var email: String = ""
    var phone: String = ""
    var location: String = "Saudi Arabia"
    var profileImageUrl: String = ""
    var isLoggedIn: Boolean = false

    fun clear() {
        username = ""
        email = ""
        phone = ""
        location = "Saudi Arabia"
        profileImageUrl = ""
        isLoggedIn = false
    }
}
