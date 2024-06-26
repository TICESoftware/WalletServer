package software.tice.wallet.attestation.repositories

import jakarta.persistence.*
import java.util.*


@Entity(name = "users")
data class WalletEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var walletId: String,
    var popNonce: String?,
    var keyAttestationNonce: String?
)