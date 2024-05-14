package com.example.ripcurrent.tool.googleSignIn

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ripcurrent.R
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

//@Composable
//fun SignInButton(
//    text: String,
//    loadingText: String = "Signing in...",
//    icon: Painter,
//    isLoading: Boolean = false,
//    shape: CornerBasedShape = MaterialTheme.shapes.medium,
//    borderColor: Color = Color.LightGray,
//    backgroundColor: Color = MaterialTheme.colorScheme.surface,
//    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
//    onClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier.clickable(
//            enabled = !isLoading,
//            onClick = onClick
//        ),
//        shape = shape,
//        border = BorderStroke(width = 1.dp, color = borderColor),
//        color = backgroundColor
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(
//                    start = 12.dp,
//                    end = 16.dp,
//                    top = 12.dp,
//                    bottom = 12.dp
//                ),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//        ) {
//            Icon(
//                painter = icon,
//                contentDescription = "SignInButton",
//                tint = Color.Unspecified
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Text(text = if (isLoading) loadingText else text)
//            if (isLoading) {
//                Spacer(modifier = Modifier.width(16.dp))
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .height(16.dp)
//                        .width(16.dp),
//                    strokeWidth = 2.dp,
//                    color = progressIndicatorColor
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun AuthView(
//    errorText: String?,
//    onClick: () -> Unit
//) {
//    var isLoading by remember { mutableStateOf(false) }
//
//    Surface{
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            SignInButton(
//                text = "Sign in with Google",
//                loadingText = "Signing in...",
//                isLoading = isLoading,
//                icon = painterResource(id = R.drawable.ic_google_logo),
//                onClick = {
//                    isLoading = true
//                    onClick()
//                }
//            )
//
//            errorText?.let {
//                isLoading = false
//                Spacer(modifier = Modifier.height(30.dp))
//                Text(text = it)
//            }
//        }
//    }
//}
//@ExperimentalAnimationApi
//@ExperimentalFoundationApi
//@ExperimentalCoroutinesApi
//@Composable
//fun AuthScreen(
//    authViewModel: AuthViewModel
//) {
//    val coroutineScope = rememberCoroutineScope()
//    var text by remember { mutableStateOf<String?>(null) }
//    val user by remember(authViewModel) { authViewModel.user }.collectAsState()
//    val signInRequestCode = 1
//
//    val authResultLauncher =
//        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
//            try {
//                val account = task?.getResult(ApiException::class.java)
//                if (account == null) {
//                    text = "Google sign in failed"
//                } else {
//                    coroutineScope.launch {
//                        account.email?.let {
//                            account.displayName?.let { it1 ->
//                                authViewModel.signIn(
//                                    email = it,
//                                    displayName = it1,
//                                )
//                            }
//                        }
//                    }
//                }
//            } catch (e: ApiException) {
//                text = "Google sign in failed"
//            }
//        }
//
//    AuthView(
//        errorText = text,
//        onClick = {
//            text = null
//            authResultLauncher.launch(signInRequestCode)
//        }
//    )
//
//    user?.let {
//        HomeScreen(user = it)
//    }
//}
//@Composable
//fun HomeScreen(user: User) {
//    Scaffold { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Hello, ${user.displayName}",
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.titleSmall,
//                fontSize = 30.sp
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(text = user.email)
//        }
//    }
//}

