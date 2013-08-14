import play.*;
import play.jobs.*;
import play.test.*;

import play.test.MorphiaFixtures;

import play.modules.morphia.MorphiaPlugin;

import com.google.code.morphia.Datastore;

@OnApplicationStart
public class LoadFixtures extends Job {

    public void doJob() {
        //MorphiaFixtures.deleteDatabase();
        MorphiaFixtures.deleteAllModels();
        MorphiaFixtures.loadModels("data.yml");

        // Como el plugin de Morphia se ejecuta antes que este Job, y es dicho plugin el que crea los índices. Debemos volver
        // a crearlos porque al hacer el deleteAllModels() nos lo cepillamos todo.
        Datastore ds = MorphiaPlugin.ds();
        ds.ensureIndexes();
    }

}