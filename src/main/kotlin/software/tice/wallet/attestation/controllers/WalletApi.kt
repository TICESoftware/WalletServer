package software.tice.wallet.attestation.controllers

import org.springframework.web.bind.annotation.*
import software.tice.wallet.attestation.requests.AttestationRequest
import software.tice.wallet.attestation.requests.NonceRequest
import software.tice.wallet.attestation.responses.AttestationResponse
import software.tice.wallet.attestation.responses.NonceResponse
import software.tice.wallet.attestation.services.WalletApiService


@RestController
@RequestMapping("wallet")

class WalletApi(val walletApiService: WalletApiService) {

    @PostMapping()
    fun requestNonces(@RequestBody request: NonceRequest): NonceResponse {
        return walletApiService.requestNonces(request.walletId)
    }

    @PostMapping("/{walletId}/attestation")
    fun requestAttestation(@RequestBody request: AttestationRequest,  @PathVariable walletId: String): AttestationResponse {
        return walletApiService.requestAttestation(request, walletId)
    }
}