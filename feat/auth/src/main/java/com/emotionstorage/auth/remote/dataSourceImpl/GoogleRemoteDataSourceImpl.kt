package com.emotionstorage.auth.remote.dataSourceImpl

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.common.DataResource
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.emotionstorage.auth.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class GoogleRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : GoogleRemoteDataSource {
    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    override suspend fun getIdToken(): DataResource<String> {
        val deferredResult = CoroutineScope(Dispatchers.Default).async {
            try {
                val credentialResult: GetCredentialResponse = credentialManager.getCredential(
                    request = GetCredentialRequest.Builder().addCredentialOption(
                        GetSignInWithGoogleOption
                            .Builder(
                                serverClientId = BuildConfig.GOOGLE_CLIENT_ID
                            ).build()
                    ).build(),
                    context = context,
                )

                credentialResult.credential.apply {
                    if (this is CustomCredential && this.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(this.data)

                            Log.d(
                                "GoogleRemoteDataSourceImpl",
                                "getIdToken: ${googleIdTokenCredential.idToken}"
                            )
                            return@async DataResource.Success(googleIdTokenCredential.idToken)
                        } catch (e: GoogleIdTokenParsingException) {
                            return@async DataResource.Error(e)
                        }
                    } else {
                        Log.e(
                            "GoogleRemoteDataSourceImpl",
                            "getIdToken: Unexpected type of credential"
                        )
                        return@async DataResource.Error(Exception("Unexpected type of credential"))
                    }
                }
            } catch (e: Exception) {
                // todo: fix error
                // credential response error: androidx.credentials.exceptions.GetCredentialCustomException: [28444] Developer console is not set up correctly.

                Log.e("GoogleRemoteDataSourceImpl", "credential response error: $e")
                return@async DataResource.Error(e)
            }
        }
        return deferredResult.await() as DataResource<String>
    }
}
