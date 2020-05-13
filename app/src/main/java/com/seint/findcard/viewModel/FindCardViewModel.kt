package com.seint.findcard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seint.findcard.model.Card
import kotlin.collections.ArrayList

/**
 * Created by Seint San Thandar Bo on 12/5/20.
 */
class FindCardViewModel () :ViewModel(){

    private var lastTryUnmatched: Boolean = false
    var cards : MutableLiveData<ArrayList<Card>> = MutableLiveData<ArrayList<Card>>()

    var numberChoices : MutableLiveData<ArrayList<Int>> = MutableLiveData<ArrayList<Int>>()
    var numberOfSteps : MutableLiveData<Int> = MutableLiveData<Int>()
    var numberCard  :MutableLiveData<MutableMap<Card,String>> = MutableLiveData<MutableMap<Card,String>>()

    var numberOfPairsOfCards :Int = 8
    var indexOfOneAndOnlyFaceUpCard : Int
     get() {

         val faceUpCard =  cards.value?.filter {
             it -> it.isFaceUp && !it.isMatched
         }
        if(faceUpCard?.size  == 1 ){
            val oneAndOnly =faceUpCard.get(0)
            return cards.value!!.indexOfFirst { it.identifier == oneAndOnly.identifier && it.isFaceUp == oneAndOnly.isFaceUp}
        }else{
            return -1
        }
     }
    set(value) {

        for (index in cards.value?.indices!!) {
            if((cards.value?.get(index) as Card).isMatched) {
                (cards.value?.get(index) as Card).isFaceUp = true
            }else{
                (cards.value?.get(index) as Card).isFaceUp =(index == value)
            }
        }

    }

    init {
       initData()

    }
    fun initData(){
        numberOfSteps.value = 0
        val randomNuList: MutableSet<Int> = mutableSetOf()
        while (randomNuList.size < numberOfPairsOfCards){
            randomNuList.add((1..99).random())
        }
        val unchangable = randomNuList
        numberChoices = MutableLiveData<ArrayList<Int>>()
        numberChoices.value =unchangable.toList() as ArrayList<Int>


        var cards = ArrayList<Card>()
        for ( i in 1..numberOfPairsOfCards){
            var card = Card()
            cards.add(card)
            cards.add(card.copy())
        }
        cards.shuffle();
        this.cards.value = cards
    }

    fun lastTryUnmatched() :Boolean{
        if(lastTryUnmatched){
            indexOfOneAndOnlyFaceUpCard = -1
        }
        return lastTryUnmatched


    }
    fun chooseCard( index : Int){
        lastTryUnmatched = false

        if( cards.value?.get(index)?.isMatched == false) {

            val indexOfOnlyFaceUp =  indexOfOneAndOnlyFaceUpCard
            if( indexOfOnlyFaceUp > -1  && indexOfOnlyFaceUp != index ){
                if ( cards.value?.get(indexOfOnlyFaceUp) ==  cards.value?.get(index)) {
                    (cards.value?.get(indexOfOnlyFaceUp) as Card).isMatched = true
                    (cards.value?.get(index) as Card).isMatched = true
                }else {
                    lastTryUnmatched = true
                }
                (cards.value?.get(index) as Card).isFaceUp = true
            }else{
                indexOfOneAndOnlyFaceUpCard = index
            }
        }
    }
}

