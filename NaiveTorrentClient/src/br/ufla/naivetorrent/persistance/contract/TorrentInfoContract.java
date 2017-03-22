package br.ufla.naivetorrent.persistance.contract;


public class TorrentInfoContract {
	
	private TorrentInfoContract() {

    }

    public static final String TABLE_NAME = "TorrentInfo";
    
    /* "CREATE TABLE IF NOT EXISTS TorrentInfo (
            idTorrent INTEGER NOT NULL,
            idInfo INTEGER NOT NULL,
			PRIMARY KEY (idTorrent, idInfo),
			FOREIGN KEY(idTorrent) REFERENCES MetaTorrent(id) 
				ON DELETE CASCADE,
			FOREIGN KEY(idInfo) REFERENCES MetaInfoTorrent(id) 
				ON DELETE CASCADE );"
    */

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID_TORRENT + " INTEGER NOT NULL, "
            + Columns.ID_INFO + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + Columns.ID_TORRENT + ", " + Columns.ID_INFO + "), "
            + "FOREIGN KEY(" + Columns.ID_TORRENT + ") REFERENCES " 
            + MetaFileTorrentContract.TABLE_NAME + "(" + MetaFileTorrentContract.Columns.ID + ") "
            + " ON DELETE CASCADE, "
            + "FOREIGN KEY(" + Columns.ID_INFO + ") REFERENCES " 
            + MetaInfoTorrentContract.TABLE_NAME + "(" + MetaInfoTorrentContract.Columns.ID + ") "
            + " ON DELETE CASCADE );";

    public static abstract class Columns {
        public static final String ID_TORRENT = "idTorrent";
        public static final String ID_INFO = "idInfo";
    }

}
