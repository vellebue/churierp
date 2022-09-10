package org.bastanchu.churierp.churierpback.entity

import javax.persistence.*

@Entity
@Table(name = "ADDRESSES")
class Address : TraceableEntity() {

    public enum class AddressType(var type:String) {
        ADDRESS_TYPE_COMPANY("COMP");

        override fun toString(): String {
            return type
        }
    }

    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(generator = "seq_addresses")
    @SequenceGenerator(name = "seq_addresses", sequenceName = "SEQ_ADDRESSES", allocationSize = 1)
    var addressId : Integer? = null

    @Column(name = "TYPE_ID")
    var type: String? = null

    @Column(name = "ADDRESS")
    var address: String? = null

    @Column(name = "POSTAL_CODE")
    var postalCode : String? = null

    @Column(name = "CITY")
    var city : String? = null

    @Column(name = "COUNTRY_ID")
    var countryId : String? = null

    @Column(name = "REGION_ID")
    var regionId : String? = null

}