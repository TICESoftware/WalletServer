package software.tice.wallet.attestation

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

@Configuration
class KeyConfiguration {
    @Value("\${private.key}")
    private lateinit var privateKey: String

    @Bean
    fun decodePrivateKey(): PrivateKey {
        val pem =
            privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
        val keyBytes = Base64.getDecoder().decode(pem)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePrivate(keySpec)
    }
}
