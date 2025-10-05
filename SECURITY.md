# Security Policy

## Supported versions
AddressAlarm is currently pre-release software. We aim to support the latest tagged version and the `main` branch.

## Reporting a vulnerability
We take security and privacy seriously. If you discover a vulnerability:
1. Email `security@addressalarm.app` with the details. Use an encrypted channel if possible (PGP key forthcoming).
2. Provide a description of the issue, affected versions, reproduction steps, and potential impact.
3. Allow us at least 14 days to investigate and respond before disclosing publicly.

Do **not** file public GitHub issues or discuss the vulnerability in community channels until we confirm a fix or provide guidance.

## Coordinated disclosure
We will acknowledge receipt within five business days, provide status updates at least once per week, and credit reporters in release notes if desired.

## Hardening checklist
- Keep Android dependencies patched.
- Run static analysis and linting before releases.
- Review new permissions or exported components for principle-of-least-privilege compliance.
