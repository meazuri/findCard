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
import com.seint.findcard.model.FindCard

/**
 * Created by Seint San Thandar Bo on 12/5/20.
 */
class CardGameAdapter (private val context: Context ,val game:FindCard ,val itemClickListener: ItemClickListener) : RecyclerView.Adapter<CardGameAdapter.MyViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)

    private var numberCard = mutableMapOf<Card,String>()
    private var flipCount = 0


    private var cardList: List<Card> = mutableListOf()
    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tvCard = itemView?.findViewById<TextView>(R.id.tvNumber)
        var dataPosition = 0
        init {
            itemView?.setOnClickListener {
                flipCount += 1

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
            holder.tvCard?.text =  numberCard.get(card)
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
        if (numberCard.filter { it.key == card }.isEmpty() && game.numberChoices.count()> 0){
            val randomIndex = (game.numberChoices.count() -1).Random()
            val cardString = game.numberChoices.get(randomIndex).toString()
            numberCard.put(card,cardString)
            game.numberChoices.removeAt(randomIndex)
        }

        return  numberCard.get(card) ?: "?"
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
