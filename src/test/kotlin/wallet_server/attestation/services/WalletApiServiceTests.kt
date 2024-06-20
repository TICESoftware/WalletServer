package wallet_server.attestation.services

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import wallet_server.attestation.config.EnvironmentConfig
import wallet_server.attestation.repositories.UserEntity
import wallet_server.attestation.repositories.UserRepository
import wallet_server.attestation.requests.AttestationRequest
import java.security.KeyPair
import java.util.*
import kotlin.test.assertEquals


internal class WalletApiServiceTests {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var environmentConfig: EnvironmentConfig

    @InjectMocks
    private lateinit var walletApiService: WalletApiService

    @Captor
    private lateinit var userCaptor: ArgumentCaptor<UserEntity>

    private val keyPair: KeyPair = Jwts.SIG.ES256.keyPair().build()
    private val privateKey: String = Base64.getEncoder().encodeToString(keyPair.private.encoded)

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(environmentConfig.getPrivateKey()).thenReturn(privateKey)
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
        val request = AttestationRequest(attestationPublicKey = "PUBLIC_KEY", proofOfPossession = "POP", keyAttestation = "KEY_ATTESTATION", appAttestation = "APP_ATTESTATION")
        val walletInstanceId = "f74813c9-3435-4028-8e0c-018dd34d3b60"

        val response = walletApiService.requestAttestation(request, walletInstanceId)

        val parsedClaims = Jwts.parser()
            .verifyWith(keyPair.public)
            .build()
        assert(parsedClaims.parseSignedClaims(response.walletAttestation).payload.subject.equals("Joe"))
    }
}
