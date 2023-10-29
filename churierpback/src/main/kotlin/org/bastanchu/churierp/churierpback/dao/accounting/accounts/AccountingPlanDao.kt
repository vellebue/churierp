package org.bastanchu.churierp.churierpback.dao.accounting.accounts

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountingPlanDto
import org.bastanchu.churierp.churierpback.entity.accounting.accounts.AccountingPlan

interface AccountingPlanDao : BaseDtoDao<String, AccountingPlan, AccountingPlanDto> {
}