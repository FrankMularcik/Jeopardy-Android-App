package com.example.jeopardyapp.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jeopardyapp.model.Clue
import com.example.jeopardyapp.model.ResponseType

@Composable
fun ClueCard(clue: Clue, onClick: () -> Unit = {} ) {

    val canClick = clue.response == ResponseType.NotSeen
    val backgroundColor = if(canClick) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 10.dp)
            .fillMaxWidth()
            .then(
                if (canClick) Modifier.clickable { onClick() }
                else Modifier),
        colors = CardDefaults.cardColors(containerColor = backgroundColor))
    {
        Column(modifier = Modifier.padding(horizontal = 2.dp, vertical = 5.dp).fillMaxSize(),
                verticalArrangement =  Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$${clue.dollarAmt}")
        }
    }
}
