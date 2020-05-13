package com.seint.findcard

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.seint.findcard.model.Card
import com.seint.findcard.model.FindCard
import com.seint.findcard.views.CardGameAdapter
import com.seint.findcard.views.ItemClickListener
import kotlinx.android.synthetic.main.activity_main.*

class FindCardActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var  mAdapter: CardGameAdapter

    var numberOfSteps : Int =0
    set(value) {
        tvSteps.setText("STEPS : ($value)")
        field = value
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val game = FindCard(6)

        mAdapter = CardGameAdapter(this,game, this)
        mAdapter.setCardList(game.cards)

        val gridLm = GridLayoutManager(this,3)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = gridLm
        recyclerView.isNestedScrollingEnabled = false

        btnRestart.setOnClickListener {
           val game = FindCard(6)
            mAdapter = CardGameAdapter(this,game,this)
            mAdapter.setCardList(game.cards)
            recyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
            numberOfSteps = 0

        }

    }

    fun gameOver (){
        AlertDialog.Builder(this)
            .setTitle("Congratulation")
            .setMessage("You win this game by $numberOfSteps steps!")
            .setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    btnRestart.callOnClick()
                })
            .show()
    }

    override fun onItemClicked(card: Card) {
        numberOfSteps +=1
        if ( mAdapter.isGameOver()){
          gameOver()
        }

    }
}
