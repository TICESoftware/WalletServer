package wallet_server.attestation.repositories

import jakarta.persistence.*


@Entity(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var walletInstanceId: String,
    var firstNonce: String?,
    var secondNonce: String?
)