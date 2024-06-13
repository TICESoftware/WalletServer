package wallet_server.attestation.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import wallet_server.attestation.types.*

@RestController
@RequestMapping("api/attestation")
class AttestationController {

    @PostMapping("/nonces")
    fun requestNonces(@RequestBody request: NonceRequest): NonceResponse {
        val nonces = listOf("nonce1", "nonce2", "nonce3")
        return NonceResponse(nonces)
    }

    @PostMapping("/request")
    fun requestAttestation(@RequestBody request: AttestationRequest): AttestationResponse {
        return AttestationResponse(walletAttestation = "testWalletAttestation")
    }

    @PostMapping("/validation")
    fun validateAttestation(@RequestBody request: ValidationRequest): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}