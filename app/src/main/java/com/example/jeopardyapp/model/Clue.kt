package com.example.jeopardyapp.model

data class Clue(
    var category: String,
    val dollarAmt: Int,
    val parent: Category,
    var response: ResponseType = ResponseType.NotSeen
)

enum class ResponseType {
    NotSeen,        // haven't seen clue yet
    NoBuzz,         // don't know, wouldn't buzz
    NoBuzz_Correct, // had a guess but wouldn't have buzzed
    Buzz_Correct,   // would buzz and was correct
    Buzz_Incorrect;  // would buzz but was incorrect

    override fun toString(): String {
        return when (this)
        {
            NotSeen -> "Not Seen"
            NoBuzz -> "No Buzz"
            NoBuzz_Correct -> "No Buzz - Correct"
            Buzz_Correct -> "Buzz - Correct"
            Buzz_Incorrect -> "Buzz - Incorrect"
        }
    }
}

// TODO: let user select if it's a daily double?
enum class QuestionType {
    JeopardyNormal,
    JeopardyDailyDouble,
    DoubleJeopardyNormal,
    DoubleJeopardyDailyDouble,
    FinalJeopardy
}

enum class RoundType {
    JeopardyRound,
    DoubleJeopardy,
    FinalJeopardy
}