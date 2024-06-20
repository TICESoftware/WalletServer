package wallet_server.attestation.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.stereotype.Component

@Component
class EnvironmentConfig {
    private val dotenv: Dotenv = Dotenv.load()

    fun getPrivateKey(): String? {
        return dotenv["PRIVATE_KEY"]
    }
}