package app.stats.exporter.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TelegramExportData implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String chatId;

    @JsonProperty("messages")
    private List<TelegramMessage> messages;
}
