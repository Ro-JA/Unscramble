package com.example.android.unscramble.ui.game

import android.os.Build.VERSION_CODES.M
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {


    private var _score = 0
    val score: Int
        get() = _score
    private var _currentWordCount = 0 // количество слов в игре
    val currentWordCount: Int
        get() = _currentWordCount
    private val _currentScrambledWord = MutableLiveData<String>() // изменяемый обект хранения данных
    val currentScrambledWord: LiveData<String> // неизменяемый обект данных текущее зашифрованое слово
        get() = _currentScrambledWord



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
            _currentScrambledWord.value = String(tempWord) // устанвливает значения если есть активные
            //наблюдатели будет отправлено им
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

    private fun increaseScore() {
        _score += SCORE_INCREASE // увеличевает очки на 20
    }

    fun isUserWordCorrect(playerWord: String): Boolean { // проверяет слово игрока с зашифрованым
        if (playerWord.equals(currentWord, true)) {
            increaseScore() // увеличевает счет
            return true
        }
        return false
    }

    fun reinitializeData() { // сбрасываем данные
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()

    }
}



