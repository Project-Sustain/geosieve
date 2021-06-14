
package sustain.geosieve.create;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.geojson.*;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

public class SustainMongoGisJoinMapper implements GisJoinMapper {
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    private static final String CONNECTION_STRING = "mongodb://lattice-100.cs.colostate.edu:27018";
    private static final String DB_NAME = "sustaindb";
    private static final String COLLECTION_NAME = "tract_geo_GISJOIN";

    public SustainMongoGisJoinMapper() {
        client = MongoClients.create(CONNECTION_STRING);
        collection = client.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);
    }

    @Override
    public String map(LatLng latlng) {
        Point point = new Point(new Position(latlng.lng(), latlng.lat()));
        FindIterable<Document> result = collection.find(geoIntersects("geometry", point));
        Document docGisjoin = result.first();
        if (docGisjoin == null) {
            return "";
        } else {
            return docGisjoin.getString("GISJOIN");
        }
    }
}
