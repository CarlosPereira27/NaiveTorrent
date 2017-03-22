package br.ufla.naivetorrent.torrent;

import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class CreateShareTorrent {

	private ShareTorrent share;

	public CreateShareTorrent(ShareTorrent share) {
		this.share = share;
	}

	//Se o arquivo existe, gerar a hash dos pedacos, e marca cada pedaco no bitfield
	
	//Senao cria o arquivo com bit 0, seta o bitfield como 0

}
