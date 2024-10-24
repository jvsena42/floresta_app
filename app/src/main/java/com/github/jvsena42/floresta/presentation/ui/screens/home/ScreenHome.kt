package com.github.jvsena42.floresta.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.R
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
import com.github.jvsena42.floresta.presentation.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenHome(
    uiState: HomeUIState
) {
    FlorestaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                ),
                title = {

                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(R.string.refresh),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = stringResource(R.string.new_wallet),
                            tint = Color.White
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.balanceBTC,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.btc),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.balanceSats,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.sats),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = CircleShape.copy(
                            bottomEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            topStart = CornerSize(32.dp),
                            topEnd = CornerSize(32.dp),
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.transactions),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ScreenHome(HomeUIState(balanceBTC = "0.05411", balanceSats = "5.411.000"))
}

