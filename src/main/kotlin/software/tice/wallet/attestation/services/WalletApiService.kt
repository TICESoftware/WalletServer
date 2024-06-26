package software.tice.wallet.attestation.services

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.tice.wallet.attestation.repositories.WalletEntity
import software.tice.wallet.attestation.repositories.WalletRepository
import software.tice.wallet.attestation.requests.AttestationRequest
import software.tice.wallet.attestation.responses.AttestationResponse
import software.tice.wallet.attestation.responses.NonceResponse
import wallet_server.attestation.exceptions.PopVerificationException
import wallet_server.attestation.exceptions.WalletNotFoundException
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*


@Service
class WalletApiService @Autowired constructor(
    @Value("\${private.key}")
    private val privateKey: String,
    private val walletRepository: WalletRepository,

    ) {
    fun requestNonces(walletId: String): NonceResponse {
        val (popNonce, keyAttestationNonce) = List(2) { UUID.randomUUID().toString() }

        val existingWallet = walletRepository.findByWalletId(walletId)

        if (existingWallet != null) {
            existingWallet.popNonce = popNonce
            existingWallet.keyAttestationNonce = keyAttestationNonce
            walletRepository.save(existingWallet)
        } else {
            val newWallet = WalletEntity(
                walletId = walletId,
                popNonce = popNonce,
                keyAttestationNonce = keyAttestationNonce,
                id = null
            )
            walletRepository.save(newWallet)
        }
        return NonceResponse(popNonce = popNonce, keyAttestationNonce = keyAttestationNonce)
    }

    fun requestAttestation(requestAttestation: AttestationRequest, id: String): AttestationResponse {
        val existingWallet = walletRepository.findByWalletId(id)
            ?: throw WalletNotFoundException("Wallet with id $id not found")

        // <--- Start: check the PoP --->
        val publicKey: PublicKey = decodePublicKey(requestAttestation.attestationPublicKey)

        try {
            val nonce: String? = Jwts.parser().verifyWith(publicKey).build()
                .parseSignedClaims(requestAttestation.proofOfPossession).payload["nonce"] as? String

            if (nonce != existingWallet.popNonce) {
                throw PopVerificationException("Nonce mismatch")
            }
        } catch (e: JwtException) {
            throw PopVerificationException("Signature invalid")
        }


        // <--- End: check the PoP --->

        // <--- End: check the request --->


        // <--- Start: throw away nonces and create random ID --->
        existingWallet.popNonce = null
        existingWallet.keyAttestationNonce = null
        walletRepository.save(existingWallet)

        val randomId: String = UUID.randomUUID().toString()
        // <--- End: throw away nonces and create random ID --->


        // <--- Start: create walletAttestation --->
        val privateKey = decodePrivateKey(privateKey)

        val walletAttestation: String =
            Jwts.builder().subject("Joe").claim("publicKey", requestAttestation.attestationPublicKey)
                .claim("randomId", randomId).signWith(privateKey).compact()
        // <--- End: create walletAttestation --->

        return AttestationResponse(walletAttestation)
    }

    fun decodePublicKey(key: String): PublicKey {
        val pem = key
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
        val keyBytes = Base64.getDecoder().decode(pem)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(keySpec)
    }

    fun decodePrivateKey(key: String): PrivateKey {
        val pem = key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
        val keyBytes = Base64.getDecoder().decode(pem)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePrivate(keySpec)
    }
}