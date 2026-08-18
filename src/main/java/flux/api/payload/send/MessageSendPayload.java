package flux.api.payload.send;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.payload.embed.EmbedSendPayload;
import flux.api.entities.message.component.ActionRow;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageSendPayload {

    @JsonProperty("content")
    private String content;

    @JsonProperty("tts")
    private Boolean tts;
    @JsonProperty("embeds")
    private List<EmbedSendPayload> embeds;

    @JsonProperty("components")
    private List<ActionRow> components;

    @JsonProperty("message_reference")
    private MessageReference messageReference;

    public static class MessageReference {
        @JsonProperty("message_id")
        private String messageId;
        @JsonProperty("channel_id")
        private String channelId;
        @JsonProperty("guild_id")
        private String guildId;
        @JsonProperty("fail_if_not_exists")
        private Boolean failIfNotExists;

        public MessageReference() {}
        public MessageReference(String messageId) { this.messageId = messageId; }
        public MessageReference(String guildId, String channelId, String messageId) {
            this.guildId = guildId;
            this.channelId = channelId;
            this.messageId = messageId;
        }

        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }
        public String getGuildId() { return guildId; }
        public void setGuildId(String guildId) { this.guildId = guildId; }
        public Boolean getFailIfNotExists() { return failIfNotExists; }
        public void setFailIfNotExists(Boolean failIfNotExists) { this.failIfNotExists = failIfNotExists; }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getTts() {
        return tts;
    }

    public void setTts(Boolean tts) {
        this.tts = tts;
    }

    public List<EmbedSendPayload> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<EmbedSendPayload> embeds) {
        this.embeds = embeds;
    }

    public List<ActionRow> getComponents() {
        return components;
    }

    public void setComponents(List<ActionRow> components) {
        this.components = components;
    }

    public MessageReference getMessageReference() {
        return messageReference;
    }

    public void setMessageReference(MessageReference messageReference) {
        this.messageReference = messageReference;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Map<String, byte[]> attachments;

    public java.util.Map<String, byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(java.util.Map<String, byte[]> attachments) {
        this.attachments = attachments;
    }
}
