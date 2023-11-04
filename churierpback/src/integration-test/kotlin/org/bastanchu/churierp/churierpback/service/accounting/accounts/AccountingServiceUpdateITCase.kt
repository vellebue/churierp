package org.bastanchu.churierp.churierpback.service.accounting.accounts

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto

import org.springframework.beans.factory.annotation.Autowired

import java.sql.Connection
import javax.sql.DataSource

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AccountingServiceUpdateITCase (@Autowired val accountService: AccountService) : BaseContainerDBITCase() {

    override fun getScriptContent(): String {
        return """
            --Address company and test company
            INSERT INTO public.addresses
            (address_id, type_id, creation_user, creation_time, update_user, update_time, address, postal_code, city, country_id, region_id)
            VALUES(0, 'COMP', 'angel', '2023-11-04 14:40:45.969', 'angel', '2023-11-04 14:40:45.969', 'Folsom Street 47 San Francisco CA', '41423312', 'San Francisco', 'ES', '28');
            INSERT INTO COMPANIES
            (company_id, "name", social_name, vat_number, creation_user, creation_time, update_user, update_time, address_id)
            VALUES(0, 'Churigroup Enterprises', 'Churigroup Enterprises Public CO', '53108437B', 'angel', '2023-11-04 14:40:45.968', 'angel', '2023-11-04 14:40:45.968', 0);
            --Testing types
            INSERT INTO TYPED_TYPES
            (area_id, entity_id, type_id, "key", description, manageable)
            VALUES(1, 2, 'ACC_CLI_PR', NULL, 'Clients & Providers', true);
            INSERT INTO TYPED_TYPES
            (area_id, entity_id, type_id, "key", description, manageable)
            VALUES(1, 2, 'ACC_SALES', NULL, 'Sales', true);
            --Testing subtypes
            INSERT INTO TYPED_SUBTYPES
            (area_id, entity_id, type_id, subtype_id, "key", description, manageable)
            VALUES(1, 2, 'ACC_CLI_PR', 'ACC_CLI', NULL, 'Clients Merchant Ops.', true);
            INSERT INTO TYPED_SUBTYPES
            (area_id, entity_id, type_id, subtype_id, "key", description, manageable)
            VALUES(1, 2, 'ACC_SALES', 'ACC_SAL_G', NULL, 'Sales Gen. Ops.', true);
            --Test Accounting accounts
            INSERT INTO public.accounting_accounts
            (acc_id, plan_id, company_id, creation_user, creation_time, update_user, update_time, kind_id, deb_hab, account, description, type_id, subtype_id, qualified)
            VALUES(0, 'NPGC', 0, 'angel', '2023-11-04 15:17:22.098', 'angel', '2023-11-04 15:17:22.098', 'X', 'H', '4300000000', 'Clients Gen. Op.', 'ACC_CLI_PR', 'ACC_SAL_G', true);
            INSERT INTO public.accounting_accounts
            (acc_id, plan_id, company_id, creation_user, creation_time, update_user, update_time, kind_id, deb_hab, account, description, type_id, subtype_id, qualified)
            VALUES(1, 'NPGC', 0, 'angel', '2023-11-04 15:18:12.786', 'angel', '2023-11-04 15:18:12.786', 'X', 'H', '4300000001', 'Clients Special Op.', 'ACC_CLI_PR', 'ACC_SAL_G', true);
            INSERT INTO public.accounting_accounts
            (acc_id, plan_id, company_id, creation_user, creation_time, update_user, update_time, kind_id, deb_hab, account, description, type_id, subtype_id, qualified)
            VALUES(2, 'NPGC', 0, 'angel', '2023-11-04 15:18:55.094', 'angel', '2023-11-04 15:18:55.094', 'P', 'D', '7000000000', 'Sales Gen. Op.', 'ACC_SALES', 'ACC_SAL_G', true);
            --Increment account sequence to make sequence room
            select nextval('SEQ_AC_ACCOUNTS'); 
            select nextval('SEQ_AC_ACCOUNTS'); 
            select nextval('SEQ_AC_ACCOUNTS');                                                             
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldRetrieveAnAccountProperly() {
        val accountDto = accountService.getAccountById(0 as Integer)
        assertEquals(0, accountDto.accountId)
        assertEquals("NPGC", accountDto.planId)
        assertEquals(0, accountDto.companyId)
        assertEquals("X",accountDto.kindId)
        assertEquals("H", accountDto.debHab)
        assertEquals("4300000000", accountDto.account)
        assertEquals("Clients Gen. Op.", accountDto.description)
        assertEquals("ACC_CLI_PR", accountDto.typeId)
        assertEquals("ACC_SAL_G", accountDto.subtypeId)
        assertEquals(true, accountDto.qualified)
    }

    @Test
    fun shouldUpdateAnAccountProperly() {
        val accountDtoToUpdate = getAccountDtoById(1 as Integer)
        accountDtoToUpdate!!.description = "Clients Special Op. Updated"
        accountDtoToUpdate.debHab = "D"
        accountDtoToUpdate.typeId = "ACC_SALES"
        accountDtoToUpdate.subtypeId = "ACC_SAL_G"
        accountService.updateAccount(accountDtoToUpdate)
        val accountDtoUpdated = getAccountDtoById(1 as Integer)
        assertEqualsAccountDto(accountDtoToUpdate, accountDtoUpdated!!)
    }
    @Test
    fun shouldDeleteAnAccountProperly() {
        val accountDtoToDelete = getAccountDtoById(2 as Integer)
        accountService.deleteAccount(accountDtoToDelete!!.accountId!!)
        val accountDtoToDeleteLater = getAccountDtoById(2 as Integer)
        assertNull(accountDtoToDeleteLater)
    }

    @Test
    fun shouldCreateAnAccountProperly() {
        val accountDto =  AccountDto()
        accountDto.planId = "NPGC"
        accountDto.companyIdStringField = "0"
        accountDto.kindId = "P"
        accountDto.debHab = "D"
        accountDto.account = "7000000001"
        accountDto.description = "Sales Special Op."
        accountDto.typeId = "ACC_SALES"
        accountDto.subtypeId = "ACC_SAL_G"
        accountDto.qualified = true
        accountService.createAccount(accountDto)
        val accountDDBBDto = getAccountDtoById(accountDto.accountId!!)
        assertEqualsAccountDto(accountDto, accountDDBBDto!!)
    }

    private fun assertEqualsAccountDto(expectedAccountDto: AccountDto, testedAccountDto: AccountDto) {
        assertEquals(expectedAccountDto.planId, testedAccountDto.planId)
        assertEquals(expectedAccountDto.companyId, testedAccountDto.companyId)
        assertEquals(expectedAccountDto.kindId, testedAccountDto.kindId)
        assertEquals(expectedAccountDto.debHab, testedAccountDto.debHab)
        assertEquals(expectedAccountDto.account, testedAccountDto.account)
        assertEquals(expectedAccountDto.description, testedAccountDto.description)
        assertEquals(expectedAccountDto.typeId, testedAccountDto.typeId)
        assertEquals(expectedAccountDto.subtypeId, testedAccountDto.subtypeId)
        assertEquals(expectedAccountDto.qualified, testedAccountDto.qualified)
    }

    private fun getAccountDtoById(accountId : Integer) : AccountDto? {
        val sql = """
            select acc_id, plan_id, company_id, creation_user, creation_time, update_user, update_time,
                   kind_id, deb_hab, account, description, type_id, subtype_id, qualified
            from ACCOUNTING_ACCOUNTS where acc_id = ?
        """.trimIndent()
        var accountDto : AccountDto? = null
        val statement = dataSource!!.connection.prepareStatement(sql)
        statement.setInt(1, accountId.toInt())
        statement.use {
            val resultSet = it.executeQuery()
            resultSet.use {
                if (it.next()) {
                    accountDto = AccountDto()
                    accountDto!!.accountId = it.getInt("acc_id") as Integer
                    accountDto!!.planId = it.getString("plan_id")
                    accountDto!!.companyId = it.getInt("company_id") as Integer
                    accountDto!!.companyIdStringField = accountDto!!.companyId.toString()
                    accountDto!!.kindId = it.getString("kind_id")
                    accountDto!!.debHab = it.getString("deb_hab")
                    accountDto!!.account = it.getString("account")
                    accountDto!!.description = it.getString("description")
                    accountDto!!.typeId = it.getString("type_id")
                    accountDto!!.subtypeId = it.getString("subtype_id")
                    accountDto!!.qualified = it.getBoolean("qualified")
                }
            }
        }
        return accountDto
    }
}