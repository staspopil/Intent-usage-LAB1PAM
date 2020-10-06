package com.example.labpam

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.labpam.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {
    var TYPE_PHOTO=1
    var REQUEST_CODE_PHOTO =1
    private lateinit var photointent:Intent
    private lateinit var radioGroup : RadioGroup
    private lateinit var mCamera: Camera
    private lateinit var directory:File
    private var CHANNEL_ID = "firstchannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        radioGroup = binding.radioGroup
         photointent = Intent(this, photocontainer::class.java)

        binding.btCamera.setOnClickListener {
            startCamera()
        }
        binding.btSearch.setOnClickListener {
            searchFor(binding.SearchField.text.toString())
        }

        binding.btPushnot.setOnClickListener {
            startNotificationAfterTenSec("Lab1 Notification","This is Notification from condition 1")
        }

        setContentView(binding.root)
    }

    private fun getStringFromBitmap(bitmapPicture: Bitmap): String? {
        val COMPRESSION_QUALITY = 100
        val byteArrayBitmapStream = ByteArrayOutputStream()
        val encodedImage: String
        bitmapPicture.compress(
            Bitmap.CompressFormat.PNG,
            COMPRESSION_QUALITY,
            byteArrayBitmapStream
        )
        val b = byteArrayBitmapStream.toByteArray()
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        return encodedImage
    }
            private fun startNotificationAfterTenSec(title:String,text:String) {
             val  notificationmanager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val intent : Intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                 val builder = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
                     .setAutoCancel(false)
                     .setSmallIcon(R.drawable.ic_launcher_foreground)
                     .setWhen(System.currentTimeMillis())
                     .setContentIntent(pendingIntent)
                     .setContentTitle(title)
                     .setContentText(text)
                     .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                     .setTimeoutAfter(10000)
                createNotificationChannel()
                with(NotificationManagerCompat.from(this)) {
                    notify(1, builder.build())
                }
            }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChannelName"
            val descriptionText="ChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     //   super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHOTO&&resultCode == Activity.RESULT_OK) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val string = getStringFromBitmap(bitmap)
                      photointent.putExtra("string", string)
                startActivity(photointent)}
            else {
            super.onActivityResult(requestCode, resultCode, data)
            }
    }
                 fun searchFor(string: String) {
                     val searcher: Intent = Intent()
                     searcher.setAction(Intent.ACTION_WEB_SEARCH)
                     searcher.putExtra(SearchManager.QUERY, string)
                     startActivity(searcher)
                 }
                 fun startCamera() {
                     val checkedRB = radioGroup.checkedRadioButtonId
                     when (checkedRB){
                        findViewById<RadioButton>(R.id.rbtBack).id -> Toast.makeText(applicationContext,"BackCamera",Toast.LENGTH_SHORT)
                        findViewById<RadioButton>(R.id.rbtFront).id -> Toast.makeText(applicationContext,"FrontCamera",Toast.LENGTH_SHORT)
                     }
                     val camera: Intent = Intent()
                     camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
                     startActivityForResult(camera, REQUEST_CODE_PHOTO);
                 }
             }

