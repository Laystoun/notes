package com.example.notes.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.notes.utils.datastore.isGuest.GuestOrRegistration
import kotlinx.coroutines.flow.map

private val Context.store: DataStore<Preferences> by preferencesDataStore("user_is_guest")

class DataStoreConfiguration(
    val context: Context
) {
    suspend fun setValueGuestConf(obj: GuestOrRegistration) {
        context.store.edit { pref ->
            pref[booleanPreferencesKey("isGuest")] = obj.isGuest
            pref[booleanPreferencesKey("showChoice")] = obj.showChoice
        }
    }

    fun getValueGuestConf() = context.store.data.map { pref ->
        return@map GuestOrRegistration(
            isGuest = pref[booleanPreferencesKey("isGuest")] ?: true,
            showChoice = pref[booleanPreferencesKey("showChoice")] ?: true
        )
    }
}