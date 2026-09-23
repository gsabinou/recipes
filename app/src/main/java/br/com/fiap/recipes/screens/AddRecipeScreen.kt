package br.com.fiap.recipes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.model.Category
import br.com.fiap.recipes.model.DifficultLevel
import br.com.fiap.recipes.model.RecipeRequest
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.getAllCategories
import br.com.fiap.recipes.repository.saveRecipe
import br.com.fiap.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavHostController?) {

    // Obter lista de categorias da API
    var categories = getAllCategories()

    // Instância de uma categoria
    var selectedCategory by remember {
        mutableStateOf(Category(1, "Select"))
    }

    // Controla a exibição da lista de categorias
    var expanded by remember {
        mutableStateOf(false)
    }

    // Lista de níveis de dificuldade
    val difficultLevelList = listOf(
        DifficultLevel.BEGINNER,
        DifficultLevel.INTERMEDIATE,
        DifficultLevel.ADVANCED
    )

    // Nível de dificuldade selecionado
    var difficultLevel by remember {
        mutableStateOf(difficultLevelList[0])
    }

    // Posição do Slider (Cooking time)
    var cookingTime by remember {
        mutableStateOf(0f)
    }

    // Variáveis de estado para nome e descrição da receita
    var recipeTitle by remember {
        mutableStateOf("")
    }
    var recipeDescription by remember {
        mutableStateOf("")
    }

    // variável que irá receber um objeto RecipeRequest retornado ao cadastrar uma nova receita
    var newRecipe by remember {
        mutableStateOf(RecipeRequest())
    }

    // Criamos um escopo de corrotina
    val scope = rememberCoroutineScope()

    var recipeRequest by remember {
        mutableStateOf(RecipeRequest())
    }

// função que será chamada para gravar a receita
    val saveNewRecipe: () -> Unit = {
        recipeRequest = RecipeRequest(
            title = recipeTitle,
            difficultLevel = difficultLevel,
            description = recipeDescription,
            cookingTime = cookingTime.toInt(),
            creationDate = LocalDate.now().toString(),
            category = selectedCategory
        )
        scope.launch {
            newRecipe = saveRecipe(recipeRequest)
//            navController!!
//                .navigate(
//                    Destination
//                        .AddRecipeIngredientsScreen
//                        .createRoute(
//                            newRecipe!!.id!!,
//                            newRecipe!!.title
//                        )
//                )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme
                    .colorScheme.background
            )
    ) {
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(
                    top = 40.dp,
                    end = 24.dp,
                    bottom = 16.dp,
                    start = 24.dp
                )
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                //.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.add_recipe_title),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.add_recipe_subtitle),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 24.dp
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddBox,
                        contentDescription = "Add box",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Basic recipe data",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Campo título da receita
                OutlinedTextField(
                    value = recipeTitle,
                    onValueChange = {recipeTitle = it},
                    modifier = Modifier
                        .padding(top = 8.dp)
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
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    placeholder = {
                        Text(text = "Title of the recipe")
                    }
                )
                // Campo descrição da receita
                OutlinedTextField(
                    value = recipeDescription,
                    onValueChange = {recipeDescription = it},
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults
                        .colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                        ),
                    leadingIcon = {
                        Box(
                            contentAlignment = Alignment.TopStart,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TextSnippet,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary,

                                )
                        }
                    },
                    placeholder = {
                        Text(text = "A brief description of your recipe")
                    },
                    maxLines = 3
                )
                // DropDown de categorias
                Box() {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = selectedCategory.name,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(
                                    text = "Select the recipe category"
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.TextFields,
                                    contentDescription = "Categories",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults
                                .colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                ),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                println(category.name)
                                DropdownMenuItem(
                                    text = { Text(text = category.name) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = "Add box",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Difficult level",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Lista de níveis de dificuldade
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    difficultLevelList.forEach { level ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .height(40.dp)
                                .selectable(
                                    selected = difficultLevel == difficultLevel,
                                    onClick = { difficultLevel = level }
                                )
                        ) {
                            RadioButton(
                                selected = difficultLevel == level,
                                onClick = { difficultLevel = level },
                                colors = RadioButtonDefaults
                                    .colors(
                                        unselectedColor = MaterialTheme.colorScheme.primary
                                    )
                            )
                            Text(
                                text = level.description,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Timer",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cooking time",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "${cookingTime.toLong()} min",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Slider(
                    value = cookingTime,
                    onValueChange = {
                        cookingTime = it
                    },
                    valueRange = 0f..240f,
                    steps = 0
                )
            }

        }
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ){
            TextButton(
                onClick = {
                    saveNewRecipe()

                    navController!!
                        .navigate(
                        Destination
                            .AddRecipeIngredientsScreen
                            .createRoute(
                                newRecipe!!.id!!,
                                newRecipe!!.title
                            )
                    )
                }
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
@Preview
@Composable
private fun AddRecipeScreenPreview() {
    RecipesTheme {
        AddRecipeScreen(null)
    }
}