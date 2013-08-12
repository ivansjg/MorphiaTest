import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.MongodProcess;

import java.net.UnknownHostException;
import java.io.IOException;

import com.mongodb.Mongo;

import play.PlayPlugin;
import play.Logger;

public class EmbededMongoStart extends PlayPlugin {

    private static int PORT = 27017;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static Mongo mongo;

    @Override
    public void onApplicationStart() {
        try {
            MongodStarter runtime = MongodStarter.getDefaultInstance();
            MongodConfig config = new MongodConfig(Version.Main.V2_0, PORT, Network.localhostIsIPv6());
            mongodExe = runtime.prepare(config);
            mongod = mongodExe.start();
            mongo = new Mongo("localhost", PORT);
        } catch (final UnknownHostException e) {
            Logger.error("Could not start embedmongo instance");
        } catch (final IOException e) {
            Logger.error("Could not start embedmongo instance");
        }
    }

    @Override
    public void onApplicationStop() {
       mongod.stop();
       mongodExe.stop();
    }

}