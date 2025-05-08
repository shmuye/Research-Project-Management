package com.project.collabrix.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User
import com.project.collabrix.domain.usecase.auth.SigninUseCase
import com.project.collabrix.domain.usecase.auth.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val tokens: AuthTokens, val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val signinUseCase: SigninUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signup(email: String, password: String, name: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            signupUseCase(email, password, name, role)
                .onSuccess { (tokens, user) ->
                    _authState.value = AuthState.Success(tokens, user)
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Signup failed")
                }
        }
    }

    fun signin(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            signinUseCase(email, password)
                .onSuccess { (tokens, user) ->
                    _authState.value = AuthState.Success(tokens, user)
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Signin failed")
                }
        }
    }
} 