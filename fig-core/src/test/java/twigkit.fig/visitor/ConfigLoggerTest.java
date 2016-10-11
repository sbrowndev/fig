package twigkit.fig.visitor;

import org.junit.Test;
import twigkit.fig.Config;
import twigkit.fig.Fig;
import twigkit.fig.loader.PropertiesLoader;

/**
 * Created by scottbrown on 10/10/2016.
 */
public class ConfigLoggerTest {

    @Test
    public void hidePrivateValues() {
        for (Config config : Fig.getInstance(new PropertiesLoader("confs/private")).configs()) {
            new ConfigLogger(config);
        }
    }
}
