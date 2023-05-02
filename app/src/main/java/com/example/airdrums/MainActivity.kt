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
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mSensorManager : SensorManager
    private var mAccelerometer : Sensor ?= null
    private var lastAcc = 0.0f
    private var canPlay = false
    var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mMediaPlayer = MediaPlayer.create(this, R.raw.tom_tom_drum_1)
        mMediaPlayer!!.isLooping = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        print("Sensor changed action")
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                Log.d("TAG", "SENSOR VAL: " + event.values[2])
                val currentAcc = event.values[2]
//                findViewById<TextView>(R.id.sensor_value).text = event.values[2].roundToInt().toString()
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
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, 2500)
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
}