package com.example.jeopardyapp.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jeopardyapp.model.Clue

// make card greyed out if it's already been clicked on / answered
@Composable
fun ClueCard(clue: Clue, onClick: () -> Unit = {} ) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        onClick = onClick)
    {
        Column() {
            Text(text = "$${clue.dollarAmt}")
        }
    }
}
