package software.tice.wallet.attestation.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
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
    @Operation(
        summary = "Request attestation for a wallet",
        description = "Submits an attestation request for a given wallet ID.",
        responses = [
            ApiResponse(description = "The wallet attestation certificate", responseCode = "200"),
            ApiResponse(
                description = "Wallet not found", responseCode = "404", content = [Content(
                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun requestAttestation(
        @RequestBody request: AttestationRequest,
        @PathVariable walletId: String
    ): AttestationResponse {
        return walletApiService.requestAttestation(request, walletId)
    }
}