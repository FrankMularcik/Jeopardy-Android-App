package com.example.jeopardyapp.model

class JeopardyGame {
//air date
    //game type
    var jeopardyRound = Round(RoundType.JeopardyRound)
    var doubleJeopardy = Round(RoundType.DoubleJeopardy)
    var finalJeopardy = Round(RoundType.FinalJeopardy)

    // TODO: add functions or fields for whole game stats

    // TODO: add 'scoring' functionality
}

class Category (var catName: String, var roundType: RoundType, var parent: Round) {
    val clues = mutableListOf<Clue>()

    init{
        if (roundType == RoundType.FinalJeopardy)
        {
            clues.add(Clue(catName, 0, this))
        }
        else
        {
            var dollarMult = 200 // default to jeopardy round but check for double jeopardy round
            if (roundType == RoundType.DoubleJeopardy)
            {
                dollarMult = dollarMult * 2
            }
            for (clue in 1..5)
            {
                clues.add(Clue(catName, clue * dollarMult, this))
            }
        }
    }

    fun CategoryRenamed(newName: String)
    {
        for(clue in clues)
        {
            clue.category = newName
        }
    }
}

class Round(val roundType: RoundType) {
    var categories = mutableListOf<Category>()
        private set
    var allClues: List<Clue> = emptyList()
        private set
    var NUM_CATEGORIES = 0
        private set
    var NUM_CLUES_PER_CATEGORY = 0
        private set

    val totalClues: Int // stores total number of clues in the round
        get() = allClues.size
    private val _statsCounter = mutableMapOf<ResponseType, Int>() // internal variable to hold stats

    val statsCounter: Map<ResponseType, Int> // public property to see stats
        get() = _statsCounter

    init{
        if (roundType == RoundType.FinalJeopardy)
        {
            NUM_CATEGORIES = 1
            NUM_CLUES_PER_CATEGORY = 1
            CreateFinalJeopardyRound()
        }
        else
        {
            NUM_CATEGORIES = 6
            NUM_CLUES_PER_CATEGORY = 5
            CreateMainJeopardyRounds()
        }

        allClues = SetListOfClues()
        InitializeStats()
    }

    // returns all clues left to right then top to bottom to display in jeopardy grid
    private fun SetListOfClues(): List<Clue>
    {
        val grid = mutableListOf<Clue>()

        // TODO: maybe refactor so we get num of rows and columns dynamically, would need to modify grid to use lazy row somehow or transpose to left to right?
        for (row in 1..NUM_CLUES_PER_CATEGORY)
        {
            for (col in 1..NUM_CATEGORIES)
            {
                grid.add(categories[col - 1].clues[row - 1])
            }
        }

        return grid
    }

    private fun CreateFinalJeopardyRound()
    {
        categories.add(Category("Category 1", roundType, this))
    }

    private fun CreateMainJeopardyRounds()
    {
        for (category in 1..6) {
            categories.add(Category("Category $category", roundType, this))
        }
    }

    private fun InitializeStats()
    {
        // initialize all response types to 0
        for (responseType in ResponseType.entries)
        {
            _statsCounter[responseType] = 0
        }

        for(clue in allClues)
        {
            // TODO: probably a more efficient way to do this than using a nested for loop
            for (responseType in ResponseType.entries)
            {
                if (clue.response == responseType)
                {
                    _statsCounter[responseType] = _statsCounter.getOrDefault(responseType, 0) + 1
                }
            }
        }
    }

    public fun ClueAnswered(response: ResponseType)
    {
        _statsCounter[ResponseType.NotSeen] = _statsCounter.getOrDefault(ResponseType.NotSeen, 0) - 1
        _statsCounter[response] = _statsCounter.getOrDefault(response, 0) + 1
    }
}