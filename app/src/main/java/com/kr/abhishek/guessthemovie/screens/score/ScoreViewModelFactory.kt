package com.kr.abhishek.guessthemovie.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory(private val finalAScore: Int, private val finalBScore: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalAScore, finalBScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}