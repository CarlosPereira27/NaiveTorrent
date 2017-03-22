package br.ufla.naivetorrent.persistance.contract;

public class InfoFileContract {
	
	private InfoFileContract() {

    }

    public static final String TABLE_NAME = "InfoFile";
    
    /* "CREATE TABLE IF NOT EXISTS InfoFile (
            idInfo INTEGER NOT NULL,
            idFile INTEGER NOT NULL,
			PRIMARY KEY (idInfo, idFile),
			FOREIGN KEY(idInfo) REFERENCES MetaInfoTorrent(id) 
				ON DELETE CASCADE,
			FOREIGN KEY(idFile) REFERENCES MetaFileTorrent(id) 
				ON DELETE CASCADE );"
    */

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID_INFO + " INTEGER NOT NULL, "
            + Columns.ID_FILE + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + Columns.ID_INFO + ", " + Columns.ID_FILE + "), "
            + "FOREIGN KEY(" + Columns.ID_INFO + ") REFERENCES " 
            + MetaInfoTorrentContract.TABLE_NAME + "(" + MetaInfoTorrentContract.Columns.ID + ") "
            + " ON DELETE CASCADE, "
            + "FOREIGN KEY(" + Columns.ID_FILE + ") REFERENCES " 
            + MetaFileTorrentContract.TABLE_NAME + "(" + MetaFileTorrentContract.Columns.ID + ") "
            + " ON DELETE CASCADE );";

    public static abstract class Columns {
        public static final String ID_INFO = "idInfo";
        public static final String ID_FILE = "idFile";
    }

}
