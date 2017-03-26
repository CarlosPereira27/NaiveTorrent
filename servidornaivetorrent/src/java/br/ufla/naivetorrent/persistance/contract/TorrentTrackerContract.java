package br.ufla.naivetorrent.persistance.contract;

public class TorrentTrackerContract {

	private TorrentTrackerContract() {

    }

    public static final String TABLE_NAME = "TorrentTracker";
    
    /* "CREATE TABLE IF NOT EXISTS TorrentTracker (
            idTorrent INTEGER NOT NULL,
            idTracker INTEGER NOT NULL,
			PRIMARY KEY (idTorrent, idTracker),
			FOREIGN KEY(idTorrent) REFERENCES MetaTorrent(id) 
				ON DELETE CASCADE,
			FOREIGN KEY(idTracker) REFERENCES Tracker(id) 
				ON DELETE CASCADE );"
    */

    public static final String CREATE_TABLE = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + Columns.ID_TORRENT + " INTEGER NOT NULL, "
            + Columns.ID_TRACKER + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + Columns.ID_TORRENT + ", " + Columns.ID_TRACKER + "), "
            + "FOREIGN KEY(" + Columns.ID_TORRENT + ") REFERENCES " 
            	+ MetaFileTorrentContract.TABLE_NAME + "(" + MetaFileTorrentContract.Columns.ID + ") "
            		+ " ON DELETE CASCADE, "
            + "FOREIGN KEY(" + Columns.ID_TRACKER + ") REFERENCES " 
            	+ TrackerContract.TABLE_NAME + "(" + TrackerContract.Columns.ID + ") "
            		+ " ON DELETE CASCADE );";

    public static abstract class Columns {
        public static final String ID_TORRENT = "idTorrent";
        public static final String ID_TRACKER = "idTracker";
    }
    
}
