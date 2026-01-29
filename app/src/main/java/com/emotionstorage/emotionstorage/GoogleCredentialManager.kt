package com.emotionstorage.emotionstorage

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GoogleCredentialManager(
    private val activity: Activity,
    private val coroutineScope: CoroutineScope,
) {
    suspend fun getIdToken(): String {
        val deferredResult =
            coroutineScope.async {
                var credentialRequest: GetCredentialRequest? = null
                try {
                    // credential request for google login
                    credentialRequest =
                        GetCredentialRequest
                            .Builder()
                            .addCredentialOption(
                                GetSignInWithGoogleOption
                                    .Builder(
                                        serverClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID,
                                    ).build(),
                            ).build()
                }catch (e: Exception) {
                    throw Exception("build credential request error, ${e.message}", e)
                }

                var credentialResult: GetCredentialResponse? = null
                try {
                    // create credential manager & get result
                    val credentialManager = CredentialManager.create(activity)
                    credentialResult =
                        credentialManager.getCredential(
                            request = credentialRequest,
                            context = activity,
                        )
                }catch (e: Exception){
                    throw Exception("get credential result error, ${e.message}", e)
                }

                try {
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
                                throw Exception("google ID token parse exception, ${e.message}", e)
                            }
                        } else {
                            throw Exception("Unexpected type of credential")
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("parse credential result error, ${e.message}", e)
                }
            }
        return deferredResult.await() as String
    }
}
