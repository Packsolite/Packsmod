# AGENTS.md - Packsmod Fabric Mod

## Project Overview
Client-only Fabric mod for modern Minecraft. Minimal QOL features: web radio, SmashMC player prefixes, glow for reported players, embedded Skid IRC client.

## Architecture
- Client-only mod.
- All feature modules live under `src/main/java/eu/packsolite/packsmod/feature/`.
- Mixins only bridge into feature code — keep them minimal, no business logic.
- Config via Cloth Config (auto-config), registered in `Packsmod.onInitialize()`.
- Keybindings registered via `KeyMappingHelper`.

## Important Constraints
- **Never** modify Minecraft classes outside mixins.
- **Never** remove or rename existing mixins.
- **Never** edit generated resources (e.g., `fabric.mod.json` is expanded at build time).
- **Never** upgrade `jacomp3_volfix.jar` without manual testing — radio system depends on it.
- **Never** replace the embedded IRC client (`IrcClient.jar`) with a remote dependency.
- Preserve compatibility with current Fabric Loader version.
- Java 25 (source/target/release 25) — do not downgrade.

## Feature Development Workflow
When adding a feature: create package under `feature/`, implement logic there (not in mixins), register in `Packsmod.onInitialize()`, add config/keybinding/ModMenu as needed.

## Mixin Guidelines
- Mixins live in `eu.packsolite.packsmod.mixin.*`, declared in `packsmod.mixins.json`.
- Only inject/callback into feature code — no logic in mixin classes.
- Use `@Inject`/`@Redirect` over `@Overwrite` when possible.
- `compatibilityLevel: "JAVA_21"` required in mixin config.
- Access widener is commented out — avoid unless absolutely necessary.

## Dependency Notes
- **Local JARs** (shadowed from `libs/`): `IrcClient.jar`, `jacomp3_volfix.jar`, `jsubtitle-0.1.0.jar`.
- Do not add these to Maven — they are intentionally embedded.
- ShadowJar replaces the main JAR (`jar.enabled = false`).

## Build & Verification
Always use `--no-daemon` to prevent background processes from hanging. Use a 20 second timeout for `runClient` — it launches the game and never exits on its own.
```bash
./gradlew --no-daemon runClient        # Run client (verify launch) — use 20s timeout
./gradlew --no-daemon build            # Build shadow JAR
./gradlew --no-daemon build -x test    # Build without tests
```

After changes, verify:
- `runClient` launches successfully.
- No `MixinApplyError` in logs.
- No missing mapping warnings.
- No `ClassNotFoundException` at runtime.
- Shadow JAR includes all three local libs.
- No Fabric loader warnings on startup.
- Config persists and loads correctly.

## Commit Style
Commit prefixes use [gitmojis](https://gitmoji.dev/) (e.g., `:arrow_up:`, `:sparkles:`, `:bug:`).
