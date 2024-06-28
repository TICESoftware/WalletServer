package software.tice.wallet.attestation.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.tice.wallet.attestation.requests.AttestationRequest
import software.tice.wallet.attestation.requests.NonceRequest
import software.tice.wallet.attestation.responses.AttestationResponse
import software.tice.wallet.attestation.responses.NonceResponse
import software.tice.wallet.attestation.services.WalletApiService

@RestController
@RequestMapping("wallet")
class WalletApi(val walletApiService: WalletApiService) {
    @PostMapping
    fun requestNonces(
        @RequestBody request: NonceRequest,
    ): NonceResponse {
        return walletApiService.requestNonces(request.walletId)
    }

    @PostMapping("/{walletId}/attestation")
    @Operation(summary = "Submits an attestation request for a given wallet ID.")
    @ApiResponse(description = "Wallet attestation issued", responseCode = "200")
    @ApiResponse(
        description = "Wallet not found",
        responseCode = "404",
        content = [
            Content(
                mediaType = MediaType.TEXT_PLAIN_VALUE,
                schema = Schema(implementation = String::class),
            ),
        ],
    )
    @ApiResponse(
        description = "Public Key wrong",
        responseCode = "400",
        content = [
            Content(
                mediaType = MediaType.TEXT_PLAIN_VALUE,
                schema = Schema(implementation = String::class),
            ),
        ],
    )
    fun requestAttestation(
        @RequestBody request: AttestationRequest,
        @PathVariable walletId: String,
    ): AttestationResponse {
        return walletApiService.requestAttestation(request, walletId)
    }
}
