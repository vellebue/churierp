package org.bastanchu.churierp.churierpback.dao.accounting.accounts

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountingKindDto
import org.bastanchu.churierp.churierpback.entity.accounting.accounts.AccountingKinds

interface AccountingKindsDao : BaseDtoDao<String, AccountingKinds, AccountingKindDto> {
}