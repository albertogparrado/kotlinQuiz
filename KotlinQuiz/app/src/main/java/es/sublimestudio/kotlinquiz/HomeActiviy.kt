package es.sublimestudio.kotlinquiz

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import es.sublimestudio.kotlinquiz.databinding.ActivityHomeActiviyBinding
import es.sublimestudio.kotlinquiz.databinding.ActivityMainBinding

class HomeActiviy : AppCompatActivity() {

    val DBName = "KotlinQuiz"
    val USERNAME = "username"
    val TAG = "HomeActiviy"

    private lateinit var b: ActivityHomeActiviyBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeActiviyBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        sharedPref = getSharedPreferences(DBName, Context.MODE_PRIVATE)

        val name = sharedPref.getString(USERNAME, null)
        if(name != null) {
            showGame(name)
        }

        b.btnContine.setOnClickListener {
           checkName()
        }

        b.btnChange.setOnClickListener {
            requestName()
        }

        b.btnPlay.setOnClickListener {
            playGame()
        }
    }

    fun showGame(name: String) {
        b.linearGame.visibility = View.VISIBLE
        b.linearHome.visibility = View.GONE
        b.txtUsername.text = "¡Hola $name!"
    }

    fun requestName() {
        b.linearGame.visibility = View.GONE
        b.linearHome.visibility = View.VISIBLE
    }

    fun playGame() {
        startActivity(Intent(this, QuizActivity::class.java))
        finish()
    }

    fun checkName() {
        val nameInput = b.edName.text.toString()
        if(nameInput.length > 3) {

            val edit = sharedPref.edit()
            edit.putString(USERNAME, nameInput)
            edit.apply()

            playGame()

        } else {
            Toast.makeText(this, "Introduce un nombre válido", Toast.LENGTH_LONG).show()
        }
    }
}