package flux.builder;

import flux.api.payload.embed.*;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmbedBuilder {
    private String title;
    private String description;
    private String url;
    private OffsetDateTime timestamp;
    private Integer color;
    private EmbedFooter footer;
    private EmbedImage image;
    private EmbedThumbnail thumbnail;
    private EmbedAuthor author;
    private final List<EmbedField> fields = new ArrayList<>();

    public static final int TITLE_MAX_LENGTH = 256;
    public static final int DESC_MAX_LENGTH = 4096;
    public static final int FIELD_NAME_MAX_LENGTH = 256;
    public static final int FIELD_VALUE_MAX_LENGTH = 1024;
    public static final int MAX_FIELDS = 25;
    public static final int FOOTER_TEXT_MAX_LENGTH = 2048;
    public static final int AUTHOR_NAME_MAX_LENGTH = 256;

    public EmbedBuilder() {}

    public EmbedBuilder setTitle(String title) {
        if (title != null && title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("Embed title cannot exceed " + TITLE_MAX_LENGTH + " characters.");
        }
        this.title = title;
        return this;
    }

    public EmbedBuilder setDescription(CharSequence description) {
        if (description != null && description.length() > DESC_MAX_LENGTH) {
            throw new IllegalArgumentException("Embed description cannot exceed " + DESC_MAX_LENGTH + " characters.");
        }
        this.description = description != null ? description.toString() : null;
        return this;
    }

    public EmbedBuilder appendDescription(CharSequence text) {
        if (this.description == null) {
            this.description = text.toString();
        } else {
            this.description += text.toString();
        }
        if (this.description.length() > DESC_MAX_LENGTH) {
            throw new IllegalArgumentException("Embed description cannot exceed " + DESC_MAX_LENGTH + " characters.");
        }
        return this;
    }

    public EmbedBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public EmbedBuilder setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public EmbedBuilder setColor(Color color) {
        this.color = color != null ? color.getRGB() & 0xFFFFFF : null;
        return this;
    }

    public EmbedBuilder setColor(int rgb) {
        this.color = rgb & 0xFFFFFF;
        return this;
    }

    public EmbedBuilder setFooter(String text, String iconUrl) {
        if (text != null && text.length() > FOOTER_TEXT_MAX_LENGTH) {
            throw new IllegalArgumentException("Embed footer text cannot exceed " + FOOTER_TEXT_MAX_LENGTH + " characters.");
        }
        this.footer = (text == null && iconUrl == null) ? null : new EmbedFooter(text, iconUrl);
        return this;
    }
    public EmbedBuilder setFooter(String text) {
        return setFooter(text, null);
    }

    public EmbedBuilder setImage(String url) {
        this.image = (url == null) ? null : new EmbedImage(url);
        return this;
    }

    public EmbedBuilder setThumbnail(String url) {
        this.thumbnail = (url == null) ? null : new EmbedThumbnail(url);
        return this;
    }

    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        if (name != null && name.length() > AUTHOR_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("Embed author name cannot exceed " + AUTHOR_NAME_MAX_LENGTH + " characters.");
        }
        this.author = (name == null && url == null && iconUrl == null) ? null : new EmbedAuthor(name, url, iconUrl);
        return this;
    }
    public EmbedBuilder setAuthor(String name) {
        return setAuthor(name, null, null);
    }
    public EmbedBuilder setAuthor(String name, String url) {
        return setAuthor(name, url, null);
    }

    public EmbedBuilder addField(String name, String value, boolean inline) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Field name cannot be null or empty.");
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Field value cannot be null or empty.");
        if (name.length() > FIELD_NAME_MAX_LENGTH) throw new IllegalArgumentException("Field name cannot exceed " + FIELD_NAME_MAX_LENGTH + " characters.");
        if (value.length() > FIELD_VALUE_MAX_LENGTH) throw new IllegalArgumentException("Field value cannot exceed " + FIELD_VALUE_MAX_LENGTH + " characters.");
        if (this.fields.size() >= MAX_FIELDS) throw new IllegalArgumentException("Cannot add more than " + MAX_FIELDS + " fields.");

        this.fields.add(new EmbedField(name, value, inline));
        return this;
    }

    public EmbedBuilder addBlankField(boolean inline) {
        return addField("\u200B", "\u200B", inline);
    }

    public EmbedBuilder clearFields() {
        this.fields.clear();
        return this;
    }

    public EmbedSendPayload build() {
        int totalLength = 0;
        if (title != null) totalLength += title.length();
        if (description != null) totalLength += description.length();
        if (footer != null && footer.text != null) totalLength += footer.text.length();
        if (author != null && author.name != null) totalLength += author.name.length();
        for (EmbedField field : fields) {
            totalLength += field.name.length();
            totalLength += field.value.length();
        }
        if (totalLength > 6000) {
            throw new IllegalStateException("Total length of embed text (title, description, footer, author, fields) cannot exceed 6000 characters.");
        }

        return getEmbedSendPayload();
    }

    private @NotNull EmbedSendPayload getEmbedSendPayload() {
        EmbedSendPayload payload = new EmbedSendPayload();
        payload.setTitle(this.title);
        payload.setDescription(this.description);
        payload.setUrl(this.url);
        payload.setTimestamp(this.timestamp != null ? this.timestamp.toString() : null);
        payload.setColor(this.color);
        payload.setFooter(this.footer);
        payload.setImage(this.image);
        payload.setThumbnail(this.thumbnail);
        payload.setAuthor(this.author);
        if (!this.fields.isEmpty()) {
            payload.setFields(new ArrayList<>(this.fields));
        }
        return payload;
    }

    public EmbedBuilder clear() {
        title = null;
        description = null;
        url = null;
        timestamp = null;
        color = null;
        footer = null;
        image = null;
        thumbnail = null;
        author = null;
        fields.clear();
        return this;
    }
}
