package sustain.geosieve.create;

import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sustain.geosieve.create.uniform.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting...");
        Namespace params = null;

        try {
            params = Parameters.parse(args);
        } catch (IllegalArgumentException e) {
            System.exit(1);
        }

        JobLauncher launcher = Factories.getLauncher(params);
        launcher.launch();
    }
}

