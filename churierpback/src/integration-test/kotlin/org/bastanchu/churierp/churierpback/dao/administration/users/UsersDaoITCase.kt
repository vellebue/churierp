package org.bastanchu.churierp.churierpback.dao.administration.users

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.entity.administration.users.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

import org.junit.jupiter.api.Assertions.*
import javax.sql.DataSource

class UsersDaoITCase (@Autowired val userDao: UserDao) : BaseContainerDBITCase() {

    override fun getScriptContent(): String {
        return """
            INSERT INTO public.users
                (user_id, creation_user, creation_time, update_user, update_time, login, "password", "name", surname, email, creation_date, end_date, language_id)
                VALUES (1, 'angel', '2023-03-04 16:45:15.260', 'angel', '2023-03-04 16:45:15.260', 'cristina', 
                '{I8Ry0i3/aMkouu56VhxxLr48LRdvpOFSgcUiGv8sg9k=}a5893db93330896ebb55a3a16db6bc40e181df10e7e651c213b0e31878b08e18', 
                'Cristina', 'Vallejo Vicente', 'crispi@gmail.com', '2023-03-04', NULL, 0);
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldDeleteUserProperly() {
        insertUserToDelete()
        val userFilter = User()
        userFilter.login = "fernando"
        val users = userDao.filter(userFilter)
        assertEquals(1, users.size)
        val user = users.get(0)
        assertEquals("fernando", user.login)
        userDao.delete(user)
        userDao.flush()
        val usersAfterDeletion = userDao.filter(userFilter)
        assertEquals(0, usersAfterDeletion.size)
    }

    @Test
    fun shouldDeleteWithNullKeyValueHaveNoEffect() {
        userDao.delete(User())
        userDao.flush()
        val users = userDao.listAll()
        val usersMap = users.associate { it.login to it }
        assertNotNull(usersMap.get("angel"))
        assertNotNull(usersMap.get("cristina"))
    }

    @Test
    fun shouldGetAllUsersProperlyWithEmptyFieldForGenericFilter() {
        val userFilter = User()
        userFilter.login = ""
        val users = userDao.genericFilter(userFilter)
        assertEquals(2, users.size)
        val usersMap = users.associate { it.login to it }
        assertNotNull(usersMap.get("angel"))
        assertNotNull(usersMap.get("cristina"))
    }

    protected fun insertUserToDelete() {
        val sql = """
            INSERT INTO public.users
                (user_id, creation_user, creation_time, update_user, update_time, login, "password", "name", surname, email, creation_date, end_date, language_id)
                VALUES (2, 'angel', '2023-03-04 16:45:15.260', 'angel', '2023-03-04 16:45:15.260', 'fernando', 
                '{I8Ry0i3/aMkouu56VhxxLr48LRdvpOFSgcUiGv8sg9k=}a5893db93330896ebb55a3a16db6bc40e181df10e7e651c213b0e31878b08e18', 
                'Fernando', 'Gomez Lafuente', 'fernan@gmail.com', '2023-03-04', NULL, 0);
            COMMIT;
        """.trimIndent()
        val connection = dataSource!!.connection
        connection.use {
            val con = it
            val statement = con.prepareStatement(sql)
            statement.use {
                it.execute()
            }
        }
    }
}