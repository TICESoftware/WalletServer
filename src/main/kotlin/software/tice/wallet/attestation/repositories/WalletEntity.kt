package software.tice.wallet.attestation.repositories

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "users")
data class WalletEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var walletId: String,
    var popNonce: String?,
    var keyAttestationNonce: String?,
    var randomId: String?,
)
