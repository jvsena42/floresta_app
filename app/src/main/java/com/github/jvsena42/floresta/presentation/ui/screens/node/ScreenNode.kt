package com.github.jvsena42.floresta.presentation.ui.screens.node

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jvsena42.floresta.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenNode(
    viewModel: NodeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ScreenNode(uiState)
}

private val cardHeight = 128.dp

private val cardsModifier = Modifier
    .height(height = cardHeight)
    .fillMaxWidth()
    .border(
        width = 1.dp,
        color = Color.Gray,
        shape = CircleShape.copy(CornerSize(24.dp))
    )
    .padding(horizontal = 26.dp, vertical = 14.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenNode(uiState: NodeUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.node),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(
                    modifier = cardsModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.number_of_peers),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(uiState.numberOfPeers)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            item {
                Column(
                    modifier = cardsModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.block_height),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(uiState.blockHeight)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            item {
                Column(
                    modifier = cardsModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.best_block),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        uiState.blockHash,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            item {
                Column(
                    modifier = cardsModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.network),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(uiState.network)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            item {
                Column(
                    modifier = cardsModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.difficulty),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("""${uiState.difficulty} ${stringResource(R.string.minutes)}""")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme {
        ScreenNode(
            NodeUiState(
                numberOfPeers = "5",
                blockHeight = "1235334",
                blockHash = "00000000000000002d342634efg588252ssq123332sdt6637387d",
                network = "Signet",
                difficulty = "9.7"
            )
        )
    }
}