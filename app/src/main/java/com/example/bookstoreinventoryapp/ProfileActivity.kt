package com.example.bookstoreinventoryapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameView: TextView
    private lateinit var profileNameView: TextView
    private lateinit var emailView: TextView
    private lateinit var logoutBtn: MaterialButton
    private lateinit var notificationSwitch: SwitchMaterial
    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var profileImageView: ImageView

    private val REQUEST_CODE_PICK_IMAGE = 101
    private val REQUEST_CODE_PERMISSION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Profile"

        nameView = findViewById(R.id.tv_name)
        profileNameView = findViewById(R.id.tv_profile_name)
        emailView = findViewById(R.id.tv_email)
        logoutBtn = findViewById(R.id.btn_logout)
        notificationSwitch = findViewById(R.id.switch_notifications)
        darkModeSwitch = findViewById(R.id.switch_dark_mode)
        profileImageView = findViewById(R.id.profile_image)

        loadUserData()
        setupSwitches()
        setupLogoutButton()

        profileImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                openGallery()
            }
        }
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences("user_auth", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId != -1) {
            val db = UserDatabaseHelper(this)
            val user = db.getUserById(userId)
            user?.let {
                profileNameView.text = it.name
                nameView.text = it.name
                emailView.text = it.email
            }
        } else {
            redirectToLogin()
        }

        val imagePath = prefs.getString("profile_image_path", null)
        imagePath?.let {
            val file = File(it)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                profileImageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun setupSwitches() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
        val darkModeEnabled = prefs.getBoolean("dark_mode_enabled", false)

        notificationSwitch.isChecked = notificationsEnabled
        darkModeSwitch.isChecked = darkModeEnabled

        findViewById<View>(R.id.notification_setting)?.setOnClickListener {
            notificationSwitch.isChecked = !notificationSwitch.isChecked
        }

        findViewById<View>(R.id.dark_mode_setting)?.setOnClickListener {
            darkModeSwitch.isChecked = !darkModeSwitch.isChecked
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply()
            val message = if (isChecked) "Notifications enabled" else "Notifications disabled"
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode_enabled", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupLogoutButton() {
        logoutBtn.setOnClickListener {
            android.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    getSharedPreferences("user_auth", MODE_PRIVATE).edit().clear().apply()
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    redirectToLogin()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let { uri ->
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val file = File(filesDir, "profile_image.jpg")
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    profileImageView.setImageBitmap(bitmap)

                    getSharedPreferences("user_auth", MODE_PRIVATE)
                        .edit()
                        .putString("profile_image_path", file.absolutePath)
                        .apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_down)
    }
}
