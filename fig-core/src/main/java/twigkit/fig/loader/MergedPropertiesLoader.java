package twigkit.fig.loader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twigkit.fig.Config;
import twigkit.fig.Fig;
import twigkit.fig.util.ConfigUtils;
import twigkit.fig.util.FigUtils;
import twigkit.fig.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * The MergedPropertiesLoader loads two {@link Fig}s, a primary and a secondary. Then merges the two by passing over
 * the properties in the secondary over to the primary {@link Fig}.
 *
 * @author scottbrown
 */
public class MergedPropertiesLoader implements Loader {

    private static final Logger logger = LoggerFactory.getLogger(MergedPropertiesLoader.class);

    private String pathToPrimaryFig;
    private String pathToSecondaryFig;
    private String configsToMerge;

    public MergedPropertiesLoader(String pathToPrimaryFig, String pathToSecondaryFig) {
        this.pathToPrimaryFig = pathToPrimaryFig;
        this.pathToSecondaryFig = pathToSecondaryFig;
    }

    /**
     * This method first loads the given primary {@link Fig} and a second {@link Fig} then merges the
     * second {@link Fig} with the primary {@link Fig}.
     *
     * Any additional configurations set on the {@link Loader} that are not part of the secondary {@link Fig}
     * will also be merged with the primary {@link Fig} once the secondary {@link Fig} has been merged.
     *
     * @param fig The primary {@link Fig}
     */
    public void load(Fig fig) {
        if (pathToPrimaryFig != null) {
            new PropertiesLoader(pathToPrimaryFig).load(fig);

            if (pathToSecondaryFig != null) {
                File secondaryFigRootFolder = FileUtils.getResourceAsFile(pathToSecondaryFig);

                if (secondaryFigRootFolder != null && secondaryFigRootFolder.exists()) {
                    FigUtils.merge(fig, Fig.getInstance(new PropertiesLoader(pathToSecondaryFig)));
                } else {
                    logger.trace("Secondary fig {} not found. Falling back to primary.", pathToSecondaryFig);
                }
            } else {
                logger.trace("Secondary fig {} not found. Falling back to primary.", pathToSecondaryFig);
            }
        } else {
            logger.trace("Primary fig {} not found. Falling back to secondary", pathToPrimaryFig);
            new PropertiesLoader(pathToSecondaryFig).load(fig);
        }

        if (configsToMerge != null) {
            FigUtils.mergeConfigs(fig, ConfigUtils.asList(configsToMerge, this));
        }
    }

    public void write(Config config) throws IOException {
        throw new IOException("Write operation not supported.");
    }

    public void delete(Config config) throws IOException {
        throw new IOException("Delete operation not supported.");
    }

    /**
     * Set a config or sequence of configs that should be merged with the primary {@link Fig} after merging the
     * secondary {@link Fig}. A config should be given in the following form:
     *
     * parent.child[property:value
     *
     * If more than one config is to be merged, subsequent configs should be separated with an ampersand, for
     * example:
     *
     * parent.child[property:value&parent[property:value
     *
     * @param configsToMerge The config or sequence of configs to be merged with the primary {@link Fig}.
     */
    public void setConfigsToMerge(String configsToMerge) {
        this.configsToMerge = configsToMerge;
    }
}
