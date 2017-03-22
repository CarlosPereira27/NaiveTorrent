package br.ufla.naivetorrent.tracker.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.tracker.protocol.RequestMessage;
import br.ufla.naivetorrent.tracker.protocol.RequestParameters;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessage;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessageDecoder;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessageError;

public class HttpRequest implements Runnable {
	
	private static final String SERVLET = "/tracker";
	private static final String PROJECT = "/servidornaivetorrent";
	@SuppressWarnings("unused")
	private Tracker tracker;
	private RequestMessage requestMessage;
	private StringBuilder sbURL;
	private StringBuilder sbResponse;
	private int numParameters;
	
	public HttpRequest(Tracker tracker, RequestMessage requestMessage) {
		this.tracker = tracker;
		this.requestMessage = requestMessage;
		numParameters = 0;
		sbURL = new StringBuilder();
		sbResponse = new StringBuilder();
		sbURL.append(tracker.getSocketAddressListening().getHostName())
			.append(':')
			.append(tracker.getSocketAddressListening().getPort())
			.append(PROJECT)
			.append(SERVLET);
		setParameters();
		System.out.println(sbURL.toString());
	}
	
	/**
	 * Adiciona um parâmetro na requisição.
	 * @param name nome do parâmetro
	 * @param value valor do parâmetro
	 */
	private void addParameter(String name, String value) {
		if (numParameters == 0) {
			sbURL.append('?');
		} else {
			sbURL.append('&');
		}
		sbURL.append(name)
			.append('=')
			.append(value);
		numParameters++;
	}
	
	/**
	 * Define os parâmetros da requisição.
	 */
	private void setParameters() {
		if (!requestMessage.isValid()) {
			throw new RuntimeException("Mensagem da requisição não é válida.");
		}
		addParameter(RequestParameters.INFO_HASH, requestMessage.getInfoHash());
		addParameter(RequestParameters.PEER_ID, requestMessage.getPeerId());
		if (requestMessage.getIp() != null) 
			addParameter(RequestParameters.PEER_IP, requestMessage.getIp());
		addParameter(RequestParameters.PEER_PORT, requestMessage.getPort().toString());
		addParameter(RequestParameters.UPLOADED, requestMessage.getUploaded().toString());
		addParameter(RequestParameters.DOWNLOADED, requestMessage.getDownloaded().toString());
		addParameter(RequestParameters.LEFT, requestMessage.getLeft().toString());
		if (requestMessage.getEvent() != null)
			addParameter(RequestParameters.EVENT, Integer.toString(
					requestMessage.getEvent().ordinal()));
		if (requestMessage.getNumWant() != null)
			addParameter(RequestParameters.NUM_WANT, requestMessage.getNumWant().toString());
		if (requestMessage.getTrackerId() != null)
			addParameter(RequestParameters.TRACKER_ID, requestMessage.getTrackerId());
		
	}

	/**
	 * Envia a requisição.
	 * @throws IOException
	 */
	public void sendRequest() throws IOException {
		URL url = new URL(sbURL.toString());
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		con.connect();
		
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sbResponse.append(inputLine);
			}
			in.close();
			ResponseMessageDecoder responseMessageDecoder = 
					new ResponseMessageDecoder(sbResponse.toString());
			if (responseMessageDecoder.isMessageError()) {
				@SuppressWarnings("unused")
				ResponseMessageError responseMessageError = 
					responseMessageDecoder.getResponseMessageError();
			} else {
				@SuppressWarnings("unused")
				ResponseMessage responseMessage = 
					responseMessageDecoder.getResponseMessage();
			}
		} else {
			
		}
	}

	@Override
	public void run() {
		try {
			sendRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
