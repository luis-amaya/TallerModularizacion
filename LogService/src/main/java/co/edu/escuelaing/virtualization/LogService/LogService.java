package co.edu.escuelaing.virtualization.LogService;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONObject;

public class LogService {
    public static void main(String[] args) {
        port(getPort());
        staticFiles.location("/");
        get("/", "application/json", ((req, res) -> {
            res.type("application/json");
            return getLogs();
        }));
        post("/", "application/json", ((req, res) -> {
            postLog();
            return null;
        }));
    }

    private static void postLog() {

    }

    private static Object getLogs() {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("logs");
        JSONObject response = new JSONObject();
        MongoCollection<Document> logsColl = db.getCollection("logs");
        List<Document> logs = logsColl.find().into(new ArrayList<>());
        int count = 0;
        for (Document log : logs) {
            if (count > 10) {
                break;
            }
            response.put("" + count, new JSONObject(log.toJson()));
            count++;
        }
        response = new JSONObject().put("Logs", response);
        mongoClient.close();
        return response;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
