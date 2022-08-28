package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.socialapp.Utils.Companion.TAG
import com.example.socialapp.databinding.ActivitySignInBinding
import com.example.socialapp.models.User
import com.example.socialapp.models.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener { signIn() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    private fun signIn() {
        val signInIntent =  googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try{
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "handleSignInResult: "+account.idToken)
            firebaseAuthWithGoogle(account.idToken)
        } catch (e: ApiException){
            Log.w(TAG, "handleSignInResult: error")
            e.printStackTrace()
        }
    }

    private fun firebaseAuthWithGoogle(id: String?) {
        val cred = GoogleAuthProvider.getCredential(id, null)
        binding.progressBar.visibility = View.VISIBLE
        binding.signInButton.isEnabled = false
        auth.signInWithCredential(cred).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                updateUI(user)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        binding.progressBar.visibility = View.INVISIBLE
        firebaseUser?.let {
            val userDao = UserDao()
            userDao.addUser(firebaseUser)
            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}