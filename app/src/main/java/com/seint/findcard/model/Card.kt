package com.seint.findcard.model

import java.util.*

/**
 * Created by Seint San Thandar Bo on 12/5/20.
 */
data class Card(val identifier : Int = getUniqueIdentifier() )  {
    var isFaceUp = false
    var isMatched = false

    private companion object Factory {
        var identifierFactory : Int = 0
       fun getUniqueIdentifier () : Int {
            identifierFactory += 1
           return identifierFactory
       }
    }

}
