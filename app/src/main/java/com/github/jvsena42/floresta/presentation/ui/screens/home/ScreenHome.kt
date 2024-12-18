package com.github.jvsena42.floresta.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.R
import com.github.jvsena42.floresta.presentation.ui.screens.home.components.TransactionItem
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenHome(
    viewmodel: HomeViewModel = koinViewModel()
) {
    val uiState: HomeUIState by viewmodel.uiState.collectAsState()
    ScreenHome(uiState, viewmodel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenHome(
    uiState: HomeUIState,
    onAction: (HomeViewModel.HomeActions) -> Unit
) {
    FlorestaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                title = { },
                actions = {
                    IconButton(
                        onClick = { onAction(HomeViewModel.HomeActions.OnClickRefresh) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(R.string.refresh),
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.balanceBTC,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = stringResource(R.string.btc),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.balanceSats,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = stringResource(R.string.sats),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.height(42.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape.copy(
                            bottomEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            topStart = CornerSize(32.dp),
                            topEnd = CornerSize(32.dp),
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(34.dp))

                Text(
                    text = stringResource(R.string.transactions),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 18.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.transactions, key = { item -> item.title }) { item ->
                        TransactionItem(
                            title = item.title,
                            date = item.date,
                            amount = item.amount,
                            isReceived = item.isReceived,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ScreenHome(
        HomeUIState(
            balanceBTC = "0.05411", balanceSats = "5.411.000", transactions = listOf(
                TransactionVM(
                    title = "ASDN646AS8D46ASD8FF8AS84F68AS4DA5F3",
                    date = "01/02/2024 19:32",
                    amount = "15265",
                    isReceived = false,
                ),
                TransactionVM(
                    title = "ASDN646AS8D46AS8FF8AS84F68AS4DAS5F3",
                    date = "01/02/2024 19:32",
                    amount = "15265",
                    isReceived = true,
                ),
                TransactionVM(
                    title = "ASD646AS8D46ASD8FF8AS84F68AS4DAS5F3",
                    date = "01/02/2024 19:32",
                    amount = "15265",
                    isReceived = false,
                ),
                TransactionVM(
                    title = "ASDN646AS8D46ASD8FFAS84F68AS4DAS5F3",
                    date = "01/02/2024 19:32",
                    amount = "15265",
                    isReceived = true,
                ),
                TransactionVM(
                    title = "ASDN646AS846ASD8FF8AS84F68AS4DAS5F3",
                    date = "01/02/2024 19:32",
                    amount = "15265",
                    isReceived = false,
                ),
            )
        )
    ) {}
}

