package com.emotionstorage.auth.remote.dataSourceImpl

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.emotionstorage.auth.BuildConfig
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.common.DataResource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class GoogleRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : GoogleRemoteDataSource {
    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    override suspend fun getIdToken(): DataResource<String> {
        val credentialResult: GetCredentialResponse = credentialManager.getCredential(
            request = GetCredentialRequest
                .Builder()
                .addCredentialOption(
                    GetGoogleIdOption
                        .Builder()
                        .setFilterByAuthorizedAccounts(true)
                        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                        .setAutoSelectEnabled(false)
                        .build()
                )
                .build(),
            context = context,
        )

        credentialResult.credential.apply {
            if (this is CustomCredential && this.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(this.data)

                    return DataResource.Success(googleIdTokenCredential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    return DataResource.Error(e)
                }
            } else {
                return DataResource.Error(Exception("Unexpected type of credential"))
            }
        }
    }
}
