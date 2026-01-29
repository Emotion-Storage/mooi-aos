package com.emotionstorage.auth.util

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.emotionstorage.auth.BuildConfig
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class GoogleCredentialManager(
    private val context: Context
) {
    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    suspend fun getIdToken(): String {
        val deferredResult =
            CoroutineScope(Dispatchers.Default).async {
                try {
                    // credential request for google login
                    val credentialRequest =
                        GetCredentialRequest
                            .Builder()
                            .addCredentialOption(
                                GetSignInWithGoogleOption
                                    .Builder(
                                        serverClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID,
                                    ).build(),
                            ).build()

                    // get credential result
                    val credentialResult: GetCredentialResponse =
                        credentialManager.getCredential(
                            request = credentialRequest,
                            context = context,
                        )

                    // parse credential result
                    credentialResult.credential.apply {
                        if (
                            this is CustomCredential &&
                            this.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        ) {
                            try {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential
                                        .createFrom(this.data)
                                return@async googleIdTokenCredential.idToken
                            } catch (e: GoogleIdTokenParsingException) {
                                throw Exception("Invalid Google ID token", e)
                            }
                        } else {
                            throw Exception("Unexpected type of credential")
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Google credential failed", e)
                }
            }
        return deferredResult.await() as String
    }
}
