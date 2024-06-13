package wallet_server.attestation.services

import org.springframework.stereotype.Service
import wallet_server.attestation.requests.AttestationRequest
import wallet_server.attestation.responses.AttestationResponse
import wallet_server.attestation.responses.NonceResponse

@Service
class WalletApiService {

    fun requestNonces(walletInstanceId: String): NonceResponse {
        val nonces = listOf("nonce1", "nonce2", "nonce3")
        return NonceResponse(nonces)
    }

    fun requestAttestation(requestAttestation: AttestationRequest): AttestationResponse {
        return AttestationResponse(walletAttestation = "testWalletAttestation")
    }
}