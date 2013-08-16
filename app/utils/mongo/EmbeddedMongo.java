package utils.mongo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.MongodProcessOutputConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.io.IStreamProcessor;
import de.flapdoodle.embed.process.io.NamedOutputStreamProcessor;
import de.flapdoodle.embed.process.io.StreamToLineProcessor;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.mongo.MongodProcess;

/**
 * Special subclass of {@link Mongo} that
 * launches an embedded Mongod server instance in a separate process.
 * 
 * @author Alexandre Dutra
 */
public class EmbeddedMongo extends Mongo {

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static Mongo mongo;

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongo.class);

    public static final String DEFAULT_HOST = "127.0.0.1";

    public static final int DEFAULT_PORT = 27017;

    public static final WriteConcern DEFAULT_WRITE_CONCERN = WriteConcern.FSYNC_SAFE;

    public static final Version.Main DEFAULT_VERSION = Version.Main.V2_0;

    public EmbeddedMongo() throws MongoException, IOException {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public EmbeddedMongo(int port) throws MongoException, IOException {
        this(DEFAULT_HOST, port);
    }

    public EmbeddedMongo(String host, int port) throws MongoException, IOException {
        this(host, port, DEFAULT_WRITE_CONCERN);
    }

    public EmbeddedMongo(String host, int port, WriteConcern writeConcern) throws MongoException, IOException {
        this(host, port, writeConcern, DEFAULT_VERSION);
    }

    public EmbeddedMongo(String host, int port, WriteConcern writeConcern, Version.Main version) throws MongoException, IOException {
        super(host, port);
        this.setWriteConcern(writeConcern);

        IStreamProcessor mongodOutput =
                new NamedOutputStreamProcessor("[mongod out]",
                        new StreamToLineProcessor(
                                new Slf4jStreamProcessor(LOG, Slf4jLevel.DEBUG)));
        IStreamProcessor mongodError =
                new NamedOutputStreamProcessor("[mongod err]",
                        new StreamToLineProcessor(
                                new Slf4jStreamProcessor(LOG, Slf4jLevel.WARN)));
        IStreamProcessor commandsOutput =
                new NamedOutputStreamProcessor("[mongod kill]",
                        new StreamToLineProcessor(
                                new Slf4jStreamProcessor(LOG, Slf4jLevel.INFO)));

        Command command = Command.MongoD;
        ProcessOutput outputProcessor = new ProcessOutput(mongodOutput, mongodError, commandsOutput);

        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
                .defaults(command)
                .processOutput(outputProcessor)
                .build();

        MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
        mongodExe = runtime.prepare(new MongodConfig(version, port, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
        mongo = new Mongo(host, port);
    }

    public void stop() {
        mongod.stop();
        mongodExe.stop();
    }
}