package software.tice.wallet.attestation.services

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import org.mockito.Mockito.verify
import software.tice.wallet.attestation.repositories.UserEntity
import software.tice.wallet.attestation.repositories.UserRepository
import software.tice.wallet.attestation.requests.AttestationRequest
import java.security.KeyPair
import java.util.*
import kotlin.test.assertEquals


internal class WalletApiServiceTests {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var privateKey: String

    private lateinit var walletApiService: WalletApiService

    @Captor
    private lateinit var userCaptor: ArgumentCaptor<UserEntity>

    private val keyPair: KeyPair = Jwts.SIG.ES256.keyPair().build()


    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        privateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded)
        walletApiService = WalletApiService(privateKey, userRepository)
    }

    @Test
    fun `should return correct NonceResponse`() {
        val walletInstanceId = "f74813c9-3435-4028-8e0c-018dd34d3b60"

        val response = walletApiService.requestNonces(walletInstanceId)

        verify(userRepository).save(userCaptor.capture())
        val savedUser = userCaptor.value
        assertEquals(walletInstanceId, savedUser.walletInstanceId)
        assertEquals(response.popNonce, savedUser.popNonce)
        assertEquals(response.keyAttestationNonce, savedUser.keyAttestationNonce)
    }


    @Test
    fun `should return correct wallet attestation`() {
        val request = AttestationRequest("PUBLIC_KEY","POP","KEY_ATTESTATION", "APP_ATTESTATION")
        val walletInstanceId = "f74813c9-3435-4028-8e0c-018dd34d3b60"

        val response = walletApiService.requestAttestation(request, walletInstanceId)

        val parser = Jwts.parser()
            .verifyWith(keyPair.public)
            .build()
        assertEquals(parser.parseSignedClaims(response.walletAttestation).payload.subject, "Joe")
    }
}
