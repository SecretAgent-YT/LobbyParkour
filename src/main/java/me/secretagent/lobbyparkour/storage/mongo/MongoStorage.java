package me.secretagent.lobbyparkour.storage.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.secretagent.lobbyparkour.course.attempt.CourseAttempt;
import me.secretagent.lobbyparkour.course.attempt.record.AttemptRecord;
import me.secretagent.lobbyparkour.storage.Storage;
import org.bson.Document;
import org.bukkit.entity.Player;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MongoStorage implements Storage {

    private final MongoClient client = new MongoClient();
    private final MongoDatabase database = client.getDatabase("lobby-parkour");
    private final MongoCollection<Document> collection = database.getCollection("attempts");

    @Override
    public void saveCourseAttempt(CourseAttempt courseAttempt) {
        AttemptRecord record = getCourseAttempt(courseAttempt.getPlayerUUID());
        getDocument(courseAttempt.getPlayerUUID().toString()).whenComplete( (document, throwable) -> {
            if (document == null || record.getLength() > courseAttempt.getLength()) {
                if (document != null) collection.deleteOne(document);
                Document doc = new Document();
                doc.put("_id", courseAttempt.getPlayerUUID().toString());
                doc.put("startTime", courseAttempt.getStartTime());
                doc.put("endTime", courseAttempt.getEndTime());
                collection.insertOne(doc);
            }
        });
    }

    @Override
    public AttemptRecord getCourseAttempt(UUID uuid) {
        for (AttemptRecord record : getCourseAttempts()) {
            if (record.getPlayerUUID().equals(uuid)) {
                return record;
            }
        }
        return null;
    }

    @Override
    public List<AttemptRecord> getCourseAttempts() {
        List<AttemptRecord> attempts = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            attempts.add(AttemptRecord.valueOf(cursor.next()));
        }
        return attempts;
    }

    private CompletableFuture<Document> getDocument(String playerUUID) {
        return CompletableFuture.supplyAsync(() -> collection.find(Filters.eq("_id", playerUUID)).first());
    }

    public String getBestTime(Player player) {
        AttemptRecord record = getCourseAttempt(player);
        if (record == null) {
            return "None";
        } else {
            return record.getLengthString();
        }
    }

}
