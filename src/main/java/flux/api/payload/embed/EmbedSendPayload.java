package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedSendPayload {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url")
    private String url;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("color")
    private Integer color;

    @JsonProperty("footer")
    private EmbedFooter footer;

    @JsonProperty("image")
    private EmbedImage image;

    @JsonProperty("thumbnail")
    private EmbedThumbnail thumbnail;

    @JsonProperty("author")
    private EmbedAuthor author;

    @JsonProperty("fields")
    private List<EmbedField> fields;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public EmbedFooter getFooter() {
        return footer;
    }

    public void setFooter(EmbedFooter footer) {
        this.footer = footer;
    }

    public EmbedImage getImage() {
        return image;
    }

    public void setImage(EmbedImage image) {
        this.image = image;
    }

    public EmbedThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(EmbedThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public EmbedAuthor getAuthor() {
        return author;
    }

    public void setAuthor(EmbedAuthor author) {
        this.author = author;
    }

    public List<EmbedField> getFields() {
        return fields;
    }

    public void setFields(List<EmbedField> fields) {
        this.fields = fields;
    }
}
