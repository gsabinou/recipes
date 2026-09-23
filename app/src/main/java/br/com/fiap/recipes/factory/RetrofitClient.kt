package br.com.fiap.recipes.factory

import br.com.fiap.recipes.service.CategoryService
import br.com.fiap.recipes.service.RecipeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Criamos este arquivo como um object para garantir que ele será um singleton, ou seja,
// uma única instância será criada durante a execução do aplicativo, garantindo assim uma
// única conexão do servidor sem necessidade de ser chamada mais de uma vez

object RetrofitClient {

    // http://localhost:8080/api/
    // O link abaixo é o endereço que o emulador irá ler e identificar que é o endereço do servidor,
    // após o ler, o android irá entender que o endereço é externo (ou seja, na máquina hospedeira)
    const val BASE_URL = "http://10.0.2.2:8080/api/"

    // Esse "lazy" garante que essa instância seja executada apenas quando for necessária e apenas
    // uma única vez, ela será armazenada para ser reutilizada nas chamadas seguintes
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getCategoryService(): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    fun getRecipeService(): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }

}