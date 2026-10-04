# Fluxer4J

Fluxer4J is a Java 21 client library for [Fluxer](https://web.fluxer.app), a chat platform with guilds, channels, voice and bots. It gives you:

- an **asynchronous high-level client** (`flux.api.FluxClient`) with typed entities, events and about forty common operations, all returning `CompletableFuture`;
- a **raw REST layer** (`client.rest()`) covering 26 resource groups and 345 endpoints that take and return JSON strings;
- a **gateway client** over WebSocket with heartbeats, session resume and automatic reconnection;
- **voice receive**: decrypting and decoding incoming audio to PCM.

Maven coordinates: `app.fluxer4j:fluxer4j:1.0.1`.

## Requirements

- JDK 21 or newer (records, pattern matching and arrow switches are used throughout)
- Maven
- `libopus` installed on the system **only if you use voice** (loaded through JNA when the Opus classes are first touched)

## Installation

The library is not published to Maven Central. Build and install it into your local repository, then depend on it:

```
git clone https://github.com/luna-gelmini/Fluxer4J
cd Fluxer4J
mvn install
```

```xml
<dependency>
    <groupId>app.fluxer4j</groupId>
    <artifactId>fluxer4j</artifactId>
    <version>1.0.1</version>
</dependency>
```

The jar is not shaded; Maven pulls the dependencies (OkHttp 4.12, Jackson 2.15, SLF4J 2, Logback runtime, JNA, tweetnacl-java, weupnp) transitively.

## Quick start

```java
import flux.Flux;
import flux.api.FluxClient;
import flux.api.gateway.GatewayIntent;
import flux.model.gateway.MessageCreateEvent;

import java.util.EnumSet;

public class PingBot {
    public static void main(String[] args) {
        FluxClient client = Flux.createDefault();

        client.addEventListener(event -> {
            if (event instanceof MessageCreateEvent e && "!ping".equals(e.getContentRaw())) {
                client.sendMessage(e.getChannelId(), "pong");
            }
        });

        client.login(System.getenv("FLUXER_BOT_TOKEN"),
                EnumSet.of(GatewayIntent.GUILDS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
              .join();   // completes when READY arrives
    }
}
```

`EventListener` is a functional interface with a single `onEvent(Event)` method; dispatch by `instanceof` on the event type. `login` sends the token as `Authorization: Bot <token>` on REST calls and in the gateway IDENTIFY. Call `client.shutdown()` to close everything.

## What the high-level client covers

- **Messages:** `sendMessage` (text, `MessageSendPayload` with embeds and attachments, or a raw multipart body), `editMessage`, `deleteMessage`, `bulkDeleteMessages`, `getMessages`, `sendTyping`.
- **Channels:** get, create, modify, delete, edit permission overwrites with the `Permission` bitmask enum.
- **Guilds, roles, members:** get guild and roles, get member, add/remove roles, move a member between voice channels, `banMember`, `kickMember`, `timeoutMember` (with audit-log reason).
- **Users and presence:** `getUserById`, `getSelfUser` (after READY), `setActivity` (playing, streaming, listening, watching, custom, competing).
- **Voice:** `joinVoiceChannel`, `leaveVoiceChannel`, `playSoundboardSound`, and an `AudioReceiveHandler` that receives 48 kHz stereo 16-bit PCM per user.
- **Builders:** `EmbedBuilder` (enforces Fluxer's length limits) and `MessageBuilder`.

Gateway dispatch types turned into events: `READY`, `RESUMED`, `MESSAGE_CREATE`, `MESSAGE_UPDATE`, `MESSAGE_DELETE`, `GUILD_CREATE`, `GUILD_MEMBER_ADD`, `GUILD_MEMBER_UPDATE`, `GUILD_MEMBER_REMOVE`, `VOICE_STATE_UPDATE`, `VOICE_SERVER_UPDATE`, `INTERACTION_CREATE`, `CHANNEL_UPDATE`. Event classes live in `flux.api.event.*`; `ReadyEvent` and `MessageCreateEvent` live in `flux.model.gateway`.

## Raw REST access

`client.rest()` exposes one object per resource group (auth, billing, channels, connections, debug, discovery, donations, emojis, gateway, geolocation, gifts, guilds, instance, invites, oauth2, packs, premium, read states, reports, saved media, search, stickers, themes, users, voice, webhooks). Each method builds the URL and returns `CompletableFuture<String>` with the response body, so anything the typed layer lacks is still reachable.

## Configuration

Base URLs default to `https://api.fluxer.app/v1`, `wss://gateway.fluxer.app` (gateway v1), `https://fluxerusercontent.com` and `https://web.fluxer.app`. For a self-hosted instance call `ApiEnvironment.configure(apiBase, gateway, mediaCdn, appBase)` once before creating the client; the setting is global to the JVM.

## Tests

```
mvn test
```

Four JUnit 4 classes (payload serialisation, embed builder, channel type mapping, interaction reply JSON). No network access is needed.

## Limitations

- **Interactions are half-implemented.** `INTERACTION_CREATE` is parsed into `SlashCommandInteraction` / `AutocompleteInteraction`, but registering commands (`createGlobalCommand`, `createGuildCommand`, `bulkOverwriteGlobalCommands`) and responding (`reply`, `deferReply`, `showModal`, `replyChoices`) only log a warning and complete without any request; only `sendInteractionFollowup` (webhook route) performs I/O.
- **No rate-limit handling, no entity cache, no sharding.** Non-2xx responses surface as `FluxException`.
- **Voice is receive-only.** `OpusEncoder` exists but nothing sends audio. Joining a voice channel also attempts a UPnP port mapping.
- Channel deserialisation has no mapping for DM and group-DM channel types, and `CHANNEL_UPDATE` is always read as a voice channel.
- Events are dispatched synchronously on the WebSocket reader thread; keep listeners short or hand off to your own executor.
- Reconnection sleeps one to five seconds on the calling thread.

## Project structure

```
flux/Flux.java                 factory: createDefault()
flux/FluxClientImpl.java       high-level client and event dispatcher
flux/api/                      public interfaces: FluxClient, entities, events, payloads, GatewayIntent, ApiEnvironment
flux/model/                    Jackson-mapped implementations and gateway payloads
flux/gateway/client/           REST and WebSocket clients (OkHttp)
flux/rest/                     FluxRest, Urls and the 26 generated-style resource classes
flux/voice/, flux/opus/        voice connection, Opus decoding through JNA
flux/builder/                  EmbedBuilder, MessageBuilder
flux/json/util/                JsonEngine over a single configured ObjectMapper
src/test/java/                 JUnit 4 tests
```

## License

No license file is included; until one is added the code is all rights reserved by default.
