import play.PlayPlugin;
import play.Logger;

import utils.mongo.EmbeddedMongo;

import com.mongodb.MongoException;
import java.io.IOException;

public class EmbededMongoStart extends PlayPlugin {

    private static EmbeddedMongo embedmongo;

    @Override
    public void onApplicationStart() {
        try {
            embedmongo = new EmbeddedMongo();
        } catch (final MongoException e) {
            Logger.error("Could not start embedmongo instance");
        } catch (final IOException e) {
            Logger.error("Could not start embedmongo instance");
        }
    }

    @Override
    public void onApplicationStop() {
        embedmongo.stop();
    }

}