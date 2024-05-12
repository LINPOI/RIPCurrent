package com.example.ripcurrent.tool.googleSignIn

import android.content.Context
import com.example.ripcurrent.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun signInRequest(){
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("392500820152-fb76bsj06ot4lbc89383e6l78898e6n7.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build())
        .build()
}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("392500820152-fb76bsj06ot4lbc89383e6l78898e6n7.apps.googleusercontent.com") // Request id token if you intend to verify google user from your backend server
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}