package com.blackspider.recetapp.recursos

import android.content.Context
import android.content.SharedPreferences
import com.blackspider.recetapp.model.mPreferencias

class sessionManager {


    constructor(context: Context) {

        this._context = context
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = this.pref!!.edit()
    }

    companion object {

        val KEY_ID = "id"

        val KEY_MEDICO = "medico"

    }

    // Shared Preferences
    var pref: SharedPreferences? = null

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null

    // Context
    var _context: Context? = null

    // Shared pref mode
    var PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "RecetAppPref"

    // All Shared Preferences Keys
    private val IS_LOGIN = "IsLoggedIn"


    /**
     * Create login session
     * */

    fun createLoginSession(id : Long, medico: Boolean) {
        // Storing login value as TRUE
        editor!!.putBoolean(IS_LOGIN, true)

        editor!!.putLong(KEY_ID, id)

        // Storing name in pref
        editor!!.putBoolean(KEY_MEDICO, medico)

        // commit changes
        editor!!.commit()
    }

    fun getUserDetails(): mPreferencias {
        /* val user = HashMap<String, String>()
         // user name
         user[KEY_NAME] = pref!!.getString(KEY_NAME, null)

         // user email id
         user[KEY_EMAIL] = pref!!.getString(KEY_EMAIL, null)
         user[KEY_ID] = pref!!.getInt(KEY_EMAIL, null)

         // return user*/

        val datos  = mPreferencias ( pref!!.getLong(KEY_ID,0), pref!!.getBoolean(KEY_MEDICO, false))

        return datos
    }

    fun checkLogin(): Boolean {
        // Check login status
        if (!this.isLoggedIn()) {

            return false
            // user is not logged in redirect him to Login Activity
          /*  val i = Intent(_context, LoginActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context!!.startActivity(i)*/
        }

        return true

    }

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor!!.clear()
        editor!!.commit()

        // After logout redirect user to Loing Activity
       /* val i = Intent(_context, LoginActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login Activity
        _context!!.startActivity(i)*/
    }

    private fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }

}