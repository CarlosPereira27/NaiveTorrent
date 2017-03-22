package br.ufla.naivetorrent.persistance.contract;

public class TrackerContract {
	
	private TrackerContract() {

    }

    public static final String TABLE_NAME = "Tracker";
    
    /* "CREATE TABLE IF NOT EXISTS Tracker (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            hashId TEXT UNIQUE NOT NULL,
            hostName TEXT NOT NULL,
            port INTEGER NOT NULL );"
    */

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Columns.HASH_ID + " TEXT UNIQUE NOT NULL, "
            + Columns.HOST_NAME + " TEXT NOT NULL, "
            + Columns.PORT + " INTEGER NOT NULL );";

    public static abstract class Columns {
        public static final String ID = "id";
        public static final String HASH_ID = "hashId";
        public static final String HOST_NAME = "hostName";
        public static final String PORT = "port";
    }

}
