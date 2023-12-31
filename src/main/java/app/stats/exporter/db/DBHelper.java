package app.stats.exporter.db;

import app.stats.exporter.data.TelegramMessage;
import app.stats.exporter.data.TelegramMessageTextEntities;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static app.stats.exporter.db.DBConstants.*;

@Slf4j
public class DBHelper {

    public static void initDB(String connString) {

        try {

            Connection conn = DriverManager.getConnection(connString);
            Statement statement = conn.createStatement();

            // Create required tables
            statement.execute(CHAT_INFO_TABLE);
            statement.execute(MESSAGES_TABLE);
            conn.close();

        } catch (SQLException ex) {
            log.error("Table creation failed. Table infos {} {}", CHAT_INFO_TABLE, MESSAGES_TABLE);
        }
    }

    public static void exportChatInfo(String connString, String chatId, String name, String type) {

        try {

            Connection conn = DriverManager.getConnection(connString);
            PreparedStatement statement = conn.prepareStatement(EXPORT_CHAT_INFO_QUERY);

            statement.setString(CHAT_INFO_NAME_COL, name);
            statement.setString(CHAT_INFO_TYPE_COL, type);
            statement.setString(CHAT_INFO_CHATID_COL, chatId);
            statement.execute();
            conn.close();

        } catch (SQLException ex){
            log.debug("Chat info variables: {} {} {}", chatId, name, type);
        }

    }

    public static void exportMessageInfo(String connString, TelegramMessage message) {

        try {

            Connection conn = DriverManager.getConnection(connString);
            PreparedStatement statement = conn.prepareStatement(EXPORT_MESSAGE_QUERY);
            statement.setString(MESSAGES_MESSAGEID_COL, message.getMessageId());
            statement.setString(MESSAGES_REPLYID_COL, message.getReplyMessageId());
            statement.setString(MESSAGES_TYPE_COL, message.getType());
            statement.setString(MESSAGES_DATE_COL, message.getDate().toString());
            statement.setString(MESSAGES_FORWARDEDFROM_COL, message.getForwardedFrom());
            if (message.getUser() == null){
                message.setUser("Deleted Account");
            }
            statement.setString(MESSAGES_USER_COL, message.getUser());
            statement.setString(MESSAGES_USERID_COL, message.getUserId());
            statement.setString(MESSAGES_MEDIATYPE_COL, message.getMediaType());
            statement.setString(MESSAGES_MIMETYPE_COL, message.getMimeType());
            statement.setString(MESSAGES_TEXT_COL, getText(message.getTextEntities()));
            statement.execute();
            conn.close();

        } catch (SQLException ex) {
            log.debug("Message infos: {}", message);
        }

    }

    private static String getText(List<TelegramMessageTextEntities> textEntities) {

        if (textEntities.isEmpty()){
            return "Media without description";
        }

        return textEntities.stream().map(TelegramMessageTextEntities::getText).reduce(String::concat).get();
    }

}
