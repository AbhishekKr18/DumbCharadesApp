package com.kr.abhishek.guessthemovie.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Layout
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kr.abhishek.guessthemovie.R
import com.kr.abhishek.guessthemovie.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private val viewModel : GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    private lateinit var binding : FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        //Set the view model for data binding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.gameViewModel = viewModel

//       WE ARE NOT USING ON CLICK LISTENERS NOW BECAUSE WE ARE USING VIEW MODEL WITH DATA BINDING
//        SO FROM LAYOUT WE ARE SETTING ON CLICK ATTRIBUTE ITSELF

//        binding.correctButton.setOnClickListener {
//            viewModel.onCorrect()
//
//           // updateScoreText() WE DON'T NEED THESE BCZ WE ARE USING LIVE DATA
//            //updateWordText()  WHICH AUTOMATICALLY UPDATE SCORE AND WORDS USING OBSERVER
//            //updateCurrentTeam()
//
//        }
//
//        binding.skipButton.setOnClickListener {
//            viewModel.onSkip()
//
//            //updateWordText()
//           // updateCurrentTeam()
//
//        }

        //THIS IS USING DATA BINDING WITH LIVE DATA SO THAT WE DON'T HAVE TO BE DEPENDENT ON OBSERVERS WE CAN REMOVE OBSERVER HERE
        binding.lifecycleOwner = this

        //SO HERE WE CAN REMOVE THESE OBSERVERS OR WE CAN KEEP THIS AS IT IS
        viewModel.teamAScore.observe(viewLifecycleOwner, Observer { newScore->
            binding.scoreA.text = newScore.toString()
        })

        viewModel.teamBScore.observe(viewLifecycleOwner, Observer { newScore->
            binding.scoreB.text = newScore.toString()
        })

        viewModel.currentWord.observe(viewLifecycleOwner, Observer { newWord->
            binding.currentWordText.text = newWord.toString()
        })

        viewModel.currentTeamId.observe(viewLifecycleOwner, Observer { id->
            if(id == 0) {
                binding.currentTeamText.text = getString(R.string.team_A_name)
            } else {
                binding.currentTeamText.text = getString(R.string.team_B_name)
            }
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

       // updateCurrentTeam()
       // initializeScore()

        binding.endButton.setOnClickListener { view ->
            gameFinished(view)
        }

        // Buzzes when triggered with different buzz events
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        return binding.root
    }

    private fun gameFinished(view: View?) {
        val finalAScore = viewModel.teamAScore.value ?: 0
        val finalBScore = viewModel.teamBScore.value ?: 0

        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(finalAScore, finalBScore)
        view?.findNavController()?.navigate(action)
    }

//    private fun updateCurrentTeam() {
//        if(viewModel.currentTeamId == 0) {
//            binding.currentTeamText.text = "TEAM A"
//        } else {
//            binding.currentTeamText.text = "TEAM B"
//        }
//    }




//   private fun updateWordText() {
//     binding.currentWordText.text = viewModel.currentWord.toString()
//  }

//    private fun updateScoreText() { //SINCE WE ARE USING LIVE DATA WE DON'T NEED THIS
//        if(viewModel.currentTeamId == 1) { //here its opposite because after we click correct team id would already be changed
//            binding.scoreA.text = viewModel.teamAScore.toString()
//        } else {
//            binding.scoreB.text = viewModel.teamBScore.toString()
//        }
//    }

//    private fun initializeScore() {
//        binding.scoreA.text = viewModel.teamAScore.toString()
//        binding.scoreB.text = viewModel.teamBScore.toString()
//    }


    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}