package app.stats.exporter.db;

public class DBConstants {

    // Constants and queries related to chat_info table
    public static String CHAT_INFO_TABLE = "CREATE TABLE IF NOT EXISTS chat_info (\n"
            + " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + " name TEXT NOT NULL ,\n"
            + " type TEXT NOT NULL ,\n"
            + " chat_id TEXT NOT NULL\n"
            + ");";

    public static String EXPORT_CHAT_INFO_QUERY = "INSERT INTO chat_info (id, name, type, chat_id) VALUES (NULL, ?, ?, ?);";

    public static int CHAT_INFO_NAME_COL = 1;
    public static int CHAT_INFO_TYPE_COL = 2;
    public static int CHAT_INFO_CHATID_COL = 3;


    // Constants and queries related to messages table
    public static String MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS messages (\n"
            + " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + " message_id TEXT NOT NULL, \n"
            + " reply_id TEXT, \n"
            + " type TEXT NOT NULL, \n"
            + " date DATETIME NOT NULL, \n"
            + " forwarded_from TEXT, \n"
            + " user TEXT NOT NULL, \n"
            + " user_id INTEGER NOT NULL, \n"
            + " media_type TEXT, \n"
            + " mime_type TEXT, \n"
            + " text TEXT NOT NULL\n"
            + ");";


    public static String EXPORT_MESSAGE_QUERY = "INSERT INTO messages (id, message_id, reply_id, type, date, " +
            "forwarded_from, user, user_id, media_type, mime_type, text) " +
            "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static int MESSAGES_MESSAGEID_COL = 1;
    public static int MESSAGES_REPLYID_COL = 2;
    public static int MESSAGES_TYPE_COL = 3;
    public static int MESSAGES_DATE_COL = 4;
    public static int MESSAGES_FORWARDEDFROM_COL = 5;
    public static int MESSAGES_USER_COL = 6;
    public static int MESSAGES_USERID_COL = 7;
    public static int MESSAGES_MEDIATYPE_COL = 8;
    public static int MESSAGES_MIMETYPE_COL = 9;
    public static int MESSAGES_TEXT_COL = 10;

}
