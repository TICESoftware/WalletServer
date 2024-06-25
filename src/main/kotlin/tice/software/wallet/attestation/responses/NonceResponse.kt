package tice.software.wallet.attestation.responses

data class NonceResponse(
    val popNonce: String,
    val keyAttestationNonce: String
)