package org.bastanchu.churierp.churierpback.dao.accounting.accounts

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto
import org.bastanchu.churierp.churierpback.entity.accounting.accounts.Account

interface AccountDao : BaseDtoDao<Integer, Account, AccountDto> {
}