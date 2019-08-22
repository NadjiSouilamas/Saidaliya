package com.example.myapplication.Identity

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Entity.User

object MyIdentity{

    lateinit var context: Context
    private val KEY_TEL_USER = "tel_user"
    private val KEY_TOKEN = "token_user"

    private val PREF_NAME = "identity"
    val sharedPref : SharedPreferences by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    fun setUserTel(tel: String){
        val editor = sharedPref.edit()
        editor.putString(KEY_TEL_USER, tel)
        editor.commit()
    }

    fun setUserToken(token: String){
        val editor = sharedPref.edit()
        editor.putString(KEY_TOKEN, token)
        editor.commit()
    }

    fun getUserTel() : String{
        return sharedPref.getString(KEY_TEL_USER, "")
    }

    fun getUserToken() : String{
        return sharedPref.getString(KEY_TOKEN, "")
    }

    fun clearUserData(){
        val editor = sharedPref.edit()
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean{

        return ( getUserToken() != "")
    }

}