package org.bastanchu.churierp.churierpback.entity

import javax.persistence.*

@Entity
@Table(name = "C_LANGUAGES")
class Language {

    @Id
    @Column(name = "ID")
    var id : Integer? = null

    @Column(name = "LANG_ID")
    var langId : String? = null

    @Column(name = "COUNTRY_ID")
    var countryId : String? = null

    @Column(name = "LANGUAGE_KEY")
    var languageKey : String? = null

}