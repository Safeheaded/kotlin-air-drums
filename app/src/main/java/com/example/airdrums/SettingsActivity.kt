package com.example.airdrums

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    val items: Array<String> = arrayOf("test1", "test2", "test3", "test4")

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    private lateinit var adapterItems: ArrayAdapter<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        autoCompleteTextView = findViewById(R.id.auto_complete_text)
        adapterItems = ArrayAdapter<String>(this, R.layout.select_item, items)
        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.setText("test1", false)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener() { parent, view, position, id ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Item: $item", Toast.LENGTH_SHORT).show()
            }
    }
}