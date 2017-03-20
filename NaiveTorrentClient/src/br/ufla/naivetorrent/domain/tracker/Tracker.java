package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class Tracker implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private String name;
	private Integer port;
	
	public ByteBuffer getId() {
		return id;
	}
	public void setId(ByteBuffer id) {
		this.id = id;
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
