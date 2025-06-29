package com.example.jeopardyapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScoreCard(score: Int)
{
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth())
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Your Score: $${score}")
        }
    }
}