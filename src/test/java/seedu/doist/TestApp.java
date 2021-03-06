package seedu.doist;

import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.doist.commons.core.Config;
import seedu.doist.commons.core.GuiSettings;
import seedu.doist.model.ReadOnlyTodoList;
import seedu.doist.model.UserPrefs;
import seedu.doist.storage.XmlSerializableTodoList;
import seedu.doist.testutil.TestUtil;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");
    public static final String SAVE_FOR_TESTING_ALIAS = TestUtil.getFilePathInSandboxFolder("sampleAliasListMap.xml");
    protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    public static final String APP_TITLE = "Test App";
    protected static final String DOIST_NAME = "Test";
    protected Supplier<ReadOnlyTodoList> initialDataSupplier = () -> null;
    protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;
    protected String aliasSaveFileLocation = SAVE_FOR_TESTING_ALIAS;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyTodoList> initialDataSupplier, String saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            TestUtil.createDataFileWithData(
                    new XmlSerializableTodoList(this.initialDataSupplier.get()),
                    this.saveFileLocation);
        }
    }

    //@@author A0140887W
    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setAppTitle(APP_TITLE);
        config.setAbsoluteStoragePath(TestUtil.SANDBOX_FOLDER);
        config.setTodoListFilePath(saveFileLocation);
        config.setAliasListMapFilePath(aliasSaveFileLocation);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        config.setTodoListName(DOIST_NAME);
        return config;
    }

    //@@author
    @Override
    protected UserPrefs initPrefs(Config config) {
        UserPrefs userPrefs = super.initPrefs(config);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new GuiSettings(800.0, 1000.0, (int) x, (int) y));
        return userPrefs;
    }


    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
