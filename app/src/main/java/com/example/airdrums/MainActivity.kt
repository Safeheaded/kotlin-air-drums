package com.example.airdrums

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.abs


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mSensorManager : SensorManager
    private var mAccelerometer : Sensor ?= null
    private var mMagneticFieldSensor: Sensor ?= null
    private var lastAcc = 0.0f
    private var canPlay = false
    var northPlayer: MediaPlayer? = null
    var southPlayer: MediaPlayer? = null
    var eastPlayer: MediaPlayer? = null
    var westPlayer: MediaPlayer? = null
    private var gravity = FloatArray(3)
    private var geoMagnetic = FloatArray(3)
    private var orientation = FloatArray(3)
    private var rotationMatrix = FloatArray(9)
    private var mAccelerometerAbsolute : Sensor ?= null
    private var currDirection : String = "N"
    private var canChangeDrums = false
    private var soundNorth = "bass_drum_5a"
    private var soundWest = "crash_cymbal_b"
    private var soundSouth = "floor_tum_drum_5a"
    private var soundEast = "hi_hat_b3"
    private var actualSoundN = R.raw.bass_drum_5a
    private var actualSoundW = R.raw.crash_cymbal_b
    private var actualSoundS = R.raw.floor_tum_drum_5a
    private var actualSoundE = R.raw.hi_hat_b3
    private lateinit var layout: ConstraintLayout
    private var currentColor: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.constraintLayout)
        val button = findViewById<Button>(R.id.change_drums_flag)
        layout.setBackgroundColor(Color.argb(255, 0, 13, 133))
        currentColor = Color.argb(255, 0, 13, 133)
//        activity = findViewById<MainActivity>(R.layout.activity_main)
//        button.setOnClickListener {
//            canChangeDrums = !canChangeDrums
//        }
        button.setOnTouchListener{ view, motionEvent ->
            // Controlling the button color.
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                canChangeDrums = true
                layout.setBackgroundColor(Color.rgb(122, 0, 120))
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                canChangeDrums = false
                layout.setBackgroundColor(currentColor)
            }
            view.performClick()
            view.onTouchEvent(motionEvent)
        }
        val soundN = intent.getStringExtra("soundNorth")
        if (soundN != null) {
            soundNorth = soundN
        }

        val soundW = intent.getStringExtra("soundWest")
        if (soundW != null) {
            soundWest = soundW
        }

        val soundS = intent.getStringExtra("soundSouth")
        if (soundS != null) {
            soundSouth = soundS
        }

        val soundE = intent.getStringExtra("soundEast")
        if (soundE != null) {
            soundEast = soundE
        }

        actualSoundN = setActualSound(soundNorth)
        actualSoundW = setActualSound(soundWest)
        actualSoundS = setActualSound(soundSouth)
        actualSoundE = setActualSound(soundEast)

        val goToSettingsButton = findViewById<Button>(R.id.go_to_settings)
        goToSettingsButton.setOnClickListener{
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            intent.putExtra("soundNorth", soundNorth)
            intent.putExtra("soundWest", soundWest)
            intent.putExtra("soundSouth", soundSouth)
            intent.putExtra("soundEast", soundEast)
            startActivity(intent)
        }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        supportActionBar?.hide()
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mAccelerometerAbsolute = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        northPlayer = MediaPlayer.create(this, actualSoundN)
        southPlayer = MediaPlayer.create(this, actualSoundS)
        eastPlayer = MediaPlayer.create(this, actualSoundE)
        westPlayer = MediaPlayer.create(this, actualSoundW)
        northPlayer!!.isLooping = false
        southPlayer!!.isLooping = false
        eastPlayer!!.isLooping = false
        westPlayer!!.isLooping = false
    }

    private fun setActualSound(soundName: String): Int{
        return when(soundName){
            "bass_drum_5a" -> R.raw.bass_drum_5a
            "crash_cymbal_b" -> R.raw.crash_cymbal_b
            "floor_tum_drum_5a" -> R.raw.floor_tum_drum_5a
            "hi_hat_b3" -> R.raw.hi_hat_b3
            "medium_tum_drum_5a" -> R.raw.medium_tom_drum_7a
            "snare_drum_2b" -> R.raw.snare_drum_2b
            "snare_drum_3a" -> R.raw.snare_drum_3a
            "tom_tom_drum_1" -> R.raw.tom_tom_drum_1
            else -> R.raw.bass_drum_5a
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        print("Sensor changed action")
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
//                Log.d("TAG", "SENSOR VAL: " + event.values[2])
                val currentAcc = event.values[2]
                if (currentAcc <= -6){
                    canPlay = true
                }
                if((currentAcc > lastAcc || currentAcc < -10) && canPlay){
//                    Log.d("TAG", "Period: " + )
                    canPlay = false
                    if(currDirection == "N"){
                        window.decorView.setBackgroundColor(0x001477)
                        if(northPlayer == null){
                            northPlayer = MediaPlayer.create(this, actualSoundN)
                            northPlayer!!.isLooping = false
                        }
                        if(northPlayer!!.isPlaying){
                            northPlayer!!.seekTo(0)
                            northPlayer!!.pause()
                        }
                        northPlayer!!.start()
                    }
                    else if (currDirection == "W"){
                        window.decorView.setBackgroundColor(0x008009)
                        if(westPlayer == null){
                            westPlayer = MediaPlayer.create(this, actualSoundW)
                            westPlayer!!.isLooping = false
                        }
                        if(westPlayer!!.isPlaying){
                            westPlayer!!.seekTo(0)
                            westPlayer!!.pause()
                        }
                        westPlayer!!.start()
                    }
                    else if (currDirection == "E"){
                        window.decorView.setBackgroundColor(0xb5a600)
                        if(eastPlayer == null){
                            eastPlayer = MediaPlayer.create(this, actualSoundE)
                            eastPlayer!!.isLooping = false
                        }
                        if(eastPlayer!!.isPlaying){
                            eastPlayer!!.seekTo(0)
                            eastPlayer!!.pause()
                        }
                        eastPlayer!!.start()
                    }
                    else if (currDirection == "S"){
                        window.decorView.setBackgroundColor(0x730000)
                        if(southPlayer == null){
                            southPlayer = MediaPlayer.create(this, actualSoundS)
                            southPlayer!!.isLooping = false
                        }
                        if(southPlayer!!.isPlaying){
                            southPlayer!!.seekTo(0)
                            southPlayer!!.pause()
                        }
                        southPlayer!!.start()
                    }
                }
                if(currentAcc > -6){
                    canPlay = false
                }
                lastAcc = currentAcc
            }
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER && !canPlay && abs(lastAcc) < .5 && canChangeDrums){
                gravity = event.values
                SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val direction = getDirection(orientation[0]);
                if(direction != currDirection){
                    findViewById<TextView>(R.id.sensor_value).text = direction;
                    changeActivityColor(direction)
                }
                currDirection = direction
//                Log.d("TAG", "ROTATION: " + orientation[1])
            }
            if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
                geoMagnetic = event.values
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, 2500)
        mSensorManager.registerListener(this, mMagneticFieldSensor, 2500)
        mSensorManager.registerListener(this, mAccelerometerAbsolute, 2500)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onStop() {
        super.onStop()
        if (northPlayer != null) {
            northPlayer!!.release()
            northPlayer = null
        }
        if (westPlayer != null) {
            westPlayer!!.release()
            westPlayer = null
        }
        if (southPlayer != null) {
            southPlayer!!.release()
            southPlayer = null
        }
        if (eastPlayer != null) {
            eastPlayer!!.release()
            eastPlayer = null
        }
    }

    private fun getDirection(azimuth: Float): String {
        return when((Math.toDegrees(azimuth.toDouble()) + 360).toInt() % 360) {
            in 45..134 -> "E"
            in 135..224 -> "S"
            in 225..314 -> "W"
            else -> "N"
        }
    }

    private fun changeActivityColor(direction: String){
        when(direction) {
            "N" -> {
//                layout.setBackgroundColor(Color.argb(255, 0, 13, 133))
                currentColor = Color.argb(255, 0, 13, 133)
            }
            "W" -> {
//                layout.setBackgroundColor(Color.argb(255, 0, 99, 3))
                currentColor = Color.argb(255, 0, 99, 3)
            }
            "S" -> {
//                layout.setBackgroundColor(Color.argb(255, 120, 0, 0))
                currentColor = Color.argb(255, 120, 0, 0)
            }
            "E" -> {
//                layout.setBackgroundColor(Color.argb(255, 189, 173, 0))
                currentColor = Color.argb(255, 189, 173, 0)
            }
        }
    }
}