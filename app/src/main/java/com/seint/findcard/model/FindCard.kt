package com.seint.findcard.model

import kotlin.collections.ArrayList

/**
 * Created by Seint San Thandar Bo on 12/5/20.
 */
class FindCard (numberOfPairsOfCards : Int){

    private var lastTryUnmatched: Boolean = false
    var cards : ArrayList<Card>

    var numberChoices : ArrayList<Int>

    var indexOfOneAndOnlyFaceUpCard : Int
     get() {

         val faceUpCard =  cards.filter {
             it -> it.isFaceUp && !it.isMatched
         }
        if(faceUpCard.size  == 1 ){
            val oneAndOnly =faceUpCard.get(0)
            return cards.indexOfFirst { it.identifier == oneAndOnly.identifier && it.isFaceUp == oneAndOnly.isFaceUp}
        }else{
            return -1
        }
     }
    set(value) {
        for (index in cards.indices  ) {
            if(cards[index].isMatched) {
                cards[index].isFaceUp = true
            }else{
                cards[index].isFaceUp =(index == value)
            }
        }

    }

    init {

        val randomNuList: MutableSet<Int> = mutableSetOf()
        while (randomNuList.size < numberOfPairsOfCards){
            randomNuList.add((1..99).random())
        }
        numberChoices = ArrayList<Int>()
        numberChoices.addAll(randomNuList)

        cards = ArrayList()
        for ( i in 1..numberOfPairsOfCards){
            var card = Card()
            cards.add(card)
            cards.add(card.copy())
        }
        cards.shuffle();

    }
    fun lastTryUnmatched() :Boolean{
        if(lastTryUnmatched){
            indexOfOneAndOnlyFaceUpCard = -1
        }
        return lastTryUnmatched


    }
    fun chooseCard( index : Int){
        lastTryUnmatched = false

        if(!cards[index].isMatched) {

            val indexOfOnlyFaceUp =  indexOfOneAndOnlyFaceUpCard
            if( indexOfOnlyFaceUp > -1  && indexOfOnlyFaceUp != index ){
                if (cards[indexOfOnlyFaceUp] == cards[index]) {
                    cards[indexOfOnlyFaceUp].isMatched = true
                    cards[index].isMatched = true
                }else {
                    lastTryUnmatched = true
                }
                cards[index].isFaceUp = true
            }else{
                indexOfOneAndOnlyFaceUpCard = index
            }
        }
    }

}

