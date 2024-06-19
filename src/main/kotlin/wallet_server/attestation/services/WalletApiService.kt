package wallet_server.attestation.services

import io.github.cdimascio.dotenv.Dotenv
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import wallet_server.attestation.repositories.UserEntity
import wallet_server.attestation.repositories.UserRepository
import wallet_server.attestation.requests.AttestationRequest
import wallet_server.attestation.responses.AttestationResponse
import wallet_server.attestation.responses.NonceResponse
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Service
class WalletApiService @Autowired constructor(
    private val userRepository: UserRepository,

) {
    private val dotenv: Dotenv = Dotenv.load()
    private val privateKey: String? = dotenv["PRIVATE_KEY"]

    fun requestNonces(walletInstanceId: String): NonceResponse {
        val (popNonce, keyAttestationNonce) = List(2) { java.util.UUID.randomUUID().toString() }

        val user = UserEntity(
            walletInstanceId = walletInstanceId,
            popNonce = popNonce,
            keyAttestationNonce = keyAttestationNonce,
            id = null
        )

        userRepository.save(user)
        return NonceResponse(popNonce = popNonce, keyAttestationNonce = keyAttestationNonce )
    }

    fun requestAttestation(requestAttestation: AttestationRequest): AttestationResponse {
        val pem = privateKey
            ?.replace("-----BEGIN PRIVATE KEY-----", "")
            ?.replace("-----END PRIVATE KEY-----", "")

        val decodedKey = Base64.getDecoder().decode(pem)

        val keySpec = PKCS8EncodedKeySpec(decodedKey)
        val keyFactory = KeyFactory.getInstance("EC")
        val privateKeyReloaded = keyFactory.generatePrivate(keySpec)

        val walletAttestation: String = Jwts.builder().subject("Joe").signWith(privateKeyReloaded).compact()
        return AttestationResponse(walletAttestation)
    }
}