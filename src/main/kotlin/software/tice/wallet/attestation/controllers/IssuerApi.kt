package software.tice.wallet.attestation.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.tice.wallet.attestation.requests.ValidationRequest

@RestController
@RequestMapping("wallet")
class IssuerApi {
    @PostMapping("/validation")
    fun validateAttestation(
        @RequestBody request: ValidationRequest,
    ): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}
