package software.tice.wallet.attestation.services

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.tice.wallet.attestation.repositories.WalletEntity
import software.tice.wallet.attestation.repositories.WalletRepository
import software.tice.wallet.attestation.requests.AttestationRequest
import software.tice.wallet.attestation.responses.AttestationResponse
import software.tice.wallet.attestation.responses.NonceResponse
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Service
class WalletApiService @Autowired constructor(
    @Value("\${private.key}")
    private val privateKey: String,
    private val userRepository: WalletRepository,

) {
    fun requestNonces(walletInstanceId: String): NonceResponse {
        val (popNonce, keyAttestationNonce) = List(2) { UUID.randomUUID().toString() }

        val user = WalletEntity(
            walletId = walletInstanceId,
            popNonce = popNonce,
            keyAttestationNonce = keyAttestationNonce,
            id = null
        )

        userRepository.save(user)
        return NonceResponse(popNonce = popNonce, keyAttestationNonce = keyAttestationNonce )
    }

    fun requestAttestation(requestAttestation: AttestationRequest, id: String): AttestationResponse {
        val privateKey = privateKey
        val pem = privateKey
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")

        val decodedKey = Base64.getDecoder().decode(pem)

        val keySpec = PKCS8EncodedKeySpec(decodedKey)
        val keyFactory = KeyFactory.getInstance("EC")
        val privateKeyReloaded = keyFactory.generatePrivate(keySpec)

        val walletAttestation: String = Jwts.builder().subject("Joe").signWith(privateKeyReloaded).compact()
        return AttestationResponse(walletAttestation)
    }
}