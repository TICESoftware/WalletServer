# EUDI Wallet Attestation Server

This is a work-in-progress wallet server for the EUDI wallet handling the attestation.

# Overview

## Context

This project has been developed as part of the [SPRIND EUDI Wallet Prototypes Challenge](https://www.sprind.org/de/challenges/eudi-wallet-prototypes). The approach is based on variant C of the [German Architecture Proposal](https://gitlab.opencode.de/bmi/eudi-wallet/eidas-2.0-architekturkonzept) (Version 2). In addition a Zero-Knowledge-Proof (ZKP) mechanism has been implemented in order for the wallet to disclose the credentials in a way that enables plausible deniability against third parties.

## Apps

This wallet attestation server is used in the following apps:

- [EUDI Wallet for Android](https://github.com/TICESoftware/WalletAndroid)
- [EUDI Wallet for iOS](https://github.com/TICESoftware/wallet-ios)

# Setup and development

Just clone the repository and build and run the application using Gradle:

```bash
./gradlew bootRun
```

### Helper utils

When the server is running:
* Find OpenAPI definitions (Swagger) under `/api/docs`.
* Get access to the in-memory database under `/h2-console`.

# Disclaimer

The software in this repository is still under development and not intended to be used in production.

# License

Copyright 2024 TICE GmbH

This project is licensed under the [Apache v2.0 License](LICENSE).
