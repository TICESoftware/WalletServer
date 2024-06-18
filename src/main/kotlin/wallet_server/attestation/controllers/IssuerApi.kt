package wallet_server.attestation.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import wallet_server.attestation.requests.ValidationRequest

@RestController
@RequestMapping("attestation")
class IssuerApi {

    @PostMapping("/validation")
    fun validateAttestation(@RequestBody request: ValidationRequest): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}