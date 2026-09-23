package br.com.fiap.recipes.screens

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.recipes.ui.theme.RecipesTheme

@Composable
fun TopEndCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(85.dp),
//            .align(alignment = Alignment.TopEnd), por estar em uma box, não é mais possível a usar diretamente no card
        shape = RoundedCornerShape(
            bottomStart = 85.dp
        ),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
    ) {}
}

@Composable
fun BottomStartCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(85.dp),
//            .align(alignment = Alignment.BottomStart),
        shape = RoundedCornerShape(
            topEnd = 85.dp
        ),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
    ) {}
}

@Preview
@Composable
private fun TopEndCardPreview() {
    RecipesTheme {
        TopEndCard()

    }
}

@Preview
@Composable
private fun BottomStartCardPreview() {
    RecipesTheme {
        BottomStartCard()
    }
    
}