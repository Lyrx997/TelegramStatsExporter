package app.stats.exporter.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessage implements Serializable {

    @JsonProperty("id")
    private String messageId;

    @JsonProperty("reply_to_message_id")
    private String replyMessageId = "";

    @JsonProperty("type")
    private String type;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("forwarded_from")
    private String forwardedFrom;

    @JsonProperty("from")
    private String user;

    @JsonProperty("from_id")
    private String userId;

    @JsonProperty("media_type")
    private String mediaType = "";

    @JsonProperty("mime_type")
    private String mimeType = "";

    @JsonProperty("action")
    private String action = "";

    @JsonProperty("text_entities")
    private List<TelegramMessageTextEntities> textEntities;
}
