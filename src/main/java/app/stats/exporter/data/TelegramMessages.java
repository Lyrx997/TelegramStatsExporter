package app.stats.exporter.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessages implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("from")
    private String from;

    @JsonProperty("from_id")
    private String fromId;

    @JsonProperty("text_entities")
    private List<TelegramMessagesTextEntities> textEntities;
}
