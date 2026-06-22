package maf.apps.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

public class BOTloggerAPIService {
	static String path = RootPath.BASE_DIR + "\\resources\\x12Download";

	public static void getID(Map<String, Object> body) throws IOException {
		try {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

				// URL of the server endpoint
				HttpPost postRequest = new HttpPost("http://10.1.241.54:8000/bot-manager/bot_unique_id/");
				postRequest.setHeader("Content-Type", "application/json");
				ObjectMapper objectMapper = new ObjectMapper();
				// JSON payload
				Map<String, String> req = new HashMap<>();

				File directory = null;
				File[] files = null;

//			String jsonInputString = "{\"name\": \"John\", \"age\": 30}";
				String jsonInputString = objectMapper.writeValueAsString(body);
				StringEntity entity = new StringEntity(jsonInputString);

				postRequest.setEntity(entity);

				// Execute the request
				CloseableHttpResponse response = httpClient.execute(postRequest);
				try {
					// Get the response code
					int responseCode = response.getStatusLine().getStatusCode();
					System.out.println("Response Code: " + responseCode);

					// Get the response body
					String responseBody = EntityUtils.toString(response.getEntity());
					try {

						ObjectMapper mapper = new ObjectMapper();

						Map<String, String> res = mapper.readValue(responseBody, Map.class);

						System.out.println(res);
						BotInfo era = new BotInfo(body.get("bot_name"), res.get("bot_unique_id"));
						System.out.println("Response Body: " + res);
						String filename = "botInfo.txt";

						// Serialization
						try {

							// Saving of object in a file
							FileOutputStream file = new FileOutputStream(filename);
							ObjectOutputStream out = new ObjectOutputStream(file);

							// Method for serialization of object
							out.writeObject(era);

							out.close();
							file.close();

							System.out.println("Object has been serialized\n" + "Data before Deserialization.");

						}

						catch (IOException ex) {
							System.out.println("IOException is caught");
						}

					} catch (Exception e) {

					}

				} finally {
					response.close();
				}

//			Path fileToDeletePath = Paths.get(path + "\\" + files[index].getName());

				//
//			Files.delete(fileToDeletePath);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PutReq(Map<String, Object> patch) throws IOException, ClassNotFoundException {
		try {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

				// URL of the server endpoint

				File directory = null;
				File[] files = null;
				String filename = "botInfo.txt";
				BotInfo botInfo = new BotInfo("", "");
				try {

					// Reading the object from a file
					FileInputStream file = new FileInputStream(filename);
					ObjectInputStream in = new ObjectInputStream(file);

					// Method for deserialization of object
					botInfo = (BotInfo) in.readObject();
					System.out.println("bot info " + botInfo);
					in.close();
					file.close();
					System.out.println("Object has been deserialized\n" + "Data after Deserialization.");
					System.out.println(botInfo.id);

					// System.out.println("z = " + object1.z);
				}

				catch (IOException ex) {
					ex.printStackTrace();
					System.out.println("IOException is caught");
				}
				HttpPut patchRequest = new HttpPut(
						"http://10.1.241.54:8000/bot-manager/update_bot_log/" + botInfo.id + "/");
				patchRequest.setHeader("Content-Type", "application/json");
				ObjectMapper objectMapper = new ObjectMapper();
				// JSON payload
				Map<String, String> req = new HashMap<>();

//			String jsonInputString = "{\"name\": \"John\", \"age\": 30}";
				String jsonInputString = objectMapper.writeValueAsString(patch);
				System.out.println(patch);
				StringEntity entity = new StringEntity(jsonInputString);

				patchRequest.setEntity(entity);

				// Execute the request
				CloseableHttpResponse response = httpClient.execute(patchRequest);
				try {
					// Get the response code
					int responseCode = response.getStatusLine().getStatusCode();
					System.out.println("Response Code: " + responseCode);

					// Get the response body
					String responseBody = EntityUtils.toString(response.getEntity());
					System.out.println(responseBody);

				} finally {
					response.close();
				}

//			Path fileToDeletePath = Paths.get(path + "\\" + files[index].getName());

				//
//			Files.delete(fileToDeletePath);

			}
		} catch (Exception e) {

		}
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException {

		Map<String, Object> body = new HashMap<>();
		body.put("bot_name", "AMDZeroBalancepaymentposting");
//		body.put("total_count", 46);
//		body.put("client_name", "PBMS");
//		body.put("failed", "0");
//		body.put("success", "1");
//		body.put("is_bot_stopped", "false");

//		getID(body);
		Map<String, Object> patch = new HashMap<>();
		patch.put("failed", 0);
		patch.put("success", 1);
		patch.put("is_bot_stopped", false);
		patch.put("client_name", "PBMS");
		patch.put("bot_name", "AMDZeroBalancepaymentposting");
		patch.put("instance", 1);
		patch.put("bot_processed", false);
		PutReq(patch);

		BotInfo era = new BotInfo("AMDZeroBalancepaymentposting", "PBM---AMD_Z_PP280824");
		String filename = "botInfo1.txt";

		// Serialization
		try {

			// Saving of object in a file
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
//			out.writeObject(era);

			out.close();
			file.close();

			System.out.println("Object has been serialized\n" + "Data before Deserialization.");

		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}

	}

}

class BotInfo implements Serializable {
	private static final long serialversionUID = 129348938L;
	Object botName;

	Object id;

	public BotInfo(Object botName, String id) {
		super();
		this.botName = botName;

		this.id = id;
	}

	@Override
	public String toString() {
		return "BotInfo [botName=" + botName + ", id=" + id + "]";
	}

	// Default constructor

}
