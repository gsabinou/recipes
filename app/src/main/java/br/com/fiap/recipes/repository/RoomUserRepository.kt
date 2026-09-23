package br.com.fiap.recipes.repository

import android.content.Context
import br.com.fiap.recipes.dao.RecipeDatabase
import br.com.fiap.recipes.model.User

class RoomUserRepository(context: Context): UserRepository {

    private val userDao = RecipeDatabase.getDatabase(context).userDao()

    override fun saveUser(user: User) {
        userDao.save(user)
    }

    override fun getUser(id: Int): User {
        return userDao.getUserById(id) ?: User()
    }

    override fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    override fun login(email: String, password: String): Boolean {
        val user = userDao.login(email, password)
        return user != null
    }

    override fun update(user: User): Int {
        return userDao.update(user)
    }

    override fun delete(user: User): Int {
        return userDao.delete(user)
    }

}