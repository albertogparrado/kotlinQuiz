package es.sublimestudio.kotlinquiz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rommansabbir.animationx.Attention
import com.rommansabbir.animationx.Fade
import com.rommansabbir.animationx.animationXAttention
import com.rommansabbir.animationx.animationXFade
import es.sublimestudio.kotlinquiz.databinding.ActivityMainBinding
import es.sublimestudio.kotlinquiz.models.MainQuestions
import io.paperdb.Paper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        DataHolder.questions = readQuiz("questions.json")

        appear()
        disappear()
        goHome()

        Paper.init(this);


    }

    fun readQuiz(routeJSON: String): MainQuestions {
        var json = ""
        try {
            val url = routeJSON
            val bufferedReader = BufferedReader(
                InputStreamReader(assets?.open(url))
            )
            val paramsBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                paramsBuilder.append(line)
                line = bufferedReader.readLine()
            }
            json = paramsBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val data = Gson().fromJson(json, MainQuestions::class.java)
        return data
    }

    /**
     * Animacion TADA de aparicion
     */
    fun appear() {
        b.quizlogo.animationXAttention(Attention.ATTENTION_TA_DA)
    }

    /**
     * Desaparece la imagen
     */
    fun disappear() {
        Handler(Looper.getMainLooper()).postDelayed({
            b.quizlogo.animationXFade(Fade.FADE_OUT)
        }, 2000)
    }

    fun goHome() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActiviy::class.java))
            finish()
        }, 3300)
    }
}