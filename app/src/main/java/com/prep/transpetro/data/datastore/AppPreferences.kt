package com.prep.transpetro.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val THEME_DARK = booleanPreferencesKey("theme_dark")
        val FIRST_LAUNCH_DONE = booleanPreferencesKey("first_launch_done")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    val themeDarkFlow = context.dataStore.data.map { it[THEME_DARK] ?: true }
    val firstLaunchDoneFlow = context.dataStore.data.map { it[FIRST_LAUNCH_DONE] ?: false }
    val notificationsEnabledFlow = context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }

    suspend fun setThemeDark(dark: Boolean) {
        context.dataStore.edit { it[THEME_DARK] = dark }
    }

    suspend fun setFirstLaunchDone(done: Boolean) {
        context.dataStore.edit { it[FIRST_LAUNCH_DONE] = done }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }
}
