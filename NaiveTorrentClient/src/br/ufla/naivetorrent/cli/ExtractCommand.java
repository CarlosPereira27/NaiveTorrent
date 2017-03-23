package br.ufla.naivetorrent.cli;
import java.util.ArrayList;
import java.util.List;

public class ExtractCommand {
	
	private int index;
	private String[] tokens;
	
	public ExtractCommand (String commando) {
		tokens = commando.split(" ");
		clearTokens();
		index = 0;
	}
	
	public ExtractCommand (String[] tokens) {
		this.tokens = tokens;
		clearTokens();
		index = 0;
	}
	
	/**
	 * Retira os tokens vazios.
	 */
	public void clearTokens() {
		List<String> cleanTokens = new ArrayList<>();
		for (String token : tokens) {
			if (!token.isEmpty()) {
				cleanTokens.add(token);
			}
		}
		tokens = new String[cleanTokens.size()];
		tokens = cleanTokens.toArray(tokens);
	}
	
	/**
	 * Lê o comando
	 * @return comando
	 * @throws Exception 
	 */
	public String readCmd() 
			throws Exception {
		if (index != 0) {
			throw new Exception("Somente deve ler o comando uma vez, no início");
		}
		return tokens[index++];
	}
	
	/**
	 * Lê um parâmetro.
	 * @return parâmetros
	 * @throws Exception 
	 */
	public String readParameter() 
			throws Exception  {
		// não possui parâmetro
		if (index > tokens.length) {
			return null;
		}
		// parâmetro incorreto
		if (!firstIndexIs(tokens[index], '\"')) {
			throw new Exception("Parâmetro incorreto! Parâmetro deve começar com '\"'.");
		}
		// apenas um token
		if (lastIndexIs(tokens[index], '\"')) {
			index++;
			return tokens[index-1].substring(1, tokens[index-1].length()-1);
		}
		String parameter = tokens[index++].substring(1);
		int length = tokens.length;
		while (index < length && !lastIndexIs(tokens[index], '\"')) {
			parameter += " " + tokens[index++];
		}
		if (index == length) {
			throw new Exception("Parâmetro incorreto (" + parameter + ")!" 
					+ "\nParâmetro deve terminar com '\"'.");
		}
		parameter += " " + tokens[index].substring(0, tokens[index].length()-1);
		index++;
		return parameter;
	}
	
	/**
	 * Verifica se o último caractere da string str é igual a c.
	 * @param str string a ser verificada
	 * @param c caracter a ser comparado
	 * @return true se o último caractere é igual a c, caso contrário false
	 */
	private boolean lastIndexIs(String str, char c) {
		return str.charAt(str.length()-1) == c;
	}
	
	/**
	 * Verifica se o primeiro caractere da string str é igual a c.
	 * @param str string a ser verificada
	 * @param c caracter a ser comparado
	 * @return true se o primeiro caractere é igual a c, caso contrário false
	 */
	private boolean firstIndexIs(String str, char c) {
		return str.charAt(0) == c;
	}
	
	/**
	 * Lê uma lista de parâmetros.
	 * @return lista de parâmetros
	 * @throws Exception 
	 */
	public List<String> readList() 
			throws Exception  {
		List<String> list = new ArrayList<>();
		if (!firstIndexIs(tokens[index], '{')) {
			throw new Exception("Lista de parâmetros incorreta!\n"
					+ "Lista deve começar com o caractere '{'.");
		}
		if (tokens[index].length() == 1) {
			index++;
		} else if (lastIndexIs(tokens[index], '}')) { //Apenas um elemento
			list.add(tokens[index].substring(1, tokens[index].length()-1));
			index++;
			return list;
		} else {
			list.add(tokens[index].substring(1));
			index++;
		}
		int n = tokens.length;
		while (index < n && !lastIndexIs(tokens[index], '}')) {
			if (lastIndexIs(tokens[index], ',')) {
				list.add(tokens[index].substring(0, tokens[index].length()-1));
			} else {
				list.add(tokens[index]);
			}
			index++;
		}
		if (index == n) {
			throw new Exception("Lista de parâmetros incorreta!\n"
					+ "Lista deve terminar com o caractere '}'.");
		}
		if (tokens[index].length() != 1) {
			list.add(tokens[index].substring(0, tokens[index].length()-1));
		}
		index++;
		return list;
	}
	

}
