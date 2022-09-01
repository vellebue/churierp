package org.bastanchu.churierp.churierpback.service.administration

import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto

interface UserService {

    fun getUserById(id:Integer): UserDto;

    fun getUserByLogin(login:String): UserDto;

    fun filterUsers(filter: UserFilterDto):List<UserDto>

    fun createUser(userDto: UserDto);

    fun updateUser(userDto: UserDto);

    fun deleteUser(userDto: UserDto);

    fun listUsers():List<UserDto>
}