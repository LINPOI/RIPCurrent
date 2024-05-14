package com.example.ripcurrent.tool.googleSignIn


//class AuthResultContract : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
//    override fun createIntent(context: Context, input: Int): Intent =
//        getGoogleSignInClient(context).signInIntent.putExtra("input", input)
//
//    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
//        return when (resultCode) {
//            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
//            else -> null
//        }
//    }
//}
//class AuthViewModel : ViewModel() {
//    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
//    val user: StateFlow<User?> = _user
//
//    suspend fun signIn(email: String, displayName: String) {
//        delay(2000) // Simulating network call
//        _user.value = User(email, displayName)
//    }
//}
//
//data class User(
//    val email: String,
//    val displayName: String
//)
