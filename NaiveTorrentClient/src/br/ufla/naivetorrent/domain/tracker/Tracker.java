package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class Tracker implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private String name;
	private Integer port;
	
	public ByteBuffer getId() {
		return id;
	}
	public String getIdHex() {
		return UtilHex.toHexString(id);
	}
	public void setId(ByteBuffer id) {
		this.id = id;
	}
	public void setIdHex(String idHex) {
		this.id = UtilHex.toBytes(idHex);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}

}
