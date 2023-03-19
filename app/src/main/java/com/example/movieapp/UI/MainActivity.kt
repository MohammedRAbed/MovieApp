package com.example.movieapp.UI

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animateShowingDetails()
    }

    private fun animateShowingDetails() {
        myCard.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        myCard.setOnClickListener {
            card_des.visibility =
                if (card_des.visibility==View.GONE)
                    View.VISIBLE
                else
                    View.GONE
            TransitionManager.beginDelayedTransition(myCard,AutoTransition())
        }
    }

}