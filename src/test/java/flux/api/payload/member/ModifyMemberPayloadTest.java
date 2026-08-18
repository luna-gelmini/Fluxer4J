package flux.api.payload.member;

import flux.json.util.impl.JacksonJsonEngineImpl;
import flux.model.gateway.VoiceStatePayloadData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModifyMemberPayloadTest {

    @Test
    public void serializesChannelAndConnectionIdForVoiceMove() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();
        String json = engine.toJsonString(new ModifyMemberPayload("channel-1", "conn-9"));

        assertTrue(json.contains("\"channel_id\":\"channel-1\""));
        assertTrue(json.contains("\"connection_id\":\"conn-9\""));
    }

    @Test
    public void voiceStatePrefersConnectionIdOverSessionId() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();
        VoiceStatePayloadData data = engine.fromJsonString(
                "{\"channel_id\":\"hub\",\"user_id\":\"u1\",\"session_id\":\"sess\",\"connection_id\":\"conn\"}",
                VoiceStatePayloadData.class);

        assertEquals("conn", data.getConnectionId());
    }

    @Test
    public void voiceStateFallsBackToSessionId() {
        JacksonJsonEngineImpl engine = new JacksonJsonEngineImpl();
        VoiceStatePayloadData data = engine.fromJsonString(
                "{\"channel_id\":\"hub\",\"user_id\":\"u1\",\"session_id\":\"sess\"}",
                VoiceStatePayloadData.class);

        assertEquals("sess", data.getConnectionId());
    }
}
