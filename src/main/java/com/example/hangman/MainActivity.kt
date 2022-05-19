package com.example.hangman

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity(){
    var level : String = "Easy"
    lateinit var stringArray : Array<String>
    private var secret : String = " "
    var secretHidden : String = " "
    var wrongAns : Int = 0
    var goodAns : Int = 0
    var end : Boolean = false
    private val triedChars = arrayListOf<Char>()
    private val triedWords = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //stringArray = resources.getStringArray(R.array.englishWords)

        val spinner: Spinner = findViewById(R.id.languageSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                level = when (position) {
                    0 -> "Easy"
                    1 -> "Medium"
                    2 -> "Hard"
                    else -> "Easy"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val play : FloatingActionButton = findViewById(R.id.playButton)
        play.setOnClickListener {
            wrongAns = 0
            goodAns = 0
            end = false
            triedChars.clear()
            triedWords.clear()
            secret = getRandomWord()
            val stringBuilder = StringBuilder(secret)
            for(i in secret.indices) stringBuilder.setCharAt(i, '_')
            val secretTextView : TextView = findViewById(R.id.secret)
            secretTextView.text = stringBuilder
            secretHidden = stringBuilder.toString()
            modifyImage()
        }

        val checkLetter : Button = findViewById(R.id.letterCheckButton)
        checkLetter.setOnClickListener {
            if(end || secret == " ") {
                if(end) Snackbar.make(it, R.string.end, Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(it, R.string.startGame, Snackbar.LENGTH_SHORT).show()
            }
            else {
                val guessText: EditText = findViewById(R.id.letterEdit)
                if (guessText.text.length != 1) Snackbar.make(
                    it,
                    R.string.wrongLetterLength,
                    Snackbar.LENGTH_SHORT
                ).show()
                else {
                    if (triedChars.contains(guessText.text[0])) {
                        Snackbar.make(it, R.string.alreadyTriedLetter, Snackbar.LENGTH_SHORT).show()
                    } else if (secret.contains(guessText.text)) {
                        triedChars?.add(guessText.text[0])
                        modifySecretWord(guessText.text)
                        if(goodAns == secret.length) {
                            end = true
                            Snackbar.make(it, R.string.findWord, Snackbar.LENGTH_SHORT).show()
                        }
                        else Snackbar.make(it, R.string.goodLetter, Snackbar.LENGTH_SHORT).show()
                    } else {
                        triedChars?.add(guessText.text[0])
                        wrongAns += 1
                        modifyImage()
                        if(wrongAns == 10) {
                            end = true
                            var myString = resources.getString(R.string.loose)
                            myString = myString.plus(" ").plus(secret)
                            Snackbar.make(it, myString, Snackbar.LENGTH_SHORT).show()
                        }
                        else Snackbar.make(it, R.string.wrongLetter, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val checkWord : Button = findViewById(R.id.wordCheckButton)
        checkWord.setOnClickListener{
            if(end || secret == " ") {
                if(end) Snackbar.make(it, R.string.end, Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(it, R.string.startGame, Snackbar.LENGTH_SHORT).show()
            }
            else {
                val guessText: EditText = findViewById(R.id.letterEdit)

                if (guessText.text.length != secret.length) Snackbar.make(
                    it,
                    R.string.wrongWordLength,
                    Snackbar.LENGTH_SHORT
                ).show()
                else {
                    if (triedWords.contains(guessText.text.toString())) {
                        Snackbar.make(it, R.string.alreadyTriedWord, Snackbar.LENGTH_SHORT).show()
                    } else if (secret == guessText.text.toString()) {
                        end = true
                        val secretTextView : TextView = findViewById(R.id.secret)
                        secretTextView.text = secret
                        Snackbar.make(it, R.string.goodWord, Snackbar.LENGTH_SHORT).show()
                    }
                    else {
                        triedWords.add(guessText.text.toString())
                        wrongAns += 1
                        modifyImage()
                        if(wrongAns == 10) {
                            end = true
                            var myString = resources.getString(R.string.loose)
                            myString = myString.plus(" ").plus(secret)
                            Snackbar.make(it, myString, Snackbar.LENGTH_SHORT).show()
                        }
                        else Snackbar.make(it, R.string.wrongWord, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private var language : String = "English"
    private val launchSettingsActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            language = result.data?.getStringExtra(getString(R.string.languageKey)) ?: "English"
            var currLanguage = resources.getString(R.string.languageChange)
            currLanguage = currLanguage.plus(" ").plus(language)
            Snackbar.make(
                findViewById(R.id.letterEdit),
                currLanguage,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun modifyImage() {
        val hangmanView: ImageView = findViewById(R.id.imageView);
        when(wrongAns) {
            0 -> hangmanView.setImageResource(R.drawable.hangman0)
            1 -> hangmanView.setImageResource(R.drawable.hangman1)
            2 -> hangmanView.setImageResource(R.drawable.hangman2)
            3 -> hangmanView.setImageResource(R.drawable.hangman3)
            4 -> hangmanView.setImageResource(R.drawable.hangman4)
            5 -> hangmanView.setImageResource(R.drawable.hangman5)
            6 -> hangmanView.setImageResource(R.drawable.hangman6)
            7 -> hangmanView.setImageResource(R.drawable.hangman7)
            8 -> hangmanView.setImageResource(R.drawable.hangman8)
            9 -> hangmanView.setImageResource(R.drawable.hangman9)
            10 -> {
                hangmanView.setImageResource(R.drawable.hangman10)
                Snackbar.make(findViewById(R.id.letterCheckButton), R.string.loose, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun getRandomWord():String{
        when(language) {
            "English" -> stringArray = resources.getStringArray(R.array.englishWords)
            "Polish" -> stringArray = resources.getStringArray(R.array.polishWords)
        }
        var number = (0..stringArray.size).random()
        while(true){
            number = if(level == "Easy" && stringArray[number].length > 4) {
                (0..stringArray.size).random()
            } else if(level == "Medium" && (stringArray[number].length < 5 || stringArray[number].length > 9)) {
                (0..stringArray.size).random()
            } else if(level == "Hard" && stringArray[number].length < 9) {
                (0..stringArray.size).random()
            } else break
        }
        return stringArray[number]
    }

    private fun modifySecretWord(text: Editable) {
        var index : Int = secret.indexOf(text[0],0)
        val stringBuilder = StringBuilder(secretHidden)
        while(index >= 0) {
            goodAns += 1
            stringBuilder.setCharAt(index, text[0])
            index = secret.indexOf(text[0],index+1)
        }
        val secretTextView : TextView = findViewById(R.id.secret)
        secretTextView.text = stringBuilder
        secretHidden = stringBuilder.toString()
    }

    override fun onCreateOptionsMenu(menu : Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingsItem -> startSettingsActivity()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra(getString(R.string.languageKey), language)
        launchSettingsActivity.launch(intent)
    }
}