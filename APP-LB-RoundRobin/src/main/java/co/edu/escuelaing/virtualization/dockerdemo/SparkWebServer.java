package co.edu.escuelaing.virtualization.dockerdemo;

import static spark.Spark.*;
import java.util.ArrayList;

import javax.swing.text.html.HTML;

import com.google.gson.annotations.JsonAdapter;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import spark.Request;
import spark.Response;

public class SparkWebServer {
    private static ArrayList<String> uriList;
    private static RoundRobin robin = new RoundRobin();

    public static void main(String... args) {
        port(getPort());
        staticFiles.location("/");
        get("/", "text/html", ((req, res) -> {
            res.redirect("index.html");
            return null;
        }));
        get("/consult", "application/json", ((req, res) -> {
            res.type("application/json");
            return getLogs();
        }));
        post("/", ((req, res) -> {
            postLog(req, res);
            return null;
        }));

    }

    private static void postLog(Request req, Response res) throws IOException {
        URL url = new URL(robin.getLoginServiceURI());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);
        connection.disconnect();
    }

    private static Object getLogs() throws IOException {
        URL url = new URL(robin.getLoginServiceURI());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        JSONObject resp = getJsonObject(connection.getResponseCode(), connection.getInputStream());
        connection.disconnect();
        return resp;
    }

    private static JSONObject getJsonObject(int responseCode, InputStream inputStream) throws IOException {
        JSONObject json;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            json = new JSONObject(response.toString());
        } else
            throw new IOException();
        return json;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

}
