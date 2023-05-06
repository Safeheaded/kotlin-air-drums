package com.example.airdrums

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    val northItems: Array<String> = arrayOf("north1", "north2", "north3", "north4")
    val westItems: Array<String> = arrayOf("west1", "west2", "west3", "west4")
    val southItems: Array<String> = arrayOf("south1", "south2", "south3", "south4")
    val eastItems: Array<String> = arrayOf("east1", "east2", "east3", "east4")

    private lateinit var autoCompleteTextViewNorth: AutoCompleteTextView
    private lateinit var autoCompleteTextViewWest: AutoCompleteTextView
    private lateinit var autoCompleteTextViewSouth: AutoCompleteTextView
    private lateinit var autoCompleteTextViewEast: AutoCompleteTextView

    private lateinit var adapterItemsNorth: ArrayAdapter<String>;
    private lateinit var adapterItemsWest: ArrayAdapter<String>;
    private lateinit var adapterItemsSouth: ArrayAdapter<String>;
    private lateinit var adapterItemsEast: ArrayAdapter<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

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

        autoCompleteTextViewNorth.setText(adapterItemsNorth.getItem(0), false)
        autoCompleteTextViewWest.setText(adapterItemsWest.getItem(0), false)
        autoCompleteTextViewSouth.setText(adapterItemsSouth.getItem(0), false)
        autoCompleteTextViewEast.setText(adapterItemsEast.getItem(0), false)

        autoCompleteTextViewNorth.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewWest.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewSouth.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }

        autoCompleteTextViewEast.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }
    }
}