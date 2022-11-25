package com.example.tipapp.utils

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if(totalBill>1&&totalBill.toString().isNotEmpty())
        (totalBill*tipPercentage)/100
    else 0.0
}
fun PerPersonBill(
    totalBill:Double,splitby:Int,tipPercentage: Int
): Double
{
val bill= calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage)+totalBill
    return (bill/splitby)
}
