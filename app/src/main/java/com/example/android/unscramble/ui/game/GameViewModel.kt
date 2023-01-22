package com.example.android.unscramble.ui.game

import android.os.Build.VERSION_CODES.M
import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {


    private var _score = 0
    val score: Int
        get() = _score
    private var _currentWordCount = 0 // количество слов в игре
    val currentWordCount: Int
        get() = _currentWordCount
    private lateinit var _currentScrambledWord: String// зашифрованое слово
    val currentScrambledWord: String
        get() = _currentScrambledWord // гетар для фрагмента для безопасности


    private val wordsList: MutableList<String> =
        mutableListOf() // изменяемый список слов который мы

    // храним в игре чтобы избежать повторений
    private lateinit var currentWord: String // слова которое пытаеться рашифровать игорок

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord() // вызываем случайно слово
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
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
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    fun nextWord(): Boolean { //вспомогательный метод для обробатки даных в нутри вью вернет true
        // если количество слов меньше 10
        return if (currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else
            false

    }
}



