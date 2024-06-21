package wallet_server.attestation.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(WalletNotFoundException::class)
    fun handleWalletNotFound(ex: WalletNotFoundException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
}