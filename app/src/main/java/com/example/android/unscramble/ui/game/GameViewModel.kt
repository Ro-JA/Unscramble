package com.example.android.unscramble.ui.game

import android.os.Build.VERSION_CODES.M
import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var score = 0
    private var currentWordCount = 0 // количество слов в игре
    private var _currentScrambledWord = "test" // зашифрованое слово
    val currentScrambledWord: String
        get() = _currentScrambledWord // гетар для фрагмента для безопасности


    private val wordList: MutableList<String> = mutableListOf() // изменяемый список слов который мы
    // храним в игре чтобы избежать повторений
    private lateinit var currentWord: String // слова которое пытаеться рашифровать игорок




    private fun getNextWord() {
        currentWord = allWordsList.random() // получает случайное слово из списка слов

        val tempWord = currentWord.toCharArray() // получаем массив сисмволов из случайного слова
        while (String(tempWord).equals(currentWord, false)) { // пока массив символов
            //равен случайному слову тусовать символы
            tempWord.shuffle() // в этом массиве символо тусуем значения
        }
        if (wordList.contains(currentWord)) {
            getNextWord() // если список уже содержит случай слово, создаем новое слова
        } else {
            _currentScrambledWord = String(tempWord) // добовляем его в зашифрованое слово
            ++currentWordCount // увеличевае счетчик слов
            wordList.add(currentWord) // добовляем его в список слов чтобы больше не повторялось

        }
    }
    init {
        Log.d("GameFragment", "GameViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

}