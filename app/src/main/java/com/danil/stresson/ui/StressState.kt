package com.danil.stresson.ui

import com.danil.stresson.R

data class StressState(
    val modes: List<Int> = listOf(R.string.nouns,
        R.string.adjectives, R.string.verbs,
        R.string.participles, R.string.gerunds,
        R.string.adverbs, R.string.all),
    val progress: Float = 0.5f,
    val gameIsInProgress: Boolean = true,
    val currentWord: String = "",
    val currentWordIndex: Int = 0,
    val currentVariants: List<Pair<String, String>> = listOf(Pair("wOrd", "wOrd")),
    val incorrectWords: List<String> = listOf()
)
