package flux.json.util.mixin;

import flux.api.entities.channel.Channel;
import flux.json.util.impl.JacksonJsonEngineImpl;
import flux.model.channel.CategoryChannelImpl;
import flux.model.channel.TextChannelImpl;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ChannelMixInTest {

    @Test
    public void deserializesGuildAnnouncementChannelToTextChannel() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();

        Channel channel = engine.fromJsonString("{\"id\":\"1\",\"type\":5,\"name\":\"announcements\"}", Channel.class);

        assertTrue(channel instanceof TextChannelImpl);
    }

    @Test
    public void deserializesGuildDirectoryChannelToCategoryChannel() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();

        Channel channel = engine.fromJsonString("{\"id\":\"2\",\"type\":14,\"name\":\"directory\"}", Channel.class);

        assertTrue(channel instanceof CategoryChannelImpl);
    }

    @Test
    public void deserializesGuildForumChannelToTextChannel() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();

        Channel channel = engine.fromJsonString("{\"id\":\"3\",\"type\":15,\"name\":\"forum\"}", Channel.class);

        assertTrue(channel instanceof TextChannelImpl);
    }
}
