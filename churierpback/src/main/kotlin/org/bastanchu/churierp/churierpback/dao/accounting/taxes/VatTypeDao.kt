package org.bastanchu.churierp.churierpback.dao.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatType
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatTypePk

interface VatTypeDao : BaseDtoDao<VatTypePk, VatType, VatTypeDto> {
}