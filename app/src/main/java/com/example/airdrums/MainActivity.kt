package com.example.airdrums

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mSensorManager : SensorManager
    private var mAccelerometer : Sensor ?= null
    private var mMagneticFieldSensor: Sensor ?= null
    private var lastAcc = 0.0f
    private var canPlay = false
    var mMediaPlayer: MediaPlayer? = null
    private var gravity = FloatArray(3)
    private var geoMagnetic = FloatArray(3)
    private var orientation = FloatArray(3)
    private var rotationMatrix = FloatArray(9)
    private var mAccelerometerAbsolute : Sensor ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mAccelerometerAbsolute = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMediaPlayer = MediaPlayer.create(this, R.raw.tom_tom_drum_1)
        mMediaPlayer!!.isLooping = false
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
                    if(mMediaPlayer == null){
                        mMediaPlayer = MediaPlayer.create(this, R.raw.tom_tom_drum_1)
                        mMediaPlayer!!.isLooping = false
                    }
                    if(mMediaPlayer!!.isPlaying){
                        mMediaPlayer!!.seekTo(0)
                        mMediaPlayer!!.pause()
                    }
                    mMediaPlayer!!.start()
                }
                if(currentAcc > -4){
                    canPlay = false
                }
                lastAcc = currentAcc
            }
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER && !canPlay && abs(lastAcc) < .5){
                gravity = event.values
                SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)
                SensorManager.getOrientation(rotationMatrix, orientation)
                findViewById<TextView>(R.id.sensor_value).text = getDirection(orientation[0]);
                Log.d("TAG", "ROTATION: " + orientation[1])
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
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private fun getDirection(azimuth: Float): String {
        return when((Math.toDegrees(azimuth.toDouble()) + 360).toInt() % 360) {
            in 45..134 -> "W"
            in 135..224 -> "S"
            in 225..314 -> "E"
            else -> "N"
        }
    }
}