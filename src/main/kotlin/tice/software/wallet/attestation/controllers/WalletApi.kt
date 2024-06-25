package tice.software.wallet.attestation.controllers

import org.springframework.web.bind.annotation.*
import tice.software.wallet.attestation.requests.AttestationRequest
import tice.software.wallet.attestation.requests.NonceRequest
import tice.software.wallet.attestation.responses.AttestationResponse
import tice.software.wallet.attestation.responses.NonceResponse
import tice.software.wallet.attestation.services.WalletApiService

@RestController
@RequestMapping("attestation")

class WalletApi(val walletApiService: WalletApiService) {

    @PostMapping("/nonces")
    fun requestNonces(@RequestBody request: NonceRequest): NonceResponse {
        return walletApiService.requestNonces(request.walletInstanceId)
    }

    @PostMapping("/request/{id}")
    fun requestAttestation(
        @RequestBody request: AttestationRequest,
        @PathVariable id: String
    ): AttestationResponse {
        return walletApiService.requestAttestation(request, id)
    }
}