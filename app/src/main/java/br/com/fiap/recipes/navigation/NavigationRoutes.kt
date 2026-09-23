package br.com.fiap.recipes.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import br.com.fiap.recipes.screens.AddPreparationMethodsScreen
import br.com.fiap.recipes.screens.AddRecipeIngredientsScreen
import br.com.fiap.recipes.screens.AddRecipeScreen
import br.com.fiap.recipes.screens.CategoryRecipeScreen
import br.com.fiap.recipes.screens.HomeScreen
import br.com.fiap.recipes.screens.InitialScreen
import br.com.fiap.recipes.screens.LoginScreen
import br.com.fiap.recipes.screens.ProfileScreen
import br.com.fiap.recipes.screens.SignUpScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()
    //  val são imutáveis,assim como constantes
    NavHost(
        navController = navController,
        startDestination = Destination.InitialScreen.route
    ){
        composable(Destination.InitialScreen.route){
            InitialScreen(navController)
        }

        composable(
            route = Destination.HomeScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://recipes.fiap.com.br/email/{email}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ){backStackEntry ->
            var email = backStackEntry.arguments?.getString("email")
            HomeScreen(navController, email)
        }

        composable(Destination.SignUpScreen.route){
            SignUpScreen(navController)
        }

        composable(
            route = Destination.ProfileScreen.route,
            arguments = listOf(
                navArgument(name = "email"){
                    type = NavType.StringType
                }
            )
        ){backStackEntry ->
            var email = backStackEntry.arguments?.getString("email")
            ProfileScreen(navController, email)
        }

        composable(Destination.LoginScreen.route){
            LoginScreen(navController)
        }

        composable(Destination.AddRecipeScreen.route){
            AddRecipeScreen(navController)
        }

        composable(
            route = Destination.CategoryRecipeScreen.route,
            arguments = listOf(navArgument("categoryId"){
                type = NavType.IntType
            })
        ){backStackEntry ->
            var categoryId = backStackEntry.arguments?.getInt("categoryId")
            CategoryRecipeScreen(navController, categoryId)
        }

        // Adicionar tela de adição de ingredientes no sistema de navegação.
        composable(
            route = Destination.AddRecipeIngredientsScreen.route,
            arguments = listOf(
                navArgument(
                    name = "recipeId"
                ) { type = NavType.IntType },
                navArgument(
                    name = "recipeName"
                ) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            var recipeId = backStackEntry.arguments?.getInt("recipeId")
            var recipeName = backStackEntry.arguments?.getString("recipeName")
            AddRecipeIngredientsScreen(
                navController,
                recipeId,
                recipeName
            )
        }

        // Adicionar a rota para a tela de modos de preparo
        composable(
            route = Destination.AddPreparationMethodsScreen.route,
            arguments = listOf(
                navArgument(
                    name = "recipeId"
                ) { type = NavType.IntType },
                navArgument(
                    name = "recipeName"
                ) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            var recipeId = backStackEntry.arguments?.getInt("recipeId")
            var recipeName = backStackEntry.arguments?.getString("recipeName")
            AddPreparationMethodsScreen(
                navController,
                recipeId,
                recipeName
            )
        }

    }
}
