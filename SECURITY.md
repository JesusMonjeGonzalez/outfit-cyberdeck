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
