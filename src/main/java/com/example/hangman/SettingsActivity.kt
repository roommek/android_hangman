package com.example.hangman

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.iterator
import java.util.*

class SettingsActivity : AppCompatActivity() {
    lateinit var language: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val spinner: Spinner = findViewById(R.id.languageSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                language = when (position) {
                    0 -> "English"
                    1 -> "Polish"
                    else -> "English"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val applyButton: Button = findViewById(R.id.applyButton)
        applyButton.setOnClickListener {
            applyChanges()
            finish()
        }
    }

    private fun applyChanges() {
        val returnIntent = Intent()
        returnIntent.putExtra(getString(R.string.languageKey), language)
        setResult(RESULT_OK, returnIntent)
        /* if(language == "Polish") {
            val languageToLoad = "pl" // your language
            val locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            this.setContentView(R.layout.activity_main)
        }
        else {
            val languageToLoad = "en" // your language
            val locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            this.setContentView(R.layout.activity_main)
        } */
    }
}