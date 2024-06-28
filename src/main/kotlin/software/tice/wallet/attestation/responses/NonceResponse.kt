package software.tice.wallet.attestation.responses

data class NonceResponse(
    val popNonce: String,
    val keyAttestationNonce: String,
)
