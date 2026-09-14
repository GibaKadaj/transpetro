package com.prep.transpetro.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val notificationsEnabled: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    val state = prefs.notificationsEnabled.map { enabled ->
        SettingsUiState(notificationsEnabled = enabled)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { prefs.setNotificationsEnabled(enabled) }
    }
}
