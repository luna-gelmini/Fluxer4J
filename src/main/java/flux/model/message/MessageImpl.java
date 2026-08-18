package flux.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.Message;
import flux.api.entities.User;
import flux.model.AbstractFluxerEntity;
import flux.model.user.UserImpl;

public class MessageImpl extends AbstractFluxerEntity implements Message {

    @JsonProperty("content")
    private String contentRaw;

    @JsonProperty("author")
    private UserImpl author;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("guild_id")
    private String guildId;

    @JsonProperty("referenced_message")
    private MessageImpl referencedMessage;

    @JsonProperty("mentions")
    private java.util.List<UserImpl> mentions;

    private flux.api.FluxClient client;

    public void setFluxClient(flux.api.FluxClient client) {
        this.client = client;
    }

    public MessageImpl() {
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public String getContentRaw() {
        return contentRaw;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public Message getReferencedMessage() {
        return referencedMessage;
    }

    @Override
    public java.util.List<User> getMentions() {
        if (mentions == null) return java.util.Collections.emptyList();
        return java.util.Collections.unmodifiableList(mentions);
    }

    @JsonProperty("attachments")
    private java.util.List<flux.model.entities.AttachmentImpl> attachments;

    @Override
    public java.util.List<flux.api.entities.Attachment> getAttachments() {
        if (attachments == null) return java.util.Collections.emptyList();
        return java.util.Collections.unmodifiableList(attachments);
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
                "id='" + id + '\'' +
                ", contentRaw='" + contentRaw + '\'' +
                ", author=" + (author != null ? author.getAsTag() : "null") +
                ", channelId='" + channelId + '\'' +
                ", referencedMessage=" + (referencedMessage != null ? referencedMessage.getId() : "null") +
                '}';
    }
}
