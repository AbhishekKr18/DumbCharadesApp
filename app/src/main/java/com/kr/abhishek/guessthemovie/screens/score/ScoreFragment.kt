package com.kr.abhishek.guessthemovie.screens.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.kr.abhishek.guessthemovie.R
import com.kr.abhishek.guessthemovie.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private lateinit var viewModel : ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding : FragmentScoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

//        val ScoreA = arguments?.let { ScoreFragmentArgs.fromBundle(it).teamAScore }
//        val ScoreB = arguments?.let { ScoreFragmentArgs.fromBundle(it).teamBScore }

        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.teamAScore, scoreFragmentArgs.teamBScore)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

//        //observer for the values
//        viewModel.finalAScoreValue.observe(viewLifecycleOwner, Observer { finalScore ->
//            binding.finalAScore.text = finalScore.toString()
//        })
//
//        viewModel.finalBScoreValue.observe(viewLifecycleOwner, Observer { finalScore ->
//            binding.finalBScore.text = finalScore.toString()
//        })

        if(scoreFragmentArgs.teamAScore > scoreFragmentArgs.teamBScore) {
            binding.winnerTeam.text = getString(R.string.title_team_a)
            binding.loserTeam.text = getString(R.string.title_team_b)
            binding.finalAScore.text = scoreFragmentArgs.teamAScore.toString()
            binding.finalBScore.text = scoreFragmentArgs.teamBScore.toString()

        } else {
            binding.winnerTeam.text = getString(R.string.title_team_b)
            binding.loserTeam.text = getString(R.string.title_team_a)
            binding.finalAScore.text = scoreFragmentArgs.teamBScore.toString()
            binding.finalBScore.text = scoreFragmentArgs.teamAScore.toString()

        }

        binding.buttonPlayAgain.setOnClickListener { view->
            view.findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
        }

        return binding.root
    }

}
