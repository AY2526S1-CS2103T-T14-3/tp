package foodtrail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import foodtrail.commons.core.Config;
import foodtrail.commons.core.LogsCenter;
import foodtrail.commons.core.Version;
import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.commons.util.ConfigUtil;
import foodtrail.commons.util.StringUtil;
import foodtrail.logic.Logic;
import foodtrail.logic.LogicManager;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.ReadOnlyUserPrefs;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.util.SampleDataUtil;
import foodtrail.storage.JsonRestaurantDirectoryStorage;
import foodtrail.storage.JsonUserPrefsStorage;
import foodtrail.storage.RestaurantDirectoryStorage;
import foodtrail.storage.Storage;
import foodtrail.storage.StorageManager;
import foodtrail.storage.UserPrefsStorage;
import foodtrail.ui.Ui;
import foodtrail.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 2, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing RestaurantDirectory ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        RestaurantDirectoryStorage restaurantDirectoryStorage =
                new JsonRestaurantDirectoryStorage(userPrefs.getRestaurantDirectoryFilePath());
        storage = new StorageManager(restaurantDirectoryStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s restaurant directory and {@code userPrefs}.
     * <br>
     * The data from the sample restaurant directory will be used instead if {@code storage}'s restaurant directory
     * is not found, or an empty restaurant directory will be used instead if errors occur when reading
     * {@code storage}'s restaurant directory.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getRestaurantDirectoryFilePath());

        Optional<ReadOnlyRestaurantDirectory> restaurantDirectoryOptional;
        ReadOnlyRestaurantDirectory initialData;
        try {
            restaurantDirectoryOptional = storage.readRestaurantDirectory();
            if (!restaurantDirectoryOptional.isPresent()) {
                logger.info("Creating a new data file " + storage.getRestaurantDirectoryFilePath()
                        + " populated with a sample RestaurantDirectory.");
            }
            initialData = restaurantDirectoryOptional.orElseGet(SampleDataUtil::getSampleRestaurantDirectory);
        } catch (DataLoadingException e) {
            logger.warning("Data file at " + storage.getRestaurantDirectoryFilePath() + " could not be loaded."
                    + " Will be starting with an empty RestaurantDirectory.");
            initialData = new RestaurantDirectory();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                    + " Using default config properties.");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting RestaurantDirectory " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping RestaurantDirectory ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
