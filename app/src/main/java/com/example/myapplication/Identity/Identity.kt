package com.example.myapplication.Identity

import com.example.myapplication.Entity.User

data class Identity (
    // ToDO check if I can use only MyIdentity
    var user: User,
    var token: String
)