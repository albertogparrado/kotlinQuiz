package es.sublimestudio.kotlinquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.sublimestudio.kotlinquiz.views.QuestionsFragment

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        supportFragmentManager.beginTransaction().replace(R.id.mainContainer, QuestionsFragment()).commit()
    }
}