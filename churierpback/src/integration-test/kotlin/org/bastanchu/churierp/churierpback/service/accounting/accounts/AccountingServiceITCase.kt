package org.bastanchu.churierp.churierpback.service.accounting.accounts

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto

import org.springframework.beans.factory.annotation.Autowired

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AccountingServiceITCase(@Autowired val accountService: AccountService) :BaseContainerDBITCase() {


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
            --Test custom account plan
            INSERT INTO C_ACCOUNTING_PLANS
            (plan_id, country_id, "key", description)
            VALUES('NPGP', 'ES', '', 'Special Acc. Plan For Spain');
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldBuildInitializedAccountDtoReturnInitializedMapsProperly() {
        val accountDto = accountService.buildInitializedAccountDto()
        assertAccountMaps(accountDto.planIdMap!!, accountDto.kindIdMap!!, accountDto.companyIdMap!!,
                          accountDto.typeIdMap!!, accountDto.subtypeIdMap!!)
    }

    @Test
    fun shouldInitializedAccountDtoMapsProperly() {
        val accountDto = AccountDto()
        accountService.fillAccountDtoMaps(accountDto)
        assertAccountMaps(accountDto.planIdMap!!, accountDto.kindIdMap!!, accountDto.companyIdMap!!,
            accountDto.typeIdMap!!, accountDto.subtypeIdMap!!)
    }

    @Test
    fun shouldBuildInitializedAccountFilterDtoReturnInitializedMapsProperly() {
        val accountFilterDto = accountService.buildInitializedAccountFilterDto()
        assertAccountMaps(accountFilterDto.planIdMap!!, accountFilterDto.kindIdMap!!, accountFilterDto.companyIdMap!!,
            accountFilterDto.typeIdMap!!, accountFilterDto.subtypeIdMap!!)
    }

    @Test
    fun shouldFilterAccountsProperly() {
        val accountFilterDto = AccountFilterDto()
        accountFilterDto.typeId = "ACC_CLI_PR"
        accountFilterDto.subtypeId = "ACC_SAL_G"
        val accountsList = accountService.filterAccounts(accountFilterDto)
        assertEquals(2, accountsList.size, "According to DB initialization data there should be two accounts")
        val firstAccount = accountsList.filter { it.account == "4300000000" }
        assertNotNull(firstAccount)
        val secondAccount = accountsList.filter { it.account == "4300000001" }
        assertNotNull(secondAccount)
    }

    @Test
    fun shouldFilterAccountsWithZeroResultsProperly() {
        val accountFilterDto = AccountFilterDto()
        accountFilterDto.planId = "NPGP"
        val accountsList = accountService.filterAccounts(accountFilterDto)
        assertEquals(0, accountsList.size, "There should be zero accounts under NPGP plan")
    }

    private fun assertAccountMaps(planesMap : Map<String,String>, kindsMap : Map<String,String>,
                                  companiesMap : Map<String, String>, typesMap : Map<String, String>,
                                  subtypesMap : Map<String, Map<String, String>>) {
        assertTrue(planesMap.containsKey("NPGC"))
        assertTrue(kindsMap.containsKey("X"))
        assertTrue(kindsMap.containsKey("P"))
        assertTrue(kindsMap.containsKey("A"))
        assertTrue(companiesMap.containsKey("0"))
        assertTrue(typesMap.containsKey("ACC_CLI_PR"))
        assertTrue(subtypesMap.containsKey("ACC_CLI_PR"))
        val subtypesFirstMap = subtypesMap.values.stream().filter { it.containsKey("ACC_CLI") }.findFirst().get()
        assertTrue(subtypesFirstMap.containsKey("ACC_CLI"))
    }
}