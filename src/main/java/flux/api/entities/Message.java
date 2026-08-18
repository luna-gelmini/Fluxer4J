package flux.api.entities;

import java.util.List;

public interface Message extends FluxerEntity {
    String getGuildId();

    String getContentRaw();

    User getAuthor();

    String getChannelId();

    Message getReferencedMessage();

    java.util.List<User> getMentions();

    List<Attachment> getAttachments();
}
