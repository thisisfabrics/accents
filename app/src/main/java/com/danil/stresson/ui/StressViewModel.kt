package com.danil.stresson.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.danil.stresson.StressOnApplication
import com.danil.stresson.data.StressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class StressViewModel(val stressRepository: StressRepository) : ViewModel() {
    val uiState = MutableStateFlow(StressState())
    private var words = listOf<Pair<String, String>>()
    private val vowels = "аеиоуыэюяaeiouy"

    init {
        restart()
    }

    enum class Screen {
        Main,
        Settings
    }

    fun getContext(): Context {
        return stressRepository.context()
    }

    fun getLengthOfWords(): Int {
        return words.count()
    }

    private fun buildCurrentVariants() {
        val (word, correct) = words[uiState.value.currentWordIndex]
        val variants: MutableList<Pair<String, String>> = mutableListOf()
        for (i in word.indices)
            if (word[i] != '(') {
                if (vowels.contains(word[i]))
                    variants.add(
                        Pair(
                            if (word.lastIndex != i)
                                word.slice(0 until i) + word[i].uppercase() +
                                        word.slice(i + 1..word.lastIndex)
                            else word.slice(0 until i) + word[i].uppercase(),
                            correct
                        )
                    )
            } else break
        uiState.update {
            it.copy(currentVariants = variants)
        }
    }

    fun restart() {
        words = stressRepository.getWords(uiState.value.modes.first())
        uiState.update {
            it.copy(
                currentWordIndex = 0,
                progress = 0.0f,
                currentWord = words[0].first,
                gameIsInProgress = true,
                incorrectWords = listOf()
            )
        }
        buildCurrentVariants()
    }

    fun changeMode(index: Int) {
        var modesStart: List<Int> = listOf()
        var modesEnd: List<Int> = listOf()
        if (index != 0)
            modesStart = uiState.value.modes.slice(0 until index)
        if (index != uiState.value.modes.lastIndex)
            modesEnd = uiState.value.modes.slice(index + 1..uiState.value.modes.lastIndex)
        uiState.update {
            it.copy(modes = listOf(uiState.value.modes[index]) + modesStart + modesEnd)
        }
        restart()
    }

    fun addToStatistics(word: String) {
        uiState.update {
            it.copy(
                incorrectWords = uiState.value.incorrectWords + listOf(word)
            )
        }
    }

    fun next() {
        if (uiState.value.currentWordIndex + 1 == words.count()) {
            uiState.update {
                it.copy(gameIsInProgress = false)
            }
            return
        }
        val progress = (uiState.value.currentWordIndex + 1).toFloat() / words.lastIndex
        uiState.update {
            it.copy(
                currentWordIndex = uiState.value.currentWordIndex + 1,
                currentWord = words[uiState.value.currentWordIndex + 1].first,
                progress = progress
            )
        }
        buildCurrentVariants()
    }

    fun previous() {
        if (uiState.value.currentWordIndex - 1 != -1) {
            if (uiState.value.incorrectWords.contains(words[uiState.value.currentWordIndex - 1].second)) {
                val index = uiState.value.incorrectWords.indexOf(words[uiState.value.currentWordIndex - 1].second)
                var newIncorrectWords = uiState.value.incorrectWords.slice(0 until index)
                if (index != uiState.value.incorrectWords.lastIndex)
                    newIncorrectWords = newIncorrectWords +
                            uiState.value.incorrectWords.slice(index + 1..uiState.value.incorrectWords.lastIndex)
                uiState.update { it.copy(incorrectWords = newIncorrectWords) }
            }
            val progress = (uiState.value.currentWordIndex - 1).toFloat() / words.lastIndex
            uiState.update {
                it.copy(
                    currentWordIndex = uiState.value.currentWordIndex - 1,
                    currentWord = words[uiState.value.currentWordIndex - 1].first,
                    progress = progress
                )
            }
            buildCurrentVariants()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as StressOnApplication
                val stressRepository = application.container.stressRepository
                StressViewModel(stressRepository = stressRepository)
            }
        }
    }
}
