package software.tice.wallet.attestation.requests

data class AttestationRequest(
    val attestationPublicKey: String,
    val proofOfPossession: String,
    val keyAttestation: String,
    val appAttestation: String,
)
