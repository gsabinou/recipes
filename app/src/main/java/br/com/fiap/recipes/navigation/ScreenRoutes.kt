package br.com.fiap.recipes.navigation

sealed class Destination(val route: String){

    object InitialScreen: Destination("initial")

    object HomeScreen: Destination("home/{email}") {
        fun createRoute(email: String): String {
            return "home/$email"
        }
    }

    object SignUpScreen: Destination("signup")
    object LoginScreen: Destination("login")
    object AddRecipeScreen: Destination("addRecipe")

    object ProfileScreen: Destination("profile/{email}") {
        fun createRoute(email: String): String {
            return "profile/$email"
        }
    }


    object CategoryRecipeScreen: Destination("category/{categoryId}") {
        fun createRoute(categoryId: Int): String {
            return "category/$categoryId"
        }
    }

    // rota para a tela de cadastro de ingredientes da receita
    // passando o id e nome da receita como argumentos da rota
    object AddRecipeIngredientsScreen : Destination(
        route = "addIngredients/{recipeId}/{recipeName}"
    ) {
        fun createRoute(
            recipeId: Int,
            recipeName: String
        ): String {
            return "addIngredients/$recipeId/$recipeName"
        }
    }

    // Rota para a tela de cadastro de modos de preparo da receita
    // passando o id e nome da receita como argumentos da rota
    object AddPreparationMethodsScreen : Destination(
        route = "addPreparationMethods/{recipeId}/{recipeName}"
    ) {
        fun createRoute(
            recipeId: Int,
            recipeName: String
        ): String {
            return "addPreparationMethods/$recipeId/$recipeName"
        }
    }

}