package com.example.plantillaexamen1.ui

class Order(
    val name: String,
    val size: String,
    val milkType: String,
    val extraCinnamon: Boolean,
    val extraChocolate: Boolean,
    val extraMilk: Boolean
) {
    fun calculatePrice(): Int {
        var price = 0
        when (size) {
            "Pequeño" -> price = 15
            "Mediano" -> price = 20
            "Grande" -> price = 25
        }
        if (extraCinnamon) price += 1
        if (extraChocolate) price += 3
        if (extraMilk) price += 2
        return price
    }
}