package es.sublimestudio.kotlinquiz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import es.sublimestudio.kotlinquiz.DataHolder
import es.sublimestudio.kotlinquiz.HomeActiviy
import es.sublimestudio.kotlinquiz.R
import es.sublimestudio.kotlinquiz.databinding.ActivityHomeActiviyBinding
import es.sublimestudio.kotlinquiz.databinding.FragmentQuestionsBinding
import es.sublimestudio.kotlinquiz.models.Game
import io.paperdb.Paper
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


class ReviewFragment : Fragment() {

    val DBName = "KotlinQuiz"
    val USERNAME = "username"
    private  lateinit var b: ActivityHomeActiviyBinding
    lateinit var sharedPref: SharedPreferences

    val viewKonfetti = view?.findViewById<KonfettiView>(R.id.viewKonfetti)


    val nombre = sharedPref.getString(USERNAME, "")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val title = view.findViewById<TextView>(R.id.txtTitle)
        val emoji = view.findViewById<TextView>(R.id.txtEmoji)
        val btnPlay = view.findViewById<Button>(R.id.btnPlayAgain)
        val btnScore = view.findViewById<Button>(R.id.btnSeeScore)
        val btnChangeUser = view.findViewById<Button>(R.id.btnChange)

        val points = DataHolder.points

        if(points > 20000){
            title.text = "¡Eres un crack! \n Has conseguido ${points} puntos"
            //showKonfetti(viewKonfetti!!)
        }else{
            title.text = "¡Eres un manta! \n Sólo tienes ${points} puntos"
            emoji.text = "🤣 👎"
        }

        btnPlay.setOnClickListener(){
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainContainer, QuestionsFragment()).commit()
        }

        btnChangeUser.setOnClickListener(){

            //Cambiar usuario
            sharedPref = activity?.getSharedPreferences(DBName, Context.MODE_PRIVATE)!!

            val edit = sharedPref.edit()
            edit.putString(USERNAME, null)
            edit.apply()

            //Enviar al HomeActivity
            requireActivity().startActivity(Intent(context, HomeActiviy::class.java))
            requireActivity().finish()


        }

        val misPartidas = Paper.book().read<ArrayList<Game>>(DataHolder.KEYGAMES)
        Log.v("DALE", misPartidas.toString())


        misPartidas.add(Game(nombre ?: "", points))

        Paper.book().write(DataHolder.KEYGAMES, misPartidas)

    }

    fun showKonfetti(viewKonfetti: KonfettiView){
        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)

    }

}