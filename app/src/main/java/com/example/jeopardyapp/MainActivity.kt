package com.example.jeopardyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.jeopardyapp.ui.theme.JeopardyAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.jeopardyapp.model.JeopardyViewModel
import com.example.jeopardyapp.model.Round
import com.example.jeopardyapp.ui.ClueCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jeopardyapp.model.ResponseType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JeopardyAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val jeopardyViewModel: JeopardyViewModel = viewModel()

    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") { HomeScreen(navController) }
        composable("RoundsScreen") { RoundsScreen(navController, jeopardyViewModel) }
        composable(
            route = "JeopardyBoard/{round}",
            arguments = listOf(navArgument("round") { type = NavType.IntType })
        ) { backStackEntry ->
            val round = backStackEntry.arguments?.getInt("round") ?: 0
            JeopardyBoard(navController = navController, roundNum = round, jeopardyViewModel)
        }
        composable("ClueScreen") { ClueScreen(navController, jeopardyViewModel) }
        composable(
            route = "StatsScreen/{allRounds}",
            arguments = listOf(navArgument("allRounds") {
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val allRounds = backStackEntry.arguments?.getBoolean("allRounds") ?: false
            StatsScreen(navController, jeopardyViewModel, allRounds)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            // add intermediary screen that lets user enter air date and game type (tournament or not)
          //  jeopardyGame = JeopardyGame()
            navController.navigate("RoundsScreen") }) { Text("New Game") }
    }
}

@Composable
fun RoundsScreen(navController: NavController, viewModel: JeopardyViewModel) {
    Column(modifier = Modifier.fillMaxSize())
    {
        Row(
            modifier = Modifier.fillMaxWidth().padding(PaddingValues(horizontal = 8.dp, vertical = 36.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navController.navigate("JeopardyBoard/1") },
                    modifier = Modifier.padding(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                // , modifier = Modifier.weight(1f).height(100.dp)
            ) {
                Text(text = "Jeopardy Round", softWrap = true)
            }

            Button(onClick = { navController.navigate("JeopardyBoard/2") },
                modifier = Modifier.padding(4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(text = "Double Jeopardy", softWrap = true)
            }
            Button(onClick = { navController.navigate(("JeopardyBoard/3")) },
                modifier = Modifier.padding(4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(text = "Final Jeopardy", softWrap = true)
            }
        }

        Button(onClick = {
            // TODO: show stats on stats screen

            navController.navigate(("StatsScreen/true"))
        },
            modifier = Modifier.padding(4.dp)
        )
        {
            Text("Game Over, See Stats")
        }

        Button(onClick = {
            // TODO: reset jeopardyGame object

            navController.navigate(("HomeScreen"))
        },
            modifier = Modifier.padding(4.dp)
        )
        {
            Text("Exit Without Saving")
        }
    }
}


@Composable
fun JeopardyBoard(navController: NavController, roundNum: Int, viewModel: JeopardyViewModel) {

    var round: Round? = null
    if (roundNum == 1)
    {
        round = viewModel.jeopardyGame.jeopardyRound
    }
    else if (roundNum == 2)
    {
        round = viewModel.jeopardyGame.doubleJeopardy
    }
    else if (roundNum == 3)
    {
        round = viewModel.jeopardyGame.finalJeopardy
    }

    if(round == null)
    {   // error do something here
        return
    }

    viewModel.selectedRound = round
    val categories = round.categories
    val clues = round.allClues

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(round.NUM_CATEGORIES), modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
            items(/*round?.categories ?: emptyList()*/ categories) {category -> Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {Text(text = category.catName)}}
            items(/*round?.GetAllClues() ?: emptyList()*/ clues) { clue -> ClueCard(clue = clue, onClick = {
                viewModel.selectedClue = clue
                navController.navigate("ClueScreen")
            }) }
        }

        // TODO: when round is over we should show stats for end of this round only
        Button(onClick = { navController.navigate(("StatsScreen/false")) }
        ) {
            Text("Round Over.")
        }
    }
}

@Composable
fun ClueScreen(navController: NavController, viewModel: JeopardyViewModel) {

    fun handleResponse(response: ResponseType)
    {
        viewModel.selectedClue?.response = response
        viewModel.selectedRound?.ClueAnswered(response)
        navController.popBackStack()
    }

    // TODO: create lazyverticalgrid with 2 columns.
    //  use item{ Button code } block to manually add each button

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
//    Row( modifier = Modifier.fillMaxWidth().padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
            item {
                Button(
                    onClick = { handleResponse(ResponseType.NoBuzz) },
                    modifier = Modifier.padding(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                    // , modifier = Modifier.weight(1f).height(100.dp)
                ) {
                    Text("No Buzz")
                }
            }

            item {
                Button(
                    onClick = { handleResponse(ResponseType.NoBuzz_Correct) },
                    modifier = Modifier.padding(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("No Buzz - Correct")
                }
            }

            item {
                Button(
                    onClick = { handleResponse(ResponseType.Buzz_Correct) },
                    modifier = Modifier.padding(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Buzz - Correct")
                }
            }

            item {
                Button(
                    onClick = { handleResponse(ResponseType.Buzz_Incorrect) },
                    modifier = Modifier.padding(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Buzz - Incorrect")
                }
            }
        }
    }
}

// TODO: make it optional to give a round in which case we only display one round or if none passed, display all three
@Composable
fun StatsScreen(navController: NavController, viewModel: JeopardyViewModel, allRounds: Boolean) {
    // display number of questions, and all of 5 response types with numbers and % (for each round)

    val roundType = viewModel.selectedRound.roundType

    fun navigateToNextScreen()
    {
        var screenName = "RoundsScreen" // go back to RoundsScreen so user can pick next round
        if (allRounds) // we are done with the game
        {
            // TODO: save jeopardyGame object / stats to database
            screenName = "HomeScreen" // game is over - go to home so user can start a new game if they want
        }
        navController.navigate(screenName)
    }
    val jeopardyRound = viewModel.jeopardyGame.jeopardyRound
    val doubleJeopardyRound = viewModel.jeopardyGame.doubleJeopardy
    val finalJeopardyRound = viewModel.jeopardyGame.finalJeopardy

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 20.dp)) {

        if (allRounds || roundType == jeopardyRound.roundType)
        {
            RoundStats(jeopardyRound)
        }
        if (allRounds || roundType == doubleJeopardyRound.roundType)
        {
            RoundStats(doubleJeopardyRound)
        }
        if (allRounds || roundType == finalJeopardyRound.roundType)
        {
            RoundStats(finalJeopardyRound)
        }

        if (allRounds)
        {
            // TODO: show full game stats
        }
        // TODO: show projected score

        Button(
            onClick = { navigateToNextScreen() },
            modifier = Modifier.padding(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text("Continue")
        }
    }

}

@Composable
fun RoundStats(round: Round)
{
    // TODO: improve formatting / make prettier

    Text(round.roundType.toString())
    LazyVerticalGrid(
        columns = GridCells.Fixed(ResponseType.entries.size + 1),
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
    ) {
        item { Text("Total Clues") }
        items(ResponseType.entries) { entry -> Text(entry.toString()) }

        item { Text(round.totalClues.toString()) }
        items(round.statsCounter.entries.toList()) { stat ->
            Text(
                String.format(
                    "%d (%.2f%%)",
                    stat.value,
                    100.0 * stat.value / round.totalClues
                )
            )
        }
    }
}
