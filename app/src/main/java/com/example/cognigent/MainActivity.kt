package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var appName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logo) // matches logo.xml

        logo = findViewById(R.id.imageView)
        appName = findViewById(R.id.prjname)

        appName.alpha = 0f  //hide text initially

        // Animate logo: Zoom in + fade in
        logo.scaleX = 0f
        logo.scaleY = 0f
        logo.alpha = 0f

        logo.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(1500)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .start()

        Handler(Looper.getMainLooper()).postDelayed({
            appName.visibility = View.VISIBLE
            appName.animate()
                .alpha(1f)
                .setDuration(700)
                .start()
        }, 1600)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@MainActivity, loginpage::class.java))
            finish()
        }, 3000)
    }
}
