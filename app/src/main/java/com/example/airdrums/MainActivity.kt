package com.example.airdrums

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.change_drums_flag)
        button.setOnClickListener {
            canChangeDrums = !canChangeDrums
        }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mAccelerometerAbsolute = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        northPlayer = MediaPlayer.create(this, R.raw.tom_tom_drum_1)
        southPlayer = MediaPlayer.create(this, R.raw.bass_drum_5a)
        eastPlayer = MediaPlayer.create(this, R.raw.hi_hat_b3)
        westPlayer = MediaPlayer.create(this, R.raw.snare_drum_2b)
        northPlayer!!.isLooping = false
        southPlayer!!.isLooping = false
        eastPlayer!!.isLooping = false
        westPlayer!!.isLooping = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        print("Sensor changed action")
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
//                Log.d("TAG", "SENSOR VAL: " + event.values[2])
                val currentAcc = event.values[2]
                if (currentAcc <= -4){
                    canPlay = true
                }
                if((currentAcc > lastAcc || currentAcc < -10) && canPlay){
//                    Log.d("TAG", "Period: " + )
                    canPlay = false
                    if(currDirection == "N"){
                        if(northPlayer == null){
                            northPlayer = MediaPlayer.create(this, R.raw.tom_tom_drum_1)
                            northPlayer!!.isLooping = false
                        }
                        if(northPlayer!!.isPlaying){
                            northPlayer!!.seekTo(0)
                            northPlayer!!.pause()
                        }
                        northPlayer!!.start()
                    }
                    else if (currDirection == "W"){
                        if(westPlayer == null){
                            westPlayer = MediaPlayer.create(this, R.raw.snare_drum_2b)
                            westPlayer!!.isLooping = false
                        }
                        if(westPlayer!!.isPlaying){
                            westPlayer!!.seekTo(0)
                            westPlayer!!.pause()
                        }
                        westPlayer!!.start()
                    }
                    else if (currDirection == "E"){
                        if(eastPlayer == null){
                            eastPlayer = MediaPlayer.create(this, R.raw.bass_drum_5a)
                            eastPlayer!!.isLooping = false
                        }
                        if(eastPlayer!!.isPlaying){
                            eastPlayer!!.seekTo(0)
                            eastPlayer!!.pause()
                        }
                        eastPlayer!!.start()
                    }
                    else if (currDirection == "S"){
                        if(southPlayer == null){
                            southPlayer = MediaPlayer.create(this, R.raw.hi_hat_b3)
                            southPlayer!!.isLooping = false
                        }
                        if(southPlayer!!.isPlaying){
                            southPlayer!!.seekTo(0)
                            southPlayer!!.pause()
                        }
                        southPlayer!!.start()
                    }
                }
                if(currentAcc > -4){
                    canPlay = false
                }
                lastAcc = currentAcc
            }
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER && !canPlay && abs(lastAcc) < .5 && canChangeDrums){
                gravity = event.values
                SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val direction = getDirection(orientation[0]);
                findViewById<TextView>(R.id.sensor_value).text = direction;
                currDirection = direction
//                Log.d("TAG", "ROTATION: " + orientation[1])
            }
            if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
                geoMagnetic = event.values
            }
        }
    }

//    private fun playSound(player: MediaPlayer?, sound: Int){
//        if(player == null){
//            player = MediaPlayer.create(this, sound)
//            player!!.isLooping = false
//        }
//        if(player!!.isPlaying){
//            player!!.seekTo(0)
//            player!!.pause()
//        }
//        player!!.start()
//    }

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
    }

    private fun getDirection(azimuth: Float): String {
        return when((Math.toDegrees(azimuth.toDouble()) + 360).toInt() % 360) {
            in 45..134 -> "E"
            in 135..224 -> "S"
            in 225..314 -> "W"
            else -> "N"
        }
    }
}