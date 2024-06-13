package wallet_server.attestation.controllers

import org.springframework.web.bind.annotation.*
import wallet_server.attestation.requests.AttestationRequest
import wallet_server.attestation.requests.NonceRequest
import wallet_server.attestation.responses.AttestationResponse
import wallet_server.attestation.responses.NonceResponse
import wallet_server.attestation.services.WalletApiService

@RestController
@RequestMapping("api/attestation")

class WalletApi(val walletApiService: WalletApiService) {

    @PostMapping("/nonces")
    fun requestNonces(@RequestBody request: NonceRequest): NonceResponse {
        return walletApiService.requestNonces(request.walletInstanceId)
    }

    @PostMapping("/request")
    fun requestAttestation(@RequestBody request: AttestationRequest): AttestationResponse {
        return walletApiService.requestAttestation(request)
    }
}