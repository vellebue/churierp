package org.bastanchu.churierp.churierpback.service.accounting.accounts.impl

import org.bastanchu.churierp.churierpback.dao.accounting.accounts.AccountDao
import org.bastanchu.churierp.churierpback.dao.accounting.accounts.AccountingKindsDao
import org.bastanchu.churierp.churierpback.dao.accounting.accounts.AccountingPlanDao
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto
import org.bastanchu.churierp.churierpback.service.accounting.accounts.AccountService
import org.bastanchu.churierp.churierpback.service.administration.CompanyService
import org.bastanchu.churierp.churierpback.service.administration.TypesSubtypesService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import java.lang.Exception

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class AccountServiceImpl(@Autowired val accountDao : AccountDao,
                         @Autowired val accountingKindsDao : AccountingKindsDao,
                         @Autowired val accountingPlanDao : AccountingPlanDao,
                         @Autowired val typesSubtypesService: TypesSubtypesService,
                         @Autowired val companiesService : CompanyService,
                         @Autowired val messageSource: MessageSource) : AccountService {

    override fun buildInitializedAccountDto(): AccountDto {
        val accountDto = AccountDto()
        accountDto.planIdMap = getPlanIdMap()
        accountDto.kindIdMap = getKindIdMap()
        accountDto.companyIdMap = getCompanyIdMap()
        accountDto.debHabMap = getDebHabMap()
        accountDto.typeIdMap = getTypeIdMap()
        accountDto.subtypeIdMap = getSubtypeIdMap()
        return accountDto
    }

    override fun fillAccountDtoMaps(accountDto: AccountDto) {
        accountDto.planIdMap = getPlanIdMap()
        accountDto.kindIdMap = getKindIdMap()
        accountDto.companyIdMap = getCompanyIdMap()
        accountDto.debHabMap = getDebHabMap()
        accountDto.typeIdMap = getTypeIdMap()
        accountDto.subtypeIdMap = getSubtypeIdMap()
    }

    override fun buildInitializedAccountFilterDto(): AccountFilterDto {
        val accountFilterDto = AccountFilterDto()
        accountFilterDto.planIdMap = getPlanIdMap()
        accountFilterDto.kindIdMap = getKindIdMap()
        accountFilterDto.companyIdMap = getCompanyIdMap()
        accountFilterDto.debHabMap = getDebHabMap()
        accountFilterDto.typeIdMap = getTypeIdMap()
        accountFilterDto.subtypeIdMap = getSubtypeIdMap()
        return accountFilterDto
    }

    override fun filterAccounts(filterDto: AccountFilterDto): List<AccountDto> {
        val accountList = accountDao.genericFilter(filterDto);
        val accounts = accountDao.toDataTransferObjectList(accountList);
        accounts.stream().forEach {
            it.companyIdStringField = it.companyId.toString()
        }
        return accounts
    }

    override fun getPlanIdMap(): Map<String, String> {
        val planesList = accountingPlanDao.listAll()
        val planesMap = planesList.map { it.planId as String to solveKey(it.key, it.description as String) }
                                  .associate { Pair(it.first, it.second) }
        return planesMap
    }

    override fun getCompanyIdMap(): Map<String, String> {
        val companiesList = companiesService.filterCompanies(CompanyFilterDto())
        val companiesMap = companiesList.map { it.companyId.toString() to it.name as String}
                                        .associate { Pair(it.first, it.second) }
        return companiesMap
    }

    override fun getKindIdMap(): Map<String, String> {
        val kindsList = accountingKindsDao.listAll()
        val kindsMap = kindsList.map { it.id as String to solveKey(it.key, it.description as String) }
            .associate { Pair(it.first, it.second) }
        return kindsMap
    }

    override fun getDebHabMap(): Map<String, String> {
        val values = listOf<Pair<String, String>>(
            Pair("D",
                 messageSource.getMessage("churierpweb.accounting.accounts.sign.D", null, LocaleContextHolder.getLocale())),
            Pair("H",
                 messageSource.getMessage("churierpweb.accounting.accounts.sign.H", null, LocaleContextHolder.getLocale())))
        val valuesMap = values.associate { Pair(it.first, it.second) }
        return valuesMap
    }

    override fun getTypeIdMap(): Map<String, String> {
        return typesSubtypesService.getTypesMap(1, 2)
    }

    override fun getSubtypeIdMap(): Map<String, Map<String, String>> {
        return typesSubtypesService.getSubtypesMap(1, 2)
    }

    override fun createAccount(accountDto: AccountDto) {
        val account = accountDao.fromDtoToEntity(accountDto)
        account.companyId = if (accountDto.companyIdStringField != null) {
            Integer.parseInt(accountDto.companyIdStringField) as Integer
        } else { accountDto.companyId }
        accountDao.create(account);
        accountDao.toDataTransferObject(account, accountDto)
    }

    override fun getAccountById(accountId: Integer): AccountDto {
        val account = accountDao.getById(accountId)
        val accountDto = accountDao.toDataTransferObject(account)
        accountDto.companyIdStringField = accountDto.companyId.toString()
        return accountDto
    }

    override fun updateAccount(accountDto: AccountDto) {
        val account = accountDao.fromDtoToEntity(accountDto)
        account.companyId = Integer.parseInt(accountDto.companyIdStringField) as Integer
    }

    override fun deleteAccount(accountId: Integer) {
        accountDao.deleteById(accountId)
    }

    private fun solveKey(key : String?, default : String) : String {
        try {
            val message = messageSource.getMessage(key, null, LocaleContextHolder.getLocale())
            if (message == "") {
                return default
            } else {
                return message
            }
        } catch (e: Exception) {
            return default
        }
    }
}