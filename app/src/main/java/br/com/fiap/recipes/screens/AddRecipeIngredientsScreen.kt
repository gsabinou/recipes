package br.com.fiap.recipes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.recipes.model.Ingredient
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.saveRecipeIngredients
import br.com.fiap.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeIngredientsScreen(
    navController: NavHostController?,
    recipeId: Int?,
    recipeName: String?
) {

    var ingredients = remember {
        mutableStateListOf<Ingredient>()
    }

    var ingredient by remember {
        mutableStateOf("")
    }

    var ingredientNumber by remember {
        mutableIntStateOf(0)
    }

// Criar o escopo de coroutine que será utilizado
// para chamar a gravação dos ingredientes da receita
    val scope = rememberCoroutineScope()

// Lista que receberá a lista de ingredientes retornados pela API
    var newIngredients: List<Ingredient> by remember {
        mutableStateOf(listOf())
    }

// Função lambda para preparar e enviar
// os dados para o servidor
    val saveNewIngredients: () -> Unit = {
        scope.launch {
            val ingredientsToSend = ingredients.map{
                it.copy(id=null)
            }
            newIngredients = saveRecipeIngredients(
                recipeId = recipeId!!,
                ingredients = ingredientsToSend
            )

            navController?.navigate(
                Destination.AddPreparationMethodsScreen.createRoute(
                    recipeId = recipeId,
                    recipeName = recipeName.toString()
                )
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme
                    .colorScheme.background
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Step 2...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "In this moment, list all delicious ingredients that you used in your recipe.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 8.dp
                    )
                )
                Text(
                    text = recipeName.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatListNumbered,
                        contentDescription = "Add box",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ingredients list",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Campo título do ingrediente
                OutlinedTextField(
                    value = ingredient,
                    onValueChange = { ingredient = it },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults
                        .colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                        ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    placeholder = {
                        Text(text = "Describe the ingredient here")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                ingredientNumber = ingredients.size + 1
                                ingredients.add(Ingredient(ingredientNumber, ingredient))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add button",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .weight(2f)
            ) {
                LazyColumn {
                    items(ingredients) { ingredient ->
                        IngredientItem(
                            onClick = {
                                // Removemos o ingrediente da lista
                                ingredients.remove(ingredient)

                                // Atualizamos a lista de ingredientes
                                val reorderedList = ingredients.mapIndexed { index, ingredient ->
                                    ingredient.copy(id = index + 1)
                                }

                                // Atualizamos a lista de ingredientes
                                ingredients.clear()
                                ingredients.addAll(reorderedList)
                            },
                            ingredient
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                //.align(Alignment.BottomStart)
            ) {
                TextButton(
                    onClick = saveNewIngredients
                ) {
                    Text(
                        text = "NEXT",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 28.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next button"
                    )
                }
            }

        }

    }
}

@Composable
fun IngredientItem(onClick: () -> Unit, ingredient: Ingredient) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        //.height(60.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme
                    .colorScheme.primary
            )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = ingredient.id.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Text(
                text = ingredient.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(2f)
            )
            IconButton(
                onClick = { onClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ingredient",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun IngredientItemPreview() {
    RecipesTheme {
        IngredientItem(
            onClick = {},
            ingredient = Ingredient(22, "Teste")
        )
    }
}

@Preview
@Composable
private fun AddRecipeScreenPreview() {
    RecipesTheme {
        AddRecipeIngredientsScreen(
            navController = null,
            recipeId = 0,
            recipeName = "Recipe name")
    }
}