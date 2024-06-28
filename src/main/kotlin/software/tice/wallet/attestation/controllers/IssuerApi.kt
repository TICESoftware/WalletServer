package software.tice.wallet.attestation.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import software.tice.wallet.attestation.requests.ValidationRequest

@RestController
@RequestMapping("wallet")
class IssuerApi {

    @PostMapping("/validation")
    fun validateAttestation(@RequestBody request: ValidationRequest): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}