package br.ufla.naivetorrent.domain.peer;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class Peer implements Serializable {

	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private String ip;
	private Integer port;
	
	public ByteBuffer getId() {
		return id;
	}
	public void setId(ByteBuffer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
}
