package com.danil.stresson.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import com.danil.stresson.R
import com.danil.stresson.ui.uElements.Menu

@Composable
fun Settings(navController: NavHostController) {
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
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
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
                    Text(
                        stringResource(R.string.english),
                        color = MaterialTheme.colorScheme.background
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))
                Button(onClick = {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags("ru")
                    )
                }) {
                    Text(
                        stringResource(R.string.russian),
                        color = MaterialTheme.colorScheme.background
                    )
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