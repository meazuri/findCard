package com.seint.findcard.views

import android.content.Context

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seint.findcard.R
import com.seint.findcard.model.Card
import com.seint.findcard.viewModel.FindCardViewModel

/**
 * Created by Seint San Thandar Bo on 12/5/20.
 */
class CardGameAdapter (private val context: Context, val game: FindCardViewModel, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<CardGameAdapter.MyViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    private var cardList: List<Card> = mutableListOf()
    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tvCard = itemView?.findViewById<TextView>(R.id.tvNumber)
        var dataPosition = 0
        init {
            itemView?.setOnClickListener {

                game.numberOfSteps.value = game.numberOfSteps.value?.plus(1)
                val card = cardList.get(dataPosition)
                game.chooseCard(dataPosition)
                getCardText(card)
                notifyDataSetChanged()
                checkLastTryUnmatched()

                itemClickListener.onItemClicked(card)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = layoutInflater.inflate(R.layout.card_list_item,parent,false)
        return  MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {

       return cardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = cardList.get(position)

        if(card.isFaceUp) {
            holder.tvCard?.text =  game.numberCard.value?.get(card)
            holder.tvCard?.setTextColor(context.resources.getColor(R.color.background))

            holder.tvCard?.background=  context.resources.getDrawable(R.drawable.card_bg_white)
        }else{
            holder.tvCard?.background = context.resources.getDrawable(R.drawable.card_border)
            holder.tvCard?.setTextColor(context.resources.getColor(R.color.white))
            holder.tvCard?.text = "?"


        }
        holder.dataPosition = position


    }
    fun  isGameOver() : Boolean {
        return  cardList.all { card -> card.isMatched
            }
    }
    fun setCardList(cardList : ArrayList<Card>){
        this.cardList = cardList
        notifyDataSetChanged()
    }

    fun getCardText(card: Card): String ?{

        if (game.numberCard.value == null || (game.numberCard.value?.filter { it.key == card }?.isEmpty()!! && game.numberChoices.value?.count()!!> 0)){
            val randomIndex = (game.numberChoices.value?.count()!! -1).Random()
            val cardString = game.numberChoices.value?.get(randomIndex).toString()

            if(game.numberCard.value == null ){
                val map =mutableMapOf<Card,String>()
                map.put(card,cardString)
                game.numberCard.value = map
            }else {
                game.numberCard.value?.put(card, cardString)
            }
            game.numberChoices.value?.removeAt(randomIndex)
        }

        return  game.numberCard.value?.get(card) ?: "?"
    }
    fun checkLastTryUnmatched(){
            val handler = Handler()
            handler.postDelayed(Runnable {
                if(game.lastTryUnmatched()) {
                    notifyDataSetChanged()
                }
            }, 1500)
    }
}

fun  Int.Random()=  (0..this).random()
