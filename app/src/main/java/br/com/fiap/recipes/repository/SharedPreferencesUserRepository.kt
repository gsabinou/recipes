package br.com.fiap.recipes.repository

import android.content.Context
import br.com.fiap.recipes.model.User

class SharedPreferencesUserRepository(context: Context): UserRepository {

    // só uma nota para eu não me esquecer: val é imutável, var é mutável
    private val userPrefs = context
        .getSharedPreferences(
            "user_prefs",
            Context.MODE_PRIVATE
        )

    override fun saveUser(user: User) {
        userPrefs.edit()
            .putInt("id", user.id)
            .putString("name", user.name)
            .putString("email", user.email)
            .putString("password", user.password)
            .apply()
    }

    override fun getUser(id: Int): User {
        val id = userPrefs.getInt("id", 0)
        val name = userPrefs.getString("name", "")
        val email = userPrefs.getString("email", "")
        val password = userPrefs.getString("password", "")

        return User(id, name!!, email!!, password!!)
    }



    override fun login(email: String, password: String): Boolean {
        val emailPrefs = userPrefs.getString("email", "")
        val passwordPrefs = userPrefs.getString("password", "")

        return email == emailPrefs && password == passwordPrefs
    }

    // Estas funções abaixo não serão implementadas pois não há necessidade de implementação, já que estamos usando o Room agora

    override fun update(user: User): Int {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun delete(user: User): Int {
        TODO("Not yet implemented")
    }


}