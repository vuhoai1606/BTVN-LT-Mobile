package org.example.btvnkotlin

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.example.btvnkotlin.Week2_01_24.BusinessCard
import org.example.btvnkotlin.Week3_02_03.DiceRoller
import org.example.btvnkotlin.Week4_03_03.TipTime
import org.example.btvnkotlin.Week5_03_10.WoofApp
import org.example.btvnkotlin.Week6_03_17.CupcakeApp
import org.example.btvnkotlin.Week6_03_17.ui.theme.CupcakeTheme

@Composable
@Preview
fun App() {
    // Dòng này dùng cho lab 1, 2, 3, 4
//    MaterialTheme {
//        WoofApp()
//    }

//    // Dòng này dùng cho lab 5
//    WoofApp()
    CupcakeTheme {
        CupcakeApp()
    }
}