package com.example.demo;

  

    
    import com.mongodb.ConnectionString;
    import com.mongodb.client.MongoClient;
    import com.mongodb.client.MongoClients;
    import com.mongodb.client.MongoDatabase;

    public class ConexionMongo {
        // Parámetros de conexión para MongoDB
        private static final String MONGO_URI = "mongodb://localhost:27017";
        private static final String MONGO_DATABASE_NAME = "proyecto2";

        public static MongoDatabase conectarMongoDB() {
            ConnectionString connectionString = new ConnectionString(MONGO_URI);
            MongoClient mongoClient = MongoClients.create(connectionString);
            return mongoClient.getDatabase(MONGO_DATABASE_NAME);
        }
       
}
