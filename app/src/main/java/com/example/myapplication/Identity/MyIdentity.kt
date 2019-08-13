package com.example.myapplication.Identity

import com.example.myapplication.Entity.User

object MyIdentity {
    var user: User? = null
    var token: String? = null

    fun isLoggedIn() : Boolean{
        return (token != null)
    }
}