package flux.model.interaction;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SlashCommandInteractionImplTest {

    @Test
    public void replyPayloadEscapesQuotesAndNewlinesViaSerializer() {
        String json = SlashCommandInteractionImpl.buildReplyPayload("hello \"world\"\nnext", true);

        assertTrue(json.contains("\\\"world\\\""));
        assertTrue(json.contains("\\nnext"));
        assertTrue(json.contains("\"flags\":64"));
    }
}
