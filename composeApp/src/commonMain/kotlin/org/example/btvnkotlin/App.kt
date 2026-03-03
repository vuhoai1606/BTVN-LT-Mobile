package org.example.btvnkotlin

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.example.btvnkotlin.Week2_01_24.BusinessCard
import org.example.btvnkotlin.Week3_02_03.DiceRoller
import org.example.btvnkotlin.Week4_03_03.TipTime


@Composable
@Preview
fun App() {
    MaterialTheme {
        TipTime()
    }
}