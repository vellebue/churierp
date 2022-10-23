package org.bastanchu.churierp.churierpback.service.administration.impl

import org.bastanchu.churierp.churierpback.dao.administration.users.UserDao
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto
import org.bastanchu.churierp.churierpback.entity.users.User
import org.bastanchu.churierp.churierpback.service.administration.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service("userService")
@Qualifier("userService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class UserServiceImpl (@Autowired val userDao: UserDao) : UserService {

    override fun getUserById(id: Integer): UserDto {
        val user = userDao.getById(id as Int);
        val userDto = userDao.toDataTransferObject(user);
        return userDto;
    }

    override fun getUserByLogin(login: String): UserDto {
        var userFilter = User();
        userFilter.login = login;
        var userList = userDao.filter(userFilter);
        if (userList.size == 0) {
            throw RuntimeException("Not found user for login: " + login);
        }
        else if (userList.size > 1) {
            throw RuntimeException("Multiple users found for login " + login);
        }
        else {
            return userDao.toDataTransferObject(userList.get(0));
        }
    }

    override fun filterUsers(filter: UserFilterDto): List<UserDto> {
        val users = userDao.genericFilter(filter);
        return users.map { e -> userDao.toDataTransferObject(e) };
    }

    override fun createUser(userDto: UserDto) {
        val encoder = MessageDigestPasswordEncoder("SHA-256");
        val user = userDao.fromDtoToEntity(userDto);
        user.password = encoder.encode(user.password);
        userDao.create(user);
    }

    override fun updateUser(userDto: UserDto) {
        if (userDto.userId != null) {
            val user = userDao.getById(userDto.userId as Int);
            val currentPassword = user.password;
            if (currentPassword != userDto.password) {
                val encoder = MessageDigestPasswordEncoder("SHA-256");
                val newPassword = encoder.encode(userDto.password);
                userDto.password = newPassword;
            }
            userDao.fromDtoToEntity(userDto, user);
        } else {
            throw RuntimeException("User with user id: " + userDto.userId + " to update not found");
        }
    }

    override fun deleteUser(userDto: UserDto) {
        if ((userDto != null)) {
            if (userDto.userId != null) {
                userDao.deleteById(userDto.userId as Int);
            } else {
                throw RuntimeException("userDto id is null");
            }
        } else {
            throw RuntimeException("userDto null");
        }
    }

    override fun listUsers(): List<UserDto> {
        val users = userDao.listAll();
        return users.map { u -> userDao.toDataTransferObject(u); };
    }
}