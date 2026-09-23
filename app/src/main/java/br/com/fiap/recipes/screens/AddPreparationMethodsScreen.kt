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
import br.com.fiap.recipes.model.PreparationMethod
import br.com.fiap.recipes.repository.savePreparationMethods
import br.com.fiap.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPreparationMethodsScreen(
    navController: NavHostController?,
    recipeId: Int?,
    recipeName: String?
) {

    var methods = remember {
        mutableStateListOf<PreparationMethod>()
    }

    var method by remember {
        mutableStateOf("")
    }

    var methodNumber by remember {
        mutableIntStateOf(0)
    }

    // Criar o escopo de coroutine que será utilizado
    // para chamar a gravação dos métodos de preparo da receita
    val scope = rememberCoroutineScope()

    // Lista que receberá a lista de
    // métodos de preparo retornados pela API
    var newMethods: List<PreparationMethod> by remember {
        mutableStateOf(listOf())
    }

    // Função lambda para preparar e enviar
    // os dados para o servidor
    val saveNewMethods: () -> Unit = {
        scope.launch {
            val methodsToSend = methods.map{
                it.copy(id = null)
            }
            newMethods = savePreparationMethods(
                recipeId = recipeId!!,
                preparationMethods = methodsToSend
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
                    text = "Step 3...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Now, let's add the cooking instructions. Be super detailed!",
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
                        text = "Preparation Method",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Campo título da receita
                OutlinedTextField(
                    value = method,
                    onValueChange = { method = it },
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
                        Text(text = "Instructions step here...")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                methodNumber = methods.size + 1
                                methods.add(PreparationMethod(methodNumber, method))
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
                    items(methods) { method ->
                        PreparationMethodItem(
                            onClick = {
                                // Removemos o metodo de preparo da lista
                                methods.remove(method)

                                // Atualizamos a ordem dos métodos de preparo
                                val reorderedList = methods.mapIndexed { index, item ->
                                    item.copy(id = index + 1)
                                }
                                // Atualizamos a lista de modos de preparo
                                methods.clear()
                                methods.addAll(reorderedList)
                            },
                            method
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextButton(
                    onClick = saveNewMethods
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
fun PreparationMethodItem(
    onClick: () -> Unit, preparationMethod: PreparationMethod
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
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
                    text = preparationMethod.id.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Text(
                text = preparationMethod.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(2f)
            )
            IconButton(
                onClick = {
                    onClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete preparation method",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreparationMethodItemPreview() {
    RecipesTheme {
        PreparationMethodItem(
            onClick = {},
            preparationMethod = PreparationMethod(22, "Teste")
        )
    }
}

@Preview
@Composable
private fun AddPreparationMethodsScreenPreview() {
    RecipesTheme {
        AddPreparationMethodsScreen(
            navController = null,
            recipeId = 0,
            recipeName = "Recipe name")
    }
}