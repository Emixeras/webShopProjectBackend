package de.fhdw;


import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;



@QuarkusMain
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);


    public static void main(String... args) {
        Quarkus.run(Runner.class, args);
    }

    public static class Runner implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            LOG.info("Application successfuly loaded");
            Quarkus.waitForExit();
            return 0;
        }
    }



}