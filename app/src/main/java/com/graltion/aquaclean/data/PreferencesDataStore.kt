package com.graltion.aquaclean.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aquaclean_prefs")

class PreferencesDataStore(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val REPEAT_COUNT = intPreferencesKey("repeat_count")
        val THEME = stringPreferencesKey("theme")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .map { it[ONBOARDING_COMPLETE] ?: false }

    val isVibrationEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[VIBRATION_ENABLED] ?: true }

    val isNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[NOTIFICATION_ENABLED] ?: true }

    val repeatCount: Flow<Int> = context.dataStore.data
        .map { it[REPEAT_COUNT] ?: 1 }

    val theme: Flow<String> = context.dataStore.data
        .map { it[THEME] ?: "system" }

    val language: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE] ?: "system" }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { it[ONBOARDING_COMPLETE] = true }
    }

    suspend fun setVibration(enabled: Boolean) {
        context.dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }

    suspend fun setNotification(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATION_ENABLED] = enabled }
    }

    suspend fun setRepeatCount(count: Int) {
        context.dataStore.edit { it[REPEAT_COUNT] = count }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME] = theme }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE] = language }
    }
}
