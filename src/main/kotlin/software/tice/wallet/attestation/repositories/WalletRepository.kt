package software.tice.wallet.attestation.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface WalletRepository : JpaRepository<WalletEntity, Long> {
    fun findByWalletId(walletId: String): WalletEntity?
}

