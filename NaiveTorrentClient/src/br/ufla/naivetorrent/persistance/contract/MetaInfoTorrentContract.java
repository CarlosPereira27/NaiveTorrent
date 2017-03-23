package br.ufla.naivetorrent.persistance.contract;

public class MetaInfoTorrentContract {
	
	private MetaInfoTorrentContract() {

    }

    public static final String TABLE_NAME = "MetaInfoTorrent";
    
    /* "CREATE TABLE IF NOT EXISTS MetaInfoTorrent (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            piecesLength INTEGER NOT NULL,
            piecesHash TEXT NOT NULL);"
    */

    public static final String CREATE_TABLE = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + Columns.PIECES_LENGTH + " INTEGER NOT NULL, "
            + Columns.PIECES_HASH + " TEXT NOT NULL );";

    public static abstract class Columns {
        public static final String ID = "id";
        public static final String PIECES_LENGTH = "piecesLength";
        public static final String PIECES_HASH = "piecesHash";
    }

}
