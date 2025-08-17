package com.example.cognigent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper
import com.example.cognigent.common.Common

class profile : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        email = intent.getStringExtra("email") ?: ""
        dbHelper = DBHelper(this)
        val  newSubject = intent.getStringExtra("newSubject")
        val user = dbHelper.getUserNameAndCourse(email)

        findViewById<TextView>(R.id.profilename).text ="Name: " + user?.first
        findViewById<TextView>(R.id.profileemail).text = "Email: " + email
        findViewById<TextView>(R.id.profilecourse).text = "Course: " + user?.second

        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            startActivity(Intent(this, progress::class.java).apply {
                putExtra("email", email)
                putExtra("newSubject" , user?.second)

            })
        }





        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, homepage::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<TextView>(R.id.backIc).setOnClickListener {
            startActivity(Intent(this, homepage::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<TextView>(R.id.editbtn).setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        val logoutBtn = findViewById<TextView>(R.id.logout)
        logoutBtn.setOnClickListener {
            showLogoutPopup()
        }
        val deleteBtn = findViewById<TextView>(R.id.deleteId)
        deleteBtn.setOnClickListener {
            showLogoutPopup()
            dbHelper.deleteUserId(email)
            val intent = Intent(this, loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
    private fun showLogoutPopup() {
        val inflater = layoutInflater
        val popupView = inflater.inflate(R.layout.logout_popup, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show at center
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)

        val confirmBtn = popupView.findViewById<Button>(R.id.yesButton)
        val cancelBtn = popupView.findViewById<Button>(R.id.noButton)

        confirmBtn.setOnClickListener {
            popupWindow.dismiss()
            // Go to login page or close session


            Common.clearAll(this)
            val intent = Intent(this, loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        cancelBtn.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}
