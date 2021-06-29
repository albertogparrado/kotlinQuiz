package es.sublimestudio.kotlinquiz.views

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import es.sublimestudio.kotlinquiz.DataHolder
import es.sublimestudio.kotlinquiz.R
import es.sublimestudio.kotlinquiz.databinding.FragmentQuestionsBinding
import es.sublimestudio.kotlinquiz.models.Answer
import es.sublimestudio.kotlinquiz.models.Question

class QuestionsFragment : Fragment() {

    private var _binding: FragmentQuestionsBinding? = null
    private val b get() = _binding!!

    //VARIABLES
    val TIME_MAX = 10000
    val allQuestions: ArrayList<Question> = arrayListOf()
    var pos = 0
    val allButtons: ArrayList<Button> = arrayListOf()

    var currentPoints = TIME_MAX

    lateinit var timer: CountDownTimer



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        val view = b.root
        return view
    }

    fun initData() {

        DataHolder.points = 0

       allQuestions.addAll(DataHolder.questions.questions)

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        b.progressTime.max = TIME_MAX
        b.progressTime.progress = TIME_MAX
        startProgress()

        nextQuestion()

        b.btn1.setOnClickListener {
            checkAnswer(b.btn1)
        }

        b.btn2.setOnClickListener {
            checkAnswer(b.btn2)
        }

        b.btn3.setOnClickListener {
            checkAnswer(b.btn3)
        }

        b.btn4.setOnClickListener {
            checkAnswer(b.btn4)
        }
    }


    /////////////////////////////////  FUNCIONES  ////////////////////////////////////////

    //Barra de progreso
    fun startProgress() {
        timer = object : CountDownTimer(TIME_MAX.toLong(), 50) {

            override fun onTick(millisUntilFinished: Long) {
                currentPoints = millisUntilFinished.toInt()
                b.progressTime.setProgress(millisUntilFinished.toInt(), true)
            }

            override fun onFinish() {
                b.progressTime.setProgress(0, true)
                pos++
                nextQuestion()
            }
        }
        timer.start()
    }


    //Siguiente pregunta ->  cambiar pregunta + botones + asignar TAGs a bt
    fun nextQuestion() {
        if (allQuestions.size > pos) {

            val currQuestion = allQuestions[pos]
            b.titleQuestion.text = currQuestion.title

            b.btn1.text = currQuestion.answers[0].title
            b.btn1.tag = 0

            b.btn2.text = currQuestion.answers[1].title
            b.btn2.tag = 1

            b.btn3.text = currQuestion.answers[2].title
            b.btn3.tag = 2

            b.btn4.text = currQuestion.answers[3].title
            b.btn4.tag = 3

            allButtons.clear()
            allButtons.add(b.btn1)
            allButtons.add(b.btn2)
            allButtons.add(b.btn3)
            allButtons.add(b.btn4)

            resetButtons()

            timer.start()
        } else {
            Log.v("DALE", "Ya no hay más preguntas")
            Toast.makeText(requireContext(), "Has terminado la partida", Toast.LENGTH_LONG).show()

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainContainer, ReviewFragment()).commit()
        }
    }

    //Pregunta correcta -> Al tocar una respuesta -> parar tiempo + comprobar Correcta + cambio color + cancel other bt
    fun checkAnswer(btnSelected: Button) {

        timer.cancel()

        Handler(Looper.getMainLooper()).postDelayed({
            pos++
            nextQuestion()
        }, 2000)

        val currQuestion = allQuestions[pos]
        val isCorrect = currQuestion.answers[btnSelected.tag as Int].isCorrect

        if (isCorrect) {


            DataHolder.points += currentPoints

            btnSelected.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.success
                )
            )

        } else {
            btnSelected.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.failure
                )
            )
        }

        disableButtons(btnSelected)
    }


    //Desactivar bt -> otros bt poner en ver o gris
    fun disableButtons(btnSelected: Button) {
        allButtons.forEach { currentButton ->

            currentButton.isClickable = false

            val currQuestion = allQuestions[pos]
            val isCorrect = currQuestion.answers[currentButton.tag as Int].isCorrect

            if (btnSelected != currentButton) {
                if (isCorrect) {
                    currentButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        )
                    )
                } else {
                    currentButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                }
            }
        }
    }

    //Resetear bt -> poner clickables + color original
    fun resetButtons() {
        allButtons.forEach { button ->
            button.isClickable = true
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}