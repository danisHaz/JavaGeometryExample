package com.example.common_utils.repository;

import android.content.Context;
import android.util.Log;

import com.example.common_utils.deserializers.IFigureFactory;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import app.IShape;
import io.realm.BuildConfig;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.InsertOneResult;

public final class MongoDb {

    private static final String geometryDBname = "Geometry";
    private static final String geometryCollectionName = "Figures";
    private static final String geometryClusterName = "mongodb-atlas";
    private static final String appId = "javageometryexample-rnwzp";

    private static MongoDb inst = null;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private MongoDb() {
    }

    public static void connectDB(Context context) {
        Realm.init(context);

        AppConfiguration appConfiguration = new AppConfiguration.Builder(appId)
                .appName(BuildConfig.VERSION_NAME)
                .build();

        App app = new App(appConfiguration);

        Credentials credentials = Credentials.anonymous();

        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                User user = app.currentUser();

                assert user != null;
                MongoClient mongoClient = user.getMongoClient(geometryClusterName);
                MongoDatabase db = mongoClient.getDatabase(geometryDBname);

                MongoDb mongoDb = MongoDb.create();
                mongoDb.setCollection(db.getCollection(geometryCollectionName));

            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
            }
        });
    }

    public static MongoDb create() throws IllegalStateException {
        if (inst == null) {
            Object obj = new Object();
            synchronized (obj) {
                if (inst == null)
                    inst = new MongoDb();
            }
        }

        return inst;
    }

    public static void clear() {
        inst.database = null;
        inst = null;
    }

    public void addToDatabase(List<IShape> list, String collectionName) {
        Thread thread = new Thread(() -> {
//            collection.deleteMany(new Document());

            try {
                list.forEach((shape) -> {
                    RealmResultTask<InsertOneResult> task = collection.insertOne(shape.toBson());
                    Log.e("inserted id: ", task.get().getInsertedId().toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();
    }

    public void retrieveFromDatabase(MongoCallbackHandler<List<IShape>> handler) {
        Thread thread = new Thread(() -> {
            List<IShape> list = new ArrayList<>();
            try {
                collection.find().iterator().get().forEachRemaining((document) -> {
                    Object data = document.get("data");
                    if (data instanceof String) {
                        String buildString = (String) data;
                        IShape restoredShape = IFigureFactory.create(buildString);
                        if (restoredShape != null) {
                            list.add(restoredShape);
                        } else {
                            Log.e("MongoDb", "Data is invalid");
                        }
                    }
                });

                handler.handle(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public interface MongoCallbackHandler<T> {
        public void handle(T data);
    }

}

