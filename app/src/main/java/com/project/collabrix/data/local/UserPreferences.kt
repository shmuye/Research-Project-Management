package com.project.collabrix.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME = stringPreferencesKey("user_last_name")
    }

    fun getCurrentUser(): Flow<User?> = dataStore.data.map { preferences ->
        val id = preferences[USER_ID]?.toIntOrNull()
        val email = preferences[USER_EMAIL]
        val firstName = preferences[USER_FIRST_NAME]
        val lastName = preferences[USER_LAST_NAME]

        if (id != null && email != null && firstName != null && lastName != null) {
            User(id, email, firstName, lastName)
        } else {
            null
        }
    }

    suspend fun saveTokens(tokens: AuthTokens) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = tokens.access_token
            preferences[REFRESH_TOKEN] = tokens.refresh_token
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id.toString()
            preferences[USER_EMAIL] = user.email
            preferences[USER_FIRST_NAME] = user.firstName
            preferences[USER_LAST_NAME] = user.lastName
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(USER_ID)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_FIRST_NAME)
            preferences.remove(USER_LAST_NAME)
        }
    }

    fun getAccessTokenSync(): String? = runBlocking {
        dataStore.data.map { it[ACCESS_TOKEN] }.firstOrNull()
    }
} 