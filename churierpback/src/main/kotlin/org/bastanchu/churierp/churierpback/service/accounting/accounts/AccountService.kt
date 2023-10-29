package org.bastanchu.churierp.churierpback.service.accounting.accounts

import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto

interface AccountService {

    /**
     * Builds an <code>AccountDto</code> instance whose data map fields
     * are initialized. See below.
     * <ul>
     *     <li>planIdMap</li>
     *     <li>companyIdMap</li>
     *     <li>kindIdMap</li>
     *     <li>debHabMap</li>
     *     <li>typeIdMap</li>
     *     <li>subtypeIdMap</li>
     * </ul>
     *
     * @return An <code>AccountDto</code> that initializes map fields.
     *
     */
    fun buildInitializedAccountDto() : AccountDto

    /**
     * Fills <code>accountDto</code> map fields as shown below.
     * <ul>
     *     <li>planIdMap</li>
     *     <li>companyIdMap</li>
     *     <li>kindIdMap</li>
     *     <li>debHabMap</li>
     *     <li>typeIdMap</li>
     *     <li>subtypeIdMap</li>
     * </ul>
     *
     * @param accountDto An accountDto object fields are being populated.
     *
     */
    fun fillAccountDtoMaps(accountDto : AccountDto)

    /**
     * Builds an <code>AccountFilterDto</code> instance whose data map fields
     * are initialized. See below.
     * <ul>
     *     <li>planIdMap</li>
     *     <li>companyIdMap</li>
     *     <li>kindIdMap</li>
     *     <li>debHabMap</li>
     *     <li>typeIdMap</li>
     *     <li>subtypeIdMap</li>
     * </ul>
     *
     * @return An <code>AccountFilterDto</code> that initializes map fields.
     *
     */
    fun buildInitializedAccountFilterDto() : AccountFilterDto

    fun filterAccounts(filterDto : AccountFilterDto) : List<AccountDto>

    fun getPlanIdMap() : Map<String, String>

    fun getCompanyIdMap() : Map<String, String>

    fun getKindIdMap() : Map<String, String>

    fun getDebHabMap() : Map<String, String>

    fun getTypeIdMap() : Map<String, String>

    fun getSubtypeIdMap() : Map<String, Map<String, String>>

    fun createAccount(accountDto : AccountDto)

    fun getAccountById(accountId : Integer) : AccountDto

    fun updateAccount(accountDto: AccountDto)

    fun deleteAccount(accountId : Integer)
}