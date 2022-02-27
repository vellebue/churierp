package org.bastanchu.churierp.churierpback.dao.administration.users

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto
import org.bastanchu.churierp.churierpback.entity.User

interface UserDao : BaseDtoDao<Int, User, UserDto> {
}