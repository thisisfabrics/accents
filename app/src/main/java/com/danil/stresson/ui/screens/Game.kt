package com.danil.stresson.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.danil.stresson.R
import com.danil.stresson.ui.StressState
import com.danil.stresson.ui.StressViewModel
import com.danil.stresson.ui.uElements.Menu

@Composable
fun Game(viewModel: StressViewModel, uiState: StressState, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding) * 2)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var modeListIsOpened by rememberSaveable { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .defaultMinSize(minWidth = dimensionResource(id = R.dimen.mode_width))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        modeListIsOpened = !modeListIsOpened
                    }
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Row {
                    Text(
                        text = stringResource(uiState.modes.first()),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
                    )
                }
                if (modeListIsOpened) {
                    for ((i, elem) in uiState.modes.slice(1..uiState.modes.lastIndex)
                        .withIndex())
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    modeListIsOpened = !modeListIsOpened
                                    viewModel.changeMode(i + 1)
                                },
                        ) {
                            Text(
                                text = stringResource(elem),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(
                                    bottom = dimensionResource(id = R.dimen.padding),
                                    start = dimensionResource(id = R.dimen.padding),
                                    end = dimensionResource(id = R.dimen.padding)
                                )
                            )
                        }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.menu)))
                }
            }
            IconButton(
                onClick = { viewModel.restart() },
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding))
            ) {
                Icon(
                    Icons.Outlined.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }

        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    start = dimensionResource(id = R.dimen.padding) * 2,
                    end = dimensionResource(id = R.dimen.padding) * 2,
                    top = dimensionResource(id = R.dimen.menu) * 1.5f,
                    bottom = dimensionResource(id = R.dimen.menu) * 1.5f
                )
                .verticalScroll(rememberScrollState())
                .zIndex(-1f)
        ) {
            if (uiState.gameIsInProgress) {
                Text(
                    text = uiState.currentWord,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                for (elem in uiState.currentVariants) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                if (elem.second != elem.first)
                                    viewModel.addToStatistics(elem.second)
                                viewModel.next()
                            },
                    ) {
                        Text(
                            text = elem.first,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.padding))
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                }
                IconButton (
                    onClick = { viewModel.previous() }
                ) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = null)
                }
            } else {
                Text(
                    text = "${viewModel.getLengthOfWords() - uiState.incorrectWords.count()}/${viewModel.getLengthOfWords()}",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                if (viewModel.getLengthOfWords() - uiState.incorrectWords.count() == viewModel.getLengthOfWords())
                    Text(
                        text = stringResource(R.string.congratulations),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                else {
                    Text(
                        text = stringResource(R.string.memorize),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                    for (elem in uiState.incorrectWords) {
                        Row {
                            Text(
                                text = elem,
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .fillMaxWidth()
                                    .padding(dimensionResource(id = R.dimen.padding))
                            )
                        }
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                    }
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.menu)))
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = dimensionResource(id = R.dimen.padding) * 2,
                    end = dimensionResource(R.dimen.padding) * 2
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.gameIsInProgress) {
                Text(
                    text = (uiState.progress * 100).toInt().toString() + '%',
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(
                            dimensionResource(id = R.dimen.padding)
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(id = R.dimen.padding))
                ) {
                    Spacer(
                        Modifier
                            .height(dimensionResource(id = R.dimen.padding))
                            .fillMaxWidth(uiState.progress)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                    Spacer(
                        Modifier
                            .height(dimensionResource(id = R.dimen.padding))
                            .fillMaxWidth(1 - uiState.progress)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
            }
            Menu(navController)
        }
    }
}
