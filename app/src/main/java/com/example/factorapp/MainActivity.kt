package com.example.factorapp
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.*
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)



        //Text
        val number: TextView = findViewById(R.id.Number)
        val score: TextView = findViewById(R.id.Score)
        val highscore: TextView = findViewById(R.id.Highscore)
        val input: EditText = findViewById(R.id.Input)
        //Buttons
        val start: Button = findViewById(R.id.Start)
        val option1: Button = findViewById(R.id.Option1)
        val option2: Button = findViewById(R.id.Option2)
        val option3: Button = findViewById(R.id.Option3)
        val buttons = arrayOf(option1, option2, option3)
        val screen: ConstraintLayout = findViewById(R.id.Screen)
        val toast = Toast.makeText(applicationContext, "NEW HIGHSCORE!!", Toast.LENGTH_SHORT)
        val etoast = Toast.makeText(applicationContext, "Invalid Input", Toast.LENGTH_SHORT)


        var num = 0
        var set = false
        var scoreVal = 0
        var toastdone = false
        var answer = 0
        var validnumber = false
        start.setBackgroundResource(R.color.Start)
        screen.setBackgroundResource(R.color.Bg)
        for (i in 0..2)
        {
            buttons[i].setBackgroundResource(R.color.Button)
        }

        //Start
        val sharedPreference: SharedPreferences =  getSharedPreferences("Highscore",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        number.visibility = View.INVISIBLE
        highscore.text = "HS: "+sharedPreference.getInt("highscore", 0 ).toString()

        //Animator
        var from = FloatArray(3)
        var to = FloatArray(3)

        val anim = ValueAnimator.ofFloat(0f, 1f) // animate from 0 to 1
        anim.duration = 300 // for 300 ms

        val hsv = FloatArray(3)
        anim.addUpdateListener { animation -> // Transition along each axis of HSV (hue, saturation, value)
            hsv[0] =
                from[0] + (to[0] - from[0]) * animation.animatedFraction
            hsv[1] =
                from[1] + (to[1] - from[1]) * animation.animatedFraction
            hsv[2] =
                from[2] + (to[2] - from[2]) * animation.animatedFraction
            screen.setBackgroundColor(Color.HSVToColor(hsv))
        }

        //Func
        fun numcheck(){
            var end = (num/2).toInt()

            for (i in 2..end)
            {
                if (num % i == 0) {
                    validnumber = true
                }
            }
            if (num == 4){
                validnumber = false
            }
        }
        fun hide(){
            start.visibility = View.INVISIBLE
        }
        fun reset(){
            input.visibility = View.VISIBLE
            number.visibility = View.INVISIBLE
            start.visibility = View.VISIBLE
            for (i in 0..2)
            {
                var target = buttons[i].text.toString().toInt()
                if (num % target == 0) {
                    buttons[i].setBackgroundResource(R.color.BtnGreen)
                }
            }
            if(scoreVal > sharedPreference.getInt("highscore", 0 ) )
            {

                editor.putInt("highscore", scoreVal)
                editor.commit()
                highscore.text = "HighScore: " + scoreVal.toString()
                if(!toastdone){
                    toast.show()
                    toastdone = true
                }
            }
            set = false
        }
        fun logic(){
            val rand = (0..2).random()
            var prand = 0
            for (i in 0..2) {
                if (i == rand) {
                    while (!set) {
                        var end = num-1
                        var trand = (2..end).random()
                        if (num % trand == 0) {
                            set = true
                            buttons[i].text = trand.toString()
                        }
                    }
                }
                else{
                    var end = num - 1
                    var trand = (1..end).random()
                    while (num % trand == 0 || (prand == trand && num > 4) )
                    {
                        trand = (1..end).random()
                    }
                    buttons[i].text = trand.toString()
                    prand = trand
                }
            }

        }
        fun disable() {
            for (i in 0..2)
            {
                buttons[i].setOnClickListener(null)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun btnstart(){
            for (i in 0..2)
            {
                buttons[i].setOnClickListener {


                    var target = buttons[i].text.toString().toInt()
                    if (num % target == 0) {
                        buttons[i].setBackgroundResource(R.color.BtnGreen)
                        screen.setBackgroundResource(R.color.BgGreen)
                        answer = 1
                        scoreVal += 1
                    }
                    else{
                        buttons[i].setBackgroundResource(R.color.BtnRed)
                        screen.setBackgroundResource(R.color.BgRed)
                        answer = 2
                        scoreVal = 0
                        toastdone = false
                    }
                    disable()
                    reset()
                    score.text = scoreVal.toString()
                }
            }
        }


        start.setOnClickListener {
            var enum = input.text.toString()
            if (!TextUtils.isEmpty(enum)){
                num = input.text.toString().toInt()
                validnumber = false
                numcheck()
                if (validnumber) {
                    number.text = num.toString()
                    input.visibility = View.INVISIBLE
                    number.visibility = View.VISIBLE

                    if (answer == 1) {
                        Color.colorToHSV(Color.parseColor("#00E676"), from) // from green
                        Color.colorToHSV(Color.parseColor("#303030"), to)
                        anim.start()
                    } else if (answer == 2) {
                        Color.colorToHSV(Color.parseColor("#F44336"), from) // from red
                        Color.colorToHSV(Color.parseColor("#303030"), to)
                        anim.start()
                    }

                    for (i in 0..2) {
                        buttons[i].setBackgroundResource(R.color.Button)
                        buttons[i].text = "0"
                    }


                    logic()
                    btnstart()
                    hide()
                }
                else{
                    etoast.show()
                }
            }

        }

    }
}