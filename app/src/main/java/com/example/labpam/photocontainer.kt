package com.example.labpam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class photocontainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photocontainer)
        val arguments = intent.extras
        val string = arguments?.getString("string")
        val name = getBitmapFromString(string)
        val image = findViewById<ImageView>(R.id.imageView)
        image.setImageBitmap(name)
    }
    private fun getBitmapFromString(imageString: String?): Bitmap? {
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}