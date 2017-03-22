package br.ufla.naivetorrent.persistance.contract;

public class MetaTorrentContract {
	
	private MetaTorrentContract() {

    }

    public static final String TABLE_NAME = "MetaTorrent";
    
    /* "CREATE TABLE IF NOT EXISTS MetaTorrent (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            infoHash TEXT UNIQUE NOT NULL,
            creationDate INTEGER,
            comment TEXT,
            cretedBy TEXT, 
            encoding TEXT,
            uploaded INTEGER NOT NULL,
            downloaded INTEGER NOT NULL,
            bitfield TEXT NOT NULL,
            lastActivity INTEGER NOT NULL,
            sharePath TEXT NOT NULL );"
    */

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Columns.INFO_HASH + " TEXT UNIQUE NOT NULL, "
            + Columns.CREATION_DATE + " INTEGER, "
            + Columns.COMMENT + " TEXT, "
            + Columns.CREATED_BY + " TEXT, "
            + Columns.ENCODING + " TEXT, "
            + Columns.UPLOADED + " INTEGER NOT NULL, "
            + Columns.DOWNLOADED + " INTEGER NOT NULL, "
            + Columns.BITFIELD + " TEXT NOT NULL, "
            + Columns.LAST_ACTIVITY + " INTEGER NOT NULL, "
            + Columns.SHARE_PATH + " TEXT NOT NULL );";

    public static abstract class Columns {
        public static final String ID = "id";
        public static final String INFO_HASH = "infoHash";
        public static final String CREATION_DATE = "creationDate";
        public static final String COMMENT = "comment";
        public static final String CREATED_BY = "cretedBy";
        public static final String ENCODING = "encoding";
        public static final String UPLOADED = "uploaded";
        public static final String DOWNLOADED = "downloaded";
        public static final String BITFIELD = "bitfield";
        public static final String LAST_ACTIVITY = "lastActivity";
        public static final String SHARE_PATH = "sharePath";
    }

}
