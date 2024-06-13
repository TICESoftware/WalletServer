package wallet_server.attestation.types


data class NonceRequest(val walletInstanceId: String)
data class NonceResponse(val nonces: List<String>)

data class AttestationRequest(
    val attestationPublicKey: String,
    val proofOfPossession: String,
    val keyAttestation: String,
    val appAttestation: String
)

data class AttestationResponse(val walletAttestation: String)

data class ValidationRequest(val walletAttestation: String)
