package org.bastanchu.churierp.churierpback.entity.users

import java.util.*
import javax.persistence.*;

@Entity
@Table(name = "USERS")
class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(generator = "seq_user")
    @SequenceGenerator(name = "seq_user", sequenceName = "SEQ_USERS", allocationSize = 1)
    var userId: Integer? = null;

    @Column(name = "LOGIN", nullable = false)
    var login: String? = null;

    @Column(name = "PASSWORD", nullable = false)
    var password: String? = null;

    @Column(name = "NAME", nullable = false)
    var name: String? = null;

    @Column(name = "SURNAME", nullable = false)
    var surname: String? = null;

    @Column(name = "EMAIL", nullable = true)
    var email: String? = null;

    @Column(name = "CREATION_DATE", nullable = false)
    var creationDate: Date? = null;

    @Column(name = "END_DATE")
    var endDate: Date? = null;
}