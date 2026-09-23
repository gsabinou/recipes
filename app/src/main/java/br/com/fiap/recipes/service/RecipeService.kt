package br.com.fiap.recipes.service

import br.com.fiap.recipes.model.Ingredient
import br.com.fiap.recipes.model.PreparationMethod
import br.com.fiap.recipes.model.Recipe
import br.com.fiap.recipes.model.RecipeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface RecipeService {

    @GET("recipes/categories/{categoryId}")
    fun getRecipesByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("recipes/recents")
    fun getLatestRecipes(): Call<List<Recipe>>

    @POST("recipes")
    suspend fun saveRecipe(@Body recipeRequest: RecipeRequest): RecipeRequest

    @POST("recipes/{recipeId}/ingredients")
    suspend fun saveRecipeIngredients(
        @Path("recipeId") recipeId: Int,
        @Body ingredients: List<Ingredient>
    ): List<Ingredient>

    @POST("recipes/{recipeId}/preparation-methods")
    suspend fun savePreparationMethods(
        @Path("recipeId") recipeId: Int,
        @Body preparationMethods: List<PreparationMethod>
    ): List<PreparationMethod>
}