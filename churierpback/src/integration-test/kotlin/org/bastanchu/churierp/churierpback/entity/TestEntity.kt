package org.bastanchu.churierp.churierpback.entity

import java.util.*
import javax.persistence.*

//@Entity
//@Table(name = "TEST_ENTITIES")
@Deprecated("New Test structure developed")
class TestEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "seq_test_entities")
    @SequenceGenerator(name = "seq_test_entities", sequenceName = "SEQ_TEST_ENTITIES", allocationSize = 1)
    var id:Integer? = null

    @Column(name = "TEXT")
    var text:String? = null

    @Column(name = "EVENT_DATE" )
    var eventDate: Date? = null;
}