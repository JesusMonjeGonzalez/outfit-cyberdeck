# Security Policy

## Scope

Outfit Cyberdeck is a preview-quality local-first application. Wardrobe photos,
the SQLite database and local paths are private user data.

The repository must not contain wardrobe databases, garment photos, Android
keystores, `local.properties` or generated APKs.

## Boundary

- There is no backend, account, analytics service or network sync.
- Images are copied into app-owned local storage and are never uploaded by the app.
- Delete cleanup is restricted to files inside that app-owned storage directory.
- This repository is a preview, not a signed store release.

## Reporting

Do not open a public issue containing a wardrobe photo, database or signing
material. Use a private GitHub security advisory or contact the repository owner
through GitHub with redacted details.

## Release rule

Before distributing an APK or desktop package, verify the build from a clean
checkout, test image/database lifecycle, and document export and backup behavior.

## Repository secret scanning

`.github/workflows/secrets.yml` runs on every push, pull request and manual
dispatch. It scans the full fetched Git history, including deleted content,
using Gitleaks 8.30.1 with `--redact=100`. Findings or scanner errors fail the
job. No scan reports are uploaded and no PR comments are posted.

The Linux x64 archive is verified against its pinned SHA-256 before execution.
Source: [upstream v8.30.1 checksums](https://github.com/gitleaks/gitleaks/releases/download/v8.30.1/gitleaks_8.30.1_checksums.txt).
When updating, review the release and update both the version and checksum.
All workflow actions are pinned to upstream commit SHAs; tokens have only
`contents: read`, checkout does not persist credentials, and jobs have timeouts.
The secret job does not execute project code or use `pull_request_target`.

Local equivalent with an already installed Gitleaks 8.30.1:

```sh
gitleaks git --redact=100 --no-banner --no-color --log-opts="--all --full-history" .
```

This covers locally available refs, not unfetched refs, unreachable objects,
uncommitted files or ignored private data. A clean scan is not proof that no
secret exists. Review findings privately; revoke/rotate real credentials before
planning any history cleanup. Do not silence findings with broad exclusions.
Repository-level Actions settings and required checks must be reviewed separately
by the owner; this workflow alone does not enforce branch protection.
