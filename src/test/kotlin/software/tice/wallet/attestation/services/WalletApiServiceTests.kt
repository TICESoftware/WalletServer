package software.tice.wallet.attestation.services

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.*
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import software.tice.wallet.attestation.exceptions.DecodingFailedException
import software.tice.wallet.attestation.repositories.WalletEntity
import software.tice.wallet.attestation.repositories.WalletRepository
import software.tice.wallet.attestation.requests.AttestationRequest
import software.tice.wallet.attestation.exceptions.PopVerificationException
import software.tice.wallet.attestation.exceptions.WalletNotFoundException
import java.security.KeyPair
import java.security.PrivateKey
import java.util.*
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.random.Random


internal class WalletApiServiceTests {
    @Mock
    private lateinit var walletRepository: WalletRepository

    private lateinit var walletApiService: WalletApiService

    private lateinit var walletId: String

    private lateinit var privateKey: PrivateKey

    private val keyPair: KeyPair = Jwts.SIG.ES256.keyPair().build()

    private val internalWalletId: Long = Random.nextLong()

    @Captor
    private lateinit var walletCaptor: ArgumentCaptor<WalletEntity>

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        walletId = randomUUID().toString()
        privateKey = keyPair.private

        walletApiService = WalletApiService(privateKey, walletRepository)
    }

    @Nested
    inner class RequestNoncesTests {
        @Test
        fun `should return correct NonceResponse and update existing user`() {
            val existingWallet = WalletEntity(internalWalletId, walletId, "popNonce", "keyAttestation")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(existingWallet)

            val response = walletApiService.requestNonces(walletId)

            verify(walletRepository).save(walletCaptor.capture())
            val savedwallet = walletCaptor.value
            assertEquals(internalWalletId, savedwallet.id)
            assertEquals(walletId, savedwallet.walletId)
            assertEquals(response.popNonce, savedwallet.popNonce)
            assertEquals(response.keyAttestationNonce, savedwallet.keyAttestationNonce)
        }

        @Test
        fun `should return correct NonceResponse and add new user`() {
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(null)

            val response = walletApiService.requestNonces(walletId)

            verify(walletRepository).save(walletCaptor.capture())
            val newWallet = walletCaptor.value
            assertNull(newWallet.id)
            assertEquals(walletId, newWallet.walletId)
            assertEquals(response.popNonce, newWallet.popNonce)
            assertEquals(response.keyAttestationNonce, newWallet.keyAttestationNonce)

        }
    }

    @Nested
    inner class RequestAttestationTests {
        @Test
        fun `should return correct wallet attestation`() {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val popNonce = randomUUID().toString()
            val mockPop = Jwts.builder().claim("nonce", popNonce).signWith(privateKey).compact()
            val existingWallet = WalletEntity(internalWalletId, walletId, popNonce, "keyAttestation")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(existingWallet)
            val request = AttestationRequest(publicKey, mockPop, "KEY_ATTESTATION", "APP_ATTESTATION")

            val response = walletApiService.requestAttestation(request, walletId)

            verify(walletRepository).save(walletCaptor.capture())
            val newWallet = walletCaptor.value
            val claims = Jwts.parser().verifyWith(keyPair.public).build().parseSignedClaims(response.walletAttestation)
            assertEquals(claims.payload.subject, "Joe")
            assertEquals(claims.payload["publicKey"], publicKey)
            assertNull(newWallet.popNonce)
            assertNull(newWallet.keyAttestationNonce)
        }

        @Test
        fun `should throw WalletNotFoundException if wallet can not be found`() {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val request = AttestationRequest(publicKey, "POP", "KEY_ATTESTATION", "APP_ATTESTATION")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(null)

            val exception = assertThrows<WalletNotFoundException> {
                walletApiService.requestAttestation(request, walletId)
            }
            assertEquals("Wallet with id $walletId not found", exception.message)
        }

        @Test
        fun `should throw DecodingFailedException if public key can not be decoded`() {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val corruptedPublicKey = "$publicKey???"
            val popNonce = randomUUID().toString()
            val mockPop = Jwts.builder().claim("nonce", popNonce).signWith(privateKey).compact()
            val existingWallet = WalletEntity(internalWalletId, walletId, popNonce, "keyAttestation")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(existingWallet)
            val request = AttestationRequest(corruptedPublicKey, mockPop, "KEY_ATTESTATION", "APP_ATTESTATION")

            val exception = assertThrows<DecodingFailedException> {
                walletApiService.requestAttestation(request, walletId)
            }
            assertEquals("Public Key could not be decoded", exception.message)
        }


        @Test
        fun `should throw PopVerificationException if nonce does not match`() {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val popNonceOne = randomUUID().toString()
            val popNonceTwo = randomUUID().toString()
            val mockPop = Jwts.builder().claim("nonce", popNonceOne).signWith(privateKey).compact()
            val existingWallet = WalletEntity(internalWalletId, walletId, popNonceTwo, "keyAttestation")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(existingWallet)

            val request = AttestationRequest(publicKey, mockPop, "KEY_ATTESTATION", "APP_ATTESTATION")

            val exception = assertThrows<PopVerificationException> {
                walletApiService.requestAttestation(request, walletId)
            }
            assertEquals("Nonce mismatch", exception.message)
        }

        @Test
        fun `should throw PopVerificationException if signature is invalid`() {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val popNonce = randomUUID().toString()
            val maliciousPrivateKey = Jwts.SIG.ES256.keyPair().build().private
            val mockPop = Jwts.builder().claim("nonce", popNonce).signWith(maliciousPrivateKey).compact()
            val existingWallet = WalletEntity(internalWalletId, walletId, popNonce, "keyAttestation")
            `when`(walletRepository.findByWalletId(walletId)).thenReturn(existingWallet)
            val request = AttestationRequest(publicKey, mockPop, "KEY_ATTESTATION", "APP_ATTESTATION")

            val exception = assertThrows<PopVerificationException> {
                walletApiService.requestAttestation(request, walletId)
            }
            assertEquals("Signature invalid", exception.message)
        }
    }
}
