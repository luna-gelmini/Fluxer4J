package flux.builder;

import flux.api.payload.embed.EmbedSendPayload;
import flux.api.payload.send.MessageSendPayload;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    private StringBuilder content;
    private boolean tts = false;
    private List<EmbedSendPayload> embeds = new ArrayList<>();

    private MessageSendPayload.MessageReference messageReference;

    public MessageBuilder() {
        this.content = new StringBuilder();
    }

    public MessageBuilder(String initialContent) {
        this.content = new StringBuilder(initialContent);
    }

    public MessageBuilder setContent(String content) {
        this.content = new StringBuilder(content);
        return this;
    }

    public MessageBuilder appendContent(String text) {
        this.content.append(text);
        return this;
    }

    public MessageBuilder appendContentFormat(String format, Object... args) {
        this.content.append(String.format(format, args));
        return this;
    }

    public MessageBuilder setTTS(boolean tts) {
        this.tts = tts;
        return this;
    }

    public MessageBuilder setReplyTo(String messageId) {
        this.messageReference = new MessageSendPayload.MessageReference(messageId);
        return this;
    }

    public MessageBuilder setReplyTo(String guildId, String channelId, String messageId) {
        this.messageReference = new MessageSendPayload.MessageReference(guildId, channelId, messageId);
        return this;
    }

    public MessageSendPayload build() {
        if (content.isEmpty() && embeds.isEmpty() && attachments.isEmpty()) {
            throw new IllegalStateException("Message must have content or embeds/stickers/files.");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Message content cannot exceed 2000 characters.");
        }

        MessageSendPayload payload = new MessageSendPayload();
        payload.setContent(this.content.toString());
        if (this.tts) {
            payload.setTts(this.tts);
        }
        if (!this.embeds.isEmpty()) {
            payload.setEmbeds(this.embeds);
        }
        if (!this.attachments.isEmpty()) {
            payload.setAttachments(this.attachments);
        }
        if (this.messageReference != null) {
            payload.setMessageReference(this.messageReference);
        }
        return payload;
    }

    public MessageBuilder addEmbed(EmbedSendPayload embed) {
        if (this.embeds.size() >= 10) {
            throw new IllegalArgumentException("Cannot add more than 10 embeds to a message.");
        }
        this.embeds.add(embed);
        return this;
    }

    public MessageBuilder addEmbed(EmbedBuilder embedBuilder) {
        return addEmbed(embedBuilder.build());
    }

    public String getContent() {
        return content.toString();
    }

    public boolean isTTS() {
        return tts;
    }

    public MessageBuilder setEmbeds(List<EmbedSendPayload> embeds) {
        if (embeds.size() > 10) {
            throw new IllegalArgumentException("Cannot have more than 10 embeds in a message.");
        }
        this.embeds = new ArrayList<>(embeds);
        return this;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    private final java.util.Map<String, byte[]> attachments = new java.util.HashMap<>();

    public MessageBuilder addAttachment(String name, byte[] data) {
        this.attachments.put(name, data);
        return this;
    }

    public MessageBuilder clear() {
        this.content.setLength(0);
        this.tts = false;
        this.embeds.clear();
        this.attachments.clear();
        return this;
    }
}
