package app.stats.exporter.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessagesTextEntities implements Serializable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

}
