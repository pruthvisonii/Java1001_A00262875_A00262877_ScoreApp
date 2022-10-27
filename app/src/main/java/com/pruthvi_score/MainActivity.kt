package com.pruthvi_score

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity(), View.OnClickListener {
    //variables to hold Runs and Wickets scored by both teams
    private var teamOneRun = 0
    private var teamOneWicket = 0
    private var teamTwoRun = 0
    private var teamTwoWicket = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Declaring the buttons
        val teamOneUp = findViewById<Button>(R.id.TeamOneIncrease)
        val teamOneDown = findViewById<Button>(R.id.TeamOneReduce)
        val teamTwoUp = findViewById<Button>(R.id.TeamTwoIncrease)
        val teamTwoDown = findViewById<Button>(R.id.TeamTwoReduce)
        val newGame = findViewById<Button>(R.id.newGame)

        //calling update screen to update the button status as per the opening values
        updateScreen()

        //Events for the buttons. They are called when user clicks on the buttons
        teamOneUp.setOnClickListener(this)
        teamOneDown.setOnClickListener(this)
        teamTwoUp.setOnClickListener(this)
        teamTwoDown.setOnClickListener(this)
        newGame.setOnClickListener(this)

        //Event is triggered when any of the radiobutton is selected, it calls the update screen method
        val scoringOptions = findViewById<RadioGroup>(R.id.ScoringOptions)
        scoringOptions.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int -> updateScreen() }
    }

    override fun onClick(view: View) {
        //integer array to hold run and wicket value from the TextView
        var runsAndWicket: IntArray

        //fetching the score for team one from TextView
        val teamOneScore = findViewById<TextView>(R.id.TeamOneScore)
        //fetching the run and wicket value from the TextView
        runsAndWicket = getRunAndWickets(teamOneScore.text.toString())
        //Run value is stored in the first array position of runsAndWicket
        teamOneRun = runsAndWicket[0]
        //If wicket is equal to 10, the score will only have runs value - hence the condition
        //If wicket value is available, it will be there on the second array position of runsAndWicket
        if (runsAndWicket.size > 1) {
            teamOneWicket = runsAndWicket[1]
        }

        //fetching the score for team two from TextView
        val teamTwoScore = findViewById<TextView>(R.id.TeamTwoScore)
        //fetching the run and wicket value from the TextView
        runsAndWicket = getRunAndWickets(teamTwoScore.text.toString())
        //Run value is stored in the first array position of runsAndWicket
        teamTwoRun = runsAndWicket[0]
        //If wicket is equal to 10, the score will only have runs value - hence the condition
        //If wicket value is available, it will be there on the second array position of runsAndWicket
        if (runsAndWicket.size > 1) {
            teamTwoWicket = runsAndWicket[1]
        }

        //scoreValue is fetched as per the radiobutton selected by the user
        val scoreValue = scoringValue
        when (view.id) {
            R.id.TeamOneIncrease ->                 //if user selects wicket radiobutton then the scoreValue will be -1
                if (scoreValue != -1) {
                    //increment team one run by the scoreValue
                    teamOneRun = teamOneRun + scoreValue
                } else {
                    //increment team one wicket by 1
                    teamOneWicket = teamOneWicket + 1
                }
            R.id.TeamOneReduce ->                 //if user selects wicket radiobutton then the scoreValue will be -1
                if (scoreValue != -1) {
                    //decrement team one run by the scoreValue
                    teamOneRun = teamOneRun - scoreValue
                } else {
                    //decrement team one wicket by 1
                    teamOneWicket = teamOneWicket - 1
                }
            R.id.TeamTwoIncrease ->                 //if user selects wicket radiobutton then the scoreValue will be -1
                if (scoreValue != -1) {
                    //increment team two run by scoreValue
                    teamTwoRun = teamTwoRun + scoreValue
                } else {
                    //increment team two wicket by 1
                    teamTwoWicket = teamTwoWicket + 1
                }
            R.id.TeamTwoReduce ->                 //if user selects wicket radiobutton then the scoreValue will be -1
                if (scoreValue != -1) {
                    //decrement team two run by the scoreValue
                    teamTwoRun = teamTwoRun - scoreValue
                } else {
                    //decrement team two wicket by one
                    teamTwoWicket = teamTwoWicket - 1
                }
            R.id.newGame -> {
                //newGame button resets all score to zero
                teamOneRun = 0
                teamTwoRun = 0
                teamOneWicket = 0
                teamTwoWicket = 0
            }
        }
        //update screen for updating screen values
        updateScreen()
    }

    fun updateScreen() {
        //declaring buttons
        val teamOneUp = findViewById<Button>(R.id.TeamOneIncrease)
        val teamOneDown = findViewById<Button>(R.id.TeamOneReduce)
        val teamTwoUp = findViewById<Button>(R.id.TeamTwoIncrease)
        val teamTwoDown = findViewById<Button>(R.id.TeamTwoReduce)
        val newGame = findViewById<Button>(R.id.newGame)

        //if run is 0 or less than 0 and wicket equal to or above 10 then disable minus button
        teamOneDown.isEnabled = teamOneRun > 0 && teamOneWicket < 10
        teamTwoDown.isEnabled = teamTwoRun > 0 && teamTwoWicket < 10
        //if wicket is more or equal to 10 then disable plus button
        teamOneUp.isEnabled = teamOneWicket < 10
        teamTwoUp.isEnabled = teamTwoWicket < 10

        //if radiobutton wicket is selected then enable the minus button until wicket is 0
        if (scoringValue == -1) {
            teamOneDown.isEnabled = teamOneWicket > 0
            teamTwoDown.isEnabled = teamTwoWicket > 0
        }

        //if all scores are zero then new game button is removed. It is shown only during game progress
        if (teamOneRun > 0 || teamTwoRun > 0 || teamOneWicket > 0 || teamTwoWicket > 0) newGame.visibility =
            View.VISIBLE else newGame.visibility = View.GONE

        //update team one score TextView - getScore method returns the string with run and wicket separated by a "/"
        val teamOneScore = findViewById<TextView>(R.id.TeamOneScore)
        teamOneScore.text = getScore(teamOneRun, teamOneWicket)

        //update team two score TextView
        val teamTwoScore = findViewById<TextView>(R.id.TeamTwoScore)
        teamTwoScore.text = getScore(teamTwoRun, teamTwoWicket)
    }

    //method to extract run and wickets from the score in TextView
    fun getRunAndWickets(score: String): IntArray {
        //the score is split into run and wickets using string function split() which returns string array
        val runAndWicketString: Array<String>
        //the score is split at "/" : left side contains run and right side contains wicket
        runAndWicketString = score.split("/").toTypedArray()
        //run and wicket values are passed onto int array
        val runAndWicket = IntArray(runAndWicketString.size)
        for (i in runAndWicketString.indices) {
            runAndWicket[i] = runAndWicketString[i].toInt()
        }
        return runAndWicket
    }//scoreValue is determined based on the radiobutton selection

    //method to determine selected radiobutton
    val scoringValue: Int
        get() {
            //scoreValue is determined based on the radiobutton selection
            val scoreValue: Int
            val scoringOptions = findViewById<RadioGroup>(R.id.ScoringOptions)
            scoreValue = when (scoringOptions.checkedRadioButtonId) {
                R.id.ScoreOne -> 1
                R.id.ScoreTwo -> 2
                R.id.ScoreThree -> 3
                R.id.ScoreFour -> 4
                R.id.ScoreFive -> 6
                R.id.Wicket -> -1
                else -> 0
            }
            return scoreValue
        }

    //getScore method returns the string with run and wicket value
    fun getScore(run: Int, wicket: Int): String {
        //to avoid negative values in run and wicket
        var run = run
        var wicket = wicket
        if (run < 0) {
            run = 0
        }
        if (wicket < 0) {
            wicket = 0
        }

        //if wicket is equal to 10 then only runs is displayed
        return if (wicket == 10) {
            run.toString()
        } else  //else if wicket is less than 10 then run and wicket is displayed separated by "/"
        {
            "$run/$wicket"
        }
    }
}