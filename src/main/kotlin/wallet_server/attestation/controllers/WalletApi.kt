package wallet_server.attestation.controllers

import org.springframework.web.bind.annotation.*
import wallet_server.attestation.requests.AttestationRequest
import wallet_server.attestation.requests.NonceRequest
import wallet_server.attestation.responses.AttestationResponse
import wallet_server.attestation.responses.NonceResponse

@RestController
@RequestMapping("api/attestation")
class WalletApi {

    @PostMapping("/nonces")
    fun requestNonces(@RequestBody request: NonceRequest): NonceResponse {
        val nonces = listOf("nonce1", "nonce2", "nonce3")
        return NonceResponse(nonces)
    }

    @PostMapping("/request")
    fun requestAttestation(@RequestBody request: AttestationRequest): AttestationResponse {
        return AttestationResponse(walletAttestation = "testWalletAttestation")
    }
}