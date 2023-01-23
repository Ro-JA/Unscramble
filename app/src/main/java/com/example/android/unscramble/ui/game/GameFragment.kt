/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d(
            "GameFragment", "Word: ${viewModel.currentScrambledWord}" +
                    " Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}"
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentScrambledWord.observe(viewLifecycleOwner, { newWord -> // устоновлеваем наблюдателя
            // за текушем зашифрованым словом подключаясь к жизненому циклу фрагмента через viewLifecycleOwenr
            binding.textViewUnscrambledWord.text = newWord // второй параметр лямбда функция передает новое
            // слово
        } )

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
       viewModel.score.observe(viewLifecycleOwner, {newScore ->
           binding.score.text = getString(R.string.score, newScore)// добавили очки из лайвДата
       })
    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() { // проверяем вариант веденого слова пользователя
        val playerWord = binding.textInputEditText.text.toString() // достаем слово

        if (viewModel.isUserWordCorrect(playerWord)) { // слово игрока ровно слову из массива
            setErrorTextField(false) // сбрасываем поле ввода
            if (viewModel.nextWord()) { // слов меньше 10
            } else {
                showFinalScoreDialog() // выводим диалоговое окно
            }
        } else {
            setErrorTextField(true) // выводим попробуй еще раз
        }
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() { // отоброжает зашифрованое слово и стерает старое
        if (viewModel.nextWord()) { // проверяем количество слов

        } else {
            showFinalScoreDialog() // вызываем диалоговое окно
        }

    }

    /*
     * Gets a random word for the list of words and shuffles the letters in it.
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    private fun showFinalScoreDialog() { // функция для показа финального диалогового окна с счетом
        MaterialAlertDialogBuilder(requireContext()) // метод из метериалДиалог библеотеки джетпак
            // получает контекст из фрагмента
            .setTitle(getString(R.string.congratulations)) //передаем заголовок в окно из строк
            .setMessage(
                getString(
                    R.string.you_scored,
                    viewModel.score.value
                )
            ) // отображаем количество очков
            .setCancelable(false) // чтобы диалоговое окно не исчезала при нажатие кнопки назад
            .setNegativeButton(getString(R.string.exit)) { _, _ -> // добовляем кнопку выход
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame() // добовляем кнопку повторить игру
            }
            .show() // показываем диалоговое окно
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) { // выводит текст об ошибке при вводе закадированого
        //слова
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }



    override fun onDetach() {
        super.onDetach()
        Log.d("Game Fragment", "GameFragment destroyed!")
    }
}
