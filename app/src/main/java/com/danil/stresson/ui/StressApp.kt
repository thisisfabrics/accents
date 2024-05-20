package com.danil.stresson.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danil.stresson.R
import com.danil.stresson.ui.theme.white

@Composable
fun Stress(
    viewModel: StressViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = StressViewModel.Screen.Main.name
        ) {
            composable(route = StressViewModel.Screen.Main.name) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding) * 2)
                            .fillMaxWidth()
                            .background(color = white),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var modeListIsOpened by rememberSaveable { mutableStateOf(false) }
                        Column(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.mode_width))
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(dimensionResource(id = R.dimen.padding))
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
                        ) {
                            Row {
                                Text(
                                    text = stringResource(uiState.modes.first()),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            if (modeListIsOpened)
                                for ((i, elem) in uiState.modes.slice(1..uiState.modes.lastIndex)
                                    .withIndex())
                                    Row(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = MutableInteractionSource(),
                                                indication = null
                                            ) {
                                                modeListIsOpened = !modeListIsOpened
                                                viewModel.changeMode(i + 1)
                                            },
                                    ) {
                                        Text(text = stringResource(elem))
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
                            .padding(dimensionResource(id = R.dimen.padding) * 2)
                            .verticalScroll(rememberScrollState())
                            .zIndex(-1f)
                    ) {
                        if (uiState.gameIsInProgress) {
                            Text(
                                text = uiState.currentWord,
                                style = MaterialTheme.typography.displayLarge
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
                        } else {
                            Text(
                                text = "${viewModel.getLengthOfWords() - uiState.incorrectWords.count()}/${viewModel.getLengthOfWords()}",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                            if (viewModel.getLengthOfWords() - uiState.incorrectWords.count() == viewModel.getLengthOfWords())
                                Text(
                                    text = stringResource(R.string.congratulations),
                                    style = MaterialTheme.typography.displayMedium
                                )
                            else {
                                Text(
                                    text = stringResource(R.string.memorize),
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                                for (elem in uiState.incorrectWords) {
                                    Row {
                                        Text(
                                            text = elem,
                                            style = MaterialTheme.typography.displayMedium,
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
                                modifier = Modifier
                                    .background(
                                        color = white,
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
            composable(route = StressViewModel.Screen.Settings.name) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding) * 2)
                    ) {
                        Text(
                            stringResource(R.string.description),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding) * 2)
                            .align(Alignment.Center)
                    ) {
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                        Text(
                            stringResource(R.string.language_settings),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding)))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags("en")
                                )
                            }) {
                                Text("English")
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))
                            Button(onClick = {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags("ru")
                                )
                            }) {
                                Text("Russian")
                            }
                        }
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.menu) * 2))
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                start = dimensionResource(id = R.dimen.padding) * 2,
                                end = dimensionResource(id = R.dimen.padding) * 2
                            )
                    ) {
                        Text(
                            stringResource(R.string.bashinvolved),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Menu(navController)
                    }

                }
            }

        }
    }
}


@Composable
fun Menu(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.menu))
            .background(color = white),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate(StressViewModel.Screen.Main.name) }) {
            Icon(
                Icons.Outlined.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = { navController.navigate(StressViewModel.Screen.Settings.name) }) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}