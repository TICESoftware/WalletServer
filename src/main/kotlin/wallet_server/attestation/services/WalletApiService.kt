package wallet_server.attestation.services

import org.springframework.stereotype.Service
import wallet_server.attestation.repositories.UserEntity
import wallet_server.attestation.repositories.UserRepository
import wallet_server.attestation.requests.AttestationRequest
import wallet_server.attestation.responses.AttestationResponse
import wallet_server.attestation.responses.NonceResponse

@Service
class WalletApiService(private val userRepository: UserRepository) {

    fun requestNonces(walletInstanceId: String): NonceResponse {
        val nonces = List(2) { java.util.UUID.randomUUID().toString() }

        val user = UserEntity(
            walletInstanceId = walletInstanceId,
            firstNonce = nonces[0],
            secondNonce = nonces[1],
            id = null
        )

        userRepository.save(user)
        return NonceResponse(nonces)
    }

    fun requestAttestation(requestAttestation: AttestationRequest): AttestationResponse {
        return AttestationResponse(walletAttestation = "testWalletAttestation")
    }
}