import play.*;
import play.jobs.*;
import play.test.*;

import play.test.MorphiaFixtures;

@OnApplicationStart
public class LoadFixtures extends Job {

    public void doJob() {
        MorphiaFixtures.deleteDatabase();
        MorphiaFixtures.loadModels("data.yml");
    }

}