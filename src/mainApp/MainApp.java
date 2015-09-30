package mainApp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MainApp {

	public static void main(String[] args) throws Exception {
		System.out.println(" +---------------------------+");
		System.out.println(" |  HTTP Server (Porta 8000)  |");
		System.out.println(" +---------------------------+\n");

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
		httpServer.createContext("/", new MyHandler());
		httpServer.setExecutor(null); // creates a default executor
		httpServer.start();
	}

	static class MyHandler implements HttpHandler {
		@SuppressWarnings("resource")
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {

			if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {

				if (httpExchange.getRequestHeaders().getFirst("Content-type").equals("application/json")) {
					System.out.println("É um JSON");

					byte[] tempByteArray = null;
					FileChannel fileChannel;
					ByteBuffer buffer = null;
					String fileName = (new String(new SimpleDateFormat("yyyy-MM-dd_HHmmss_").format(new Date())))
							+ "coordinates.json";
					InputStream inputStream = httpExchange.getRequestBody();
					tempByteArray = IOUtils.toByteArray(inputStream);
					buffer = ByteBuffer.wrap(tempByteArray);
					try {
						fileChannel = new FileOutputStream(fileName, true).getChannel();
						fileChannel.write(buffer);
						fileChannel.close();
					} catch (FileNotFoundException e) {
						System.out.println("ERRO FileChannel");
						e.printStackTrace();
					}
					String response = "JSON Ok!";
					httpExchange.sendResponseHeaders(200, response.length());
					OutputStream outputStream = httpExchange.getResponseBody();
					outputStream.write(response.getBytes());
					outputStream.close();
				}

				if (httpExchange.getRequestHeaders().getFirst("Content-type").equals("image/png")) {
					System.out.println("É um PNG");

					byte[] tempByteArray = null;
					FileChannel fileChannel;
					ByteBuffer buffer = null;
					String fileName = (new String(new SimpleDateFormat("yyyy-MM-dd_HHmmss_").format(new Date())))
							+ "image.png";
					InputStream in = httpExchange.getRequestBody();
					tempByteArray = IOUtils.toByteArray(in);
					buffer = ByteBuffer.wrap(tempByteArray);
					try {
						fileChannel = new FileOutputStream(fileName, true).getChannel();
						fileChannel.write(buffer);
						fileChannel.close();
					} catch (FileNotFoundException e) {
						System.out.println("ERRO FileChannel");
						e.printStackTrace();
					}
					String response = "PNG Ok!";
					httpExchange.sendResponseHeaders(200, response.length());
					OutputStream outputStream = httpExchange.getResponseBody();
					outputStream.write(response.getBytes());
					outputStream.close();
				}
				System.out.println("POST");
			}

			if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
				String response = "This is a GET";
				httpExchange.sendResponseHeaders(200, response.length());
				OutputStream outputStream = httpExchange.getResponseBody();
				outputStream.write(response.getBytes());
				outputStream.close();
				System.out.println("GET");
			}
		}
	}
}