package com.github.jvsena42.floresta.presentation.ui.screens.node

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenNode(uiState: NodeUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CenterAlignedTopAppBar(title = {
            Text(stringResource(R.string.node), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        })

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.number_of_peers),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(uiState.numberOfPeers)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.block_height),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(uiState.blockHeight)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.block_hash),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(uiState.blockHash, maxLines = 2, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.network),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(uiState.network)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.difficulty),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(uiState.numberOfPeers)
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
                difficulty = "9.7 minutes"
            )
        )
    }
}