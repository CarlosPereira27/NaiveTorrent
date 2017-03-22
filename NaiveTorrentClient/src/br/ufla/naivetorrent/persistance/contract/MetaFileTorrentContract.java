package br.ufla.naivetorrent.persistance.contract;

public class MetaFileTorrentContract {
	
	private MetaFileTorrentContract() {

    }

    public static final String TABLE_NAME = "MetaFileTorrent";
    
    /* "CREATE TABLE IF NOT EXISTS MetaFileTorrent (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            length INTEGER NOT NULL,
            md5sum TEXT NOT NULL,
            pathFile TEXT NOT NULL);"
    */

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Columns.LENGTH + " INTEGER NOT NULL, "
            + Columns.MD5SUM + " TEXT NOT NULL, "
            + Columns.PATH_FILE + " TEXT NOT NULL );";

    public static abstract class Columns {
        public static final String ID = "id";
        public static final String LENGTH = "length";
        public static final String MD5SUM = "md5sum";
        public static final String PATH_FILE = "pathFile";
    }

}
