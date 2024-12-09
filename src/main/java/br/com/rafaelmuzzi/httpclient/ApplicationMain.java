package br.com.rafaelmuzzi.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationMain {

	private static final Logger logger = Logger.getLogger(ApplicationMain.class.getName());
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static void main(String[] args) {
		ApplicationMain applicationMain = new ApplicationMain();
		applicationMain.buscarListaPosts();
		applicationMain.buscarPostPorId(1l);
	}
	
	public Post buscarPostPorId(Long id) {
		try(HttpClient client = HttpClient.newHttpClient()) {
			if (id == null || id < 0) {
				throw new RuntimeException("O parâmetro do id deve ser maior que zero.");
			}
			String url = "https://jsonplaceholder.typicode.com/posts/" + id;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			logger.log(Level.INFO, "Resposta recebida para id={0}: \n{1}", new String[] { id.toString(), response.body()});
            Post post = objectMapper.readValue(response.body(), Post.class);
            return post;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Erro ao realizar requisição: {0}", ex.getMessage());
			Thread.currentThread().interrupt();
			throw new RuntimeException("Erro realizar requisição: " + ex.getMessage());
		}
	}
	
	public List<Post> buscarListaPosts() {
		try(HttpClient client = HttpClient.newHttpClient()) {
			String url = "https://jsonplaceholder.typicode.com/posts";
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			logger.log(Level.INFO, "Resposta recebida: \n{0}", response.body());
			List<Post> posts = objectMapper.readValue(response.body(), new TypeReference<List<Post>>() {});
            return posts;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Erro ao realizar requisição: {0}", ex.getMessage());
			Thread.currentThread().interrupt();
			throw new RuntimeException("O parâmetro do id deve ser maior que zero.");
		}
	}

}
