package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {


    private val _score = MutableLiveData(0) // количество очков храняться в изменяемом обекте
    val score: LiveData<Int> // хранения данных
        get() = _score
    private val _currentWordCount = MutableLiveData(0) // количество слов в игре
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount
    private val _currentScrambledWord = MutableLiveData<String>() // изменяемый обект хранения данных
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambleWord = it.toString()
            val spannable: Spannable = SpannableString(scrambleWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambleWord).build(),
                0,
                scrambleWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }



    private val wordsList: MutableList<String> =
        mutableListOf() // изменяемый список слов который мы

    // храним в игре чтобы избежать повторений
    private lateinit var currentWord: String // слова которое пытаеться рашифровать игорок

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord() // вызываем случайно слово
    }



    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord) // устанвливает значения если есть активные
            //наблюдатели будет отправлено им
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    fun nextWord(): Boolean { //вспомогательный метод для обробатки даных в нутри вью вернет true
        // если количество слов меньше 10
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else
            false

    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE) // увеличевает очки на 20
    }

    fun isUserWordCorrect(playerWord: String): Boolean { // проверяет слово игрока с зашифрованым
        if (playerWord.equals(currentWord, true)) {
            increaseScore() // увеличевает счет
            return true
        }
        return false
    }

    fun reinitializeData() { // сбрасываем данные
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()

    }
}



