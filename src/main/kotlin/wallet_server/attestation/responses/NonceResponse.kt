package wallet_server.attestation.responses

data class NonceResponse(
    val popNonce: String,
    val keyAttestationNonce: String
)