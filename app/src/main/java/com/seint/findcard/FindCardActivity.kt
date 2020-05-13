package com.seint.findcard

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.seint.findcard.model.Card
import com.seint.findcard.viewModel.FindCardViewModel
import com.seint.findcard.views.CardGameAdapter
import com.seint.findcard.views.ItemClickListener
import kotlinx.android.synthetic.main.activity_main.*

class FindCardActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var  mAdapter: CardGameAdapter

    lateinit var  findCard: FindCardViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findCard = ViewModelProviders.of(this).get(FindCardViewModel::class.java)
        findCard.cards.observe(this, Observer {
            mAdapter.setCardList(it)
            recyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        })
        mAdapter = CardGameAdapter(this,findCard, this)

        val gridLm = GridLayoutManager(this,3)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = gridLm
        recyclerView.isNestedScrollingEnabled = false

        btnRestart.setOnClickListener {
            findCard.initData()
        }

        findCard.numberOfSteps.observe(this, Observer {
            tvSteps.setText("STEPS : ($it)")
        })

    }

    fun gameOver (){
        val totalSteps = findCard.numberOfSteps.value
        AlertDialog.Builder(this)
            .setTitle("Congratulation")
            .setMessage("You win this game by $totalSteps steps!")
            .setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    btnRestart.callOnClick()
                })
            .show()
    }

    override fun onItemClicked(card: Card) {
        if ( mAdapter.isGameOver()){
          gameOver()
        }

    }
}
