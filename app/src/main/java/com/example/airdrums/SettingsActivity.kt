package com.example.airdrums

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    val northItems: Array<String> = arrayOf("bass_drum_5a", "crash_cymbal_b", "floor_tum_drum_5a", "hi_hat_b3", "medium_tum_drum_5a", "snare_drum_2b", "snare_drum_3a", "tom_tom_drum_1")
    val westItems: Array<String> = arrayOf("bass_drum_5a", "crash_cymbal_b", "floor_tum_drum_5a", "hi_hat_b3", "medium_tum_drum_5a", "snare_drum_2b", "snare_drum_3a", "tom_tom_drum_1")
    val southItems: Array<String> = arrayOf("bass_drum_5a", "crash_cymbal_b", "floor_tum_drum_5a", "hi_hat_b3", "medium_tum_drum_5a", "snare_drum_2b", "snare_drum_3a", "tom_tom_drum_1")
    val eastItems: Array<String> = arrayOf("bass_drum_5a", "crash_cymbal_b", "floor_tum_drum_5a", "hi_hat_b3", "medium_tum_drum_5a", "snare_drum_2b", "snare_drum_3a", "tom_tom_drum_1")

    private lateinit var autoCompleteTextViewNorth: AutoCompleteTextView
    private lateinit var autoCompleteTextViewWest: AutoCompleteTextView
    private lateinit var autoCompleteTextViewSouth: AutoCompleteTextView
    private lateinit var autoCompleteTextViewEast: AutoCompleteTextView

    private lateinit var adapterItemsNorth: ArrayAdapter<String>;
    private lateinit var adapterItemsWest: ArrayAdapter<String>;
    private lateinit var adapterItemsSouth: ArrayAdapter<String>;
    private lateinit var adapterItemsEast: ArrayAdapter<String>;

    private var soundNorth = northItems[0]
    private var soundWest = westItems[0]
    private var soundSouth = southItems[0]
    private var soundEast = eastItems[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val save_button = findViewById<Button>(R.id.save_button)
        save_button.setOnClickListener {
            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
            intent.putExtra("soundNorth", soundNorth)
            intent.putExtra("soundWest", soundWest)
            intent.putExtra("soundSouth", soundSouth)
            intent.putExtra("soundEast", soundEast)
            startActivity(intent)
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

        autoCompleteTextViewNorth = findViewById(R.id.auto_complete_text)
        autoCompleteTextViewWest = findViewById(R.id.auto_complete_text1)
        autoCompleteTextViewSouth = findViewById(R.id.auto_complete_text2)
        autoCompleteTextViewEast = findViewById(R.id.auto_complete_text3)
        adapterItemsNorth = ArrayAdapter<String>(this, R.layout.select_item, northItems)
        adapterItemsWest = ArrayAdapter<String>(this, R.layout.select_item, westItems)
        adapterItemsSouth = ArrayAdapter<String>(this, R.layout.select_item, southItems)
        adapterItemsEast = ArrayAdapter<String>(this, R.layout.select_item, eastItems)
        autoCompleteTextViewNorth.setAdapter(adapterItemsNorth)
        autoCompleteTextViewWest.setAdapter(adapterItemsWest)
        autoCompleteTextViewSouth.setAdapter(adapterItemsSouth)
        autoCompleteTextViewEast.setAdapter(adapterItemsEast)

        autoCompleteTextViewNorth.setText(soundNorth, false)
        autoCompleteTextViewWest.setText(soundWest, false)
        autoCompleteTextViewSouth.setText(soundS, false)
        autoCompleteTextViewEast.setText(soundEast, false)

        autoCompleteTextViewNorth.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                soundNorth = item
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewWest.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                soundWest = item
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewSouth.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                soundSouth = item
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewEast.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                soundEast = item
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }
    }
}