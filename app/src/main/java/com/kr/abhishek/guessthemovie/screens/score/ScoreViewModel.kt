package com.kr.abhishek.guessthemovie.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalAScore : Int, finalBScore : Int) : ViewModel() {

    private val _finalAScoreValue = MutableLiveData<Int>()
    val finalAScoreValue : LiveData<Int>
        get() = _finalAScoreValue

    private val _finalBScoreValue = MutableLiveData<Int>()
    val finalBScoreValue : LiveData<Int>
        get() = _finalBScoreValue

    init {
        _finalAScoreValue.value = finalAScore
        _finalBScoreValue.value = finalBScore
    }

}