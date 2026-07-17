# ChatTweaks

ChatTweaks is a client-side Fabric mod for Minecraft 26.1.2 and 26.2 that improves chat without replacing Minecraft's chat screen.

## Features

- Hold `Z` to show the full chat while playing. The key can be changed under Minecraft's Controls settings in the ChatTweaks category.
- Scroll through chat with the mouse wheel while holding the peek-chat key.
- Keep up to 1,000 received and sent chat entries instead of Minecraft's default 100.
- Open `/chattweaks` to enable or disable extended chat history. It is enabled by default.
- The config Dashboard opens by default and shows the installed version. Its **Check** button can query GitHub Releases for a newer stable version; ChatTweaks does not make that request unless the button is pressed.

## Requirements

- Minecraft 26.1.2 or 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API
- Java 25

## Building

```shell
./gradlew build
```

The build compiles every target declared in `gradle/targets.properties`. The
distributable JARs are written under `versions/*/build/libs` and include their
Minecraft version in the filename.

## Releases

GitHub Actions does not run for normal commits or pull requests. A release is
started only by pushing a `v*` tag that exactly matches `mod_version` in
`gradle.properties`, for example `v1.0.2` for `mod_version=1.0.2`.

One GitHub Release receives the JAR for every supported Minecraft version, while
Modrinth receives a separate version record with exact game-version metadata for
each JAR.

## License

ChatTweaks is source-available. Official, unmodified releases may be used for
personal, non-commercial purposes; see `LICENSE` for the full terms.
