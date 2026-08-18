package flux.builder;

import flux.api.payload.embed.EmbedSendPayload;
import org.junit.Test;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;

public class EmbedBuilderTest {

    @Test
    public void buildPreservesTimestamp() {
        OffsetDateTime timestamp = OffsetDateTime.parse("2026-04-10T12:00:00Z");

        EmbedSendPayload payload = new EmbedBuilder()
                .setTitle("hello")
                .setTimestamp(timestamp)
                .build();

        assertEquals("2026-04-10T12:00Z", payload.getTimestamp());
    }
}
