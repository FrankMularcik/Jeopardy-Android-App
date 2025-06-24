package com.example.jeopardyapp.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class JeopardyViewModel : ViewModel() {
   // var jeopardyGame: JeopardyGame? by mutableStateOf(null)
    var jeopardyGame = JeopardyGame()
    var selectedClue: Clue? by mutableStateOf(null)
    var selectedRound: Round = jeopardyGame.jeopardyRound
}