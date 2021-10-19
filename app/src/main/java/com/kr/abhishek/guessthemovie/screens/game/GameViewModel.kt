package com.kr.abhishek.guessthemovie.screens.game

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.kr.abhishek.guessthemovie.model.MovieDetails
import com.kr.abhishek.guessthemovie.network.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

//Buzz patterns
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val SKIP_BUZZ_PATTERN = longArrayOf(0, 200)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {

    // TEAM_A_ID = 0
    // TEAM_B_ID = 1
        companion object {
            // These represent different important times
            // This is when the game is over
            const val DONE = 0L
            // This is the number of milliseconds in a second
            const val ONE_SECOND = 1000L
            // This is the time of guessing a movie
            const val COUNTDOWN_TIME = 60000L

            // This is the time when the phone will start buzzing each second
            private const val COUNTDOWN_PANIC_SECONDS = 5L
        }


        enum class BuzzType(val pattern: LongArray) {
            CORRECT(CORRECT_BUZZ_PATTERN),
            SKIP(SKIP_BUZZ_PATTERN),
            COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
            NO_BUZZ(NO_BUZZ_PATTERN)
        }

        private val timer : CountDownTimer


    val constImageUrl = "https://image.tmdb.org/t/p/w500"

        lateinit var movies : List<MovieDetails>

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
        private val _eventBuzz = MutableLiveData<BuzzType>()
        val eventBuzz: LiveData<BuzzType>
            get() = _eventBuzz

        private val _currentTeamId = MutableLiveData<Int>();
        val currentTeamId : LiveData<Int>
            get() = _currentTeamId

        private val _teamAScore = MutableLiveData<Int>()
        val teamAScore : LiveData<Int>
            get() = _teamAScore

        private val _teamBScore = MutableLiveData<Int>()
        val teamBScore : LiveData<Int>
            get() = _teamBScore

        private val _currentWord = MutableLiveData<String>()
        val currentWord : LiveData<String>
            get() = _currentWord

        private val _imageURL = MutableLiveData<String>()
        val imageURL : LiveData<String>
            get() = _imageURL

        private val _movieList = MutableLiveData<List<MovieDetails>>()
        val movieList : LiveData<List<MovieDetails>>
            get() = _movieList

        private val _currentTime = MutableLiveData<Long>()
        val currentTime : LiveData<Long>
            get() = _currentTime

    //COROUTINE JOB AND SCOPE . SCOPE WE ARE USING MAIN THREAD BCZ DEFERRED WILL AWAIT FOR RESULT WITHOUT BLOCKING UI
    //SO WE CAN USE UI THREAD HERE I.E. MAIN THREAD
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getMovieList(Random.nextInt(0, 200))
       // nextWord()
        _teamAScore.value = 0
        _teamBScore.value = 0
        _currentTeamId.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value = BuzzType.SKIP
                onSkip()
            }
        }

        //_currentTime.value?.let { DateUtils.formatElapsedTime(it) }

        timer.start()
    }

    private fun nextWord() {
        if(movies.isEmpty())
            callAgain()
        else {
        _currentWord.value = movies[0].title
        _imageURL.value = constImageUrl + movies[0].image_src
        movies = movies.drop(1)
            timer.start() }
    }

    private fun getMovieList(page : Int) {
        coroutineScope.launch {
            var getMoviesDeferred = retrofitService.getMovieList(page)

          try {
              var result = getMoviesDeferred.await()
              _movieList.value = result?.movieDetails

              setMovieToList()
          } catch (t : Throwable) {
              callAgain()
              //_currentWord.value = "Failure: " + t.message
          }

        }
    }

    private fun setMovieToList() {
        movies = movieList.value!!
        nextWord()
    }

    private fun callAgain() {
        //Log.i("GVM", "callAgainOnFailure: CalledAgain")
        getMovieList(Random.nextInt(0,200))
        //nextWord()
    }


    fun onSkip() {
        _currentTeamId.value = (currentTeamId.value?.plus(1))?.rem(2)
        _eventBuzz.value = BuzzType.SKIP
        nextWord()
    }

    fun onCorrect() {
        if(currentTeamId.value == 0) {
            _teamAScore.value = teamAScore.value?.plus(1);
        } else {
            _teamBScore.value = teamBScore.value?.plus(1);
        }
        _currentTeamId.value = (currentTeamId.value?.plus(1))?.rem(2)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        timer.cancel()
    }

}

