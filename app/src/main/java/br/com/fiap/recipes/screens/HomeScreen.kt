package br.com.fiap.recipes.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.components.CategoryItem
import br.com.fiap.recipes.components.RecipeItem
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.RoomUserRepository
import br.com.fiap.recipes.repository.getAllCategories
import br.com.fiap.recipes.repository.getLatestRecipes
import br.com.fiap.recipes.ui.theme.RecipesTheme
import br.com.fiap.recipes.utils.convertByteArrayToBitmap

@Composable
fun HomeScreen(navController: NavHostController, email: String?) {
    Surface(
        // Surface é similar a box, ele apenas tem algumas diferenças
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {MyTopAppBar(email!!, navController)}, // double bang é o nome de duas exclamações, com ele a gente tá garantindo ao kotlin q n é nulo
            bottomBar = {MyBottomAppBar()},
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Destination.AddRecipeScreen.route)
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_button)
                    )
                }
            }
        ) { paddingValues ->

            ContentScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    RecipesTheme {
        HomeScreen(rememberNavController(), "mock@email.com")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(email: String = "", navController: NavController) {

    val userRepository =
        RoomUserRepository(LocalContext.current)

    val user = userRepository.getUserByEmail(email)

    var profileBitmap by remember {
        mutableStateOf<Bitmap>(
            convertByteArrayToBitmap(user!!.userImage!!)
        )
    }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier
//                    .padding(16.dp) O padding já está sendo aplicado no componente TopAppBar
                    // o padding deve ser colocado antes do tamanho
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${stringResource(R.string.home_title)}${user!!.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                Card(
                    shape = CircleShape,
                    border = BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    colors = CardDefaults
                        .cardColors(
                            containerColor = Color.Transparent
                        ),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            onClick = {
                                navController.navigate(Destination
                                    .ProfileScreen
                                    .createRoute(email))
                            }
                        )
                ) {
                    Image(
                        bitmap = profileBitmap.asImageBitmap(),
                        contentDescription = stringResource(R.string.user_image),
                         contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun MyTopAppBarPreview() {
    RecipesTheme {
        MyTopAppBar("", rememberNavController())
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MyBottomAppBar() {
    val items = listOf(
        BottomNavItem(stringResource(R.string.home), icon = Icons.Default.Home),
        BottomNavItem(stringResource(R.string.favorites), icon = Icons.Default.Favorite),
        BottomNavItem(stringResource(R.string.profile), icon = Icons.Default.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun MyBottomAppBarPreview() {
    RecipesTheme {
        MyBottomAppBar()
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier,
                  navController: NavHostController ) {

    // variable that will store all the categories from the list
    val categories = getAllCategories()

    val latestRecipes = getLatestRecipes()

    var searchState by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
//            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = searchState,
            onValueChange = {searchValue ->
                searchState = searchValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_text),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .height(116.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.cooking_card),
                contentDescription = stringResource(R.string.man_cooking),
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop

            )
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) {category ->
                CategoryItem(
                    category = category,
                    onClick = {
                        navController.navigate(
                            route = Destination
                                .CategoryRecipeScreen
                                .createRoute(category.id)
                    )
                })
            }
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.newly_added_recipes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary

        )

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(latestRecipes) { recipe ->
                RecipeItem(recipe)
            }
        }

    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ContentScreenPreview() {
    RecipesTheme {
        ContentScreen(navController = rememberNavController())
    }
}

