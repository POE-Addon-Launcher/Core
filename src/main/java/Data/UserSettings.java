package Data;

import IO.CustomAHK;
import Repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static Data.PALdata.launcher_data;
import static Data.PALdata.settings;

/**
 *
 */
public class UserSettings
{
    public UserSettings()
    {}

    public static volatile boolean update_request = false;
    public static ObservableList<CustomAHK> customAHKS = FXCollections.observableArrayList();
    private static ArrayList<Repository> repositories = new ArrayList<>();
    public static final String LOCAL_PAL_FOLDER = System.getenv("LOCALAPPDATA") + File.separator + "PAL";
    public static ObservableList<String> PoE_Paths = FXCollections.observableArrayList();

    /***********
     * v1 Data *
     ***********
     */
    /*
    private static String PATH_ADDONS = "";
    private static String PATH_INSTALLDIR = "";
    private static String POE_STEAM = "";
    private static String POE_PATH = "";
    private static String POE_BETA = "";
    private static String LOOT_FILTER = "";

    private static boolean FILTERBLAST_API_ENABLED = true;
    private static boolean GITHUB_API_ENABLED = true;
    private static boolean GITHUB_API_TOKEN_ENABLED = false;
    private static String Github_API_Token = "";

    private static boolean DOWNLOAD_ALL_UPDATES_ON_PAL_LAUNCH = false;
    private static boolean LAUNCH_POE_ON_PAL_LAUNCH = false;
    private static boolean WAIT_FOR_UPDATES_AND_LAUNCH = false;
    private static String POE_VERSION_TO_LAUNCH = "Steam";

    private static String AHK_PATH = "";
    */
    public static void sync()
    {
        LOOT_FILTER = PALdata.settings.getLoot_filter_dir();
        FILTERBLAST_API_ENABLED = PALdata.settings.isFilterblast_api();
        Boolean GITHUB_API_ENABLED = PALdata.settings.isGithub_api_enabled();
        Boolean GITHUB_API_TOKEN_ENABLED = PALdata.settings.isGithub_api_token_enabled();
        String Github_API_Token = PALdata.settings.getGithub_token();
        boolean DOWNLOAD_ALL_UPDATES_ON_PAL_LAUNCH = PALdata.settings.isDown_on_launch();
        boolean LAUNCH_POE_ON_PAL_LAUNCH = PALdata.settings.isRun_poe_on_launch();
        boolean WAIT_FOR_UPDATES_AND_LAUNCH = PALdata.settings.isWait_for_updates();
        String POE_VERSION_TO_LAUNCH = PALdata.settings.getPref_version();
        String AHK_PATH = PALdata.settings.getAHK_Folder();
    }

    /***********
     * v2 Data *
     ***********
     */

    private static String PATH_ADDONS = launcher_data.getInstall_dir() + File.separator + "Addons";
    private static String PATH_INSTALLDIR = launcher_data.getInstall_dir() + File.separator + "Core";
    private static String LOOT_FILTER = "";

    /*******
     * API *
     *******
     */
    private static Boolean FILTERBLAST_API_ENABLED = true;
    private static Boolean GITHUB_API_ENABLED = true;
    private static Boolean GITHUB_API_TOKEN_ENABLED = false;
    private static String Github_API_Token = "";

    /**********
     * LAUNCH *
     **********
     */
    private static boolean DOWNLOAD_ALL_UPDATES_ON_PAL_LAUNCH = false;
    private static boolean LAUNCH_POE_ON_PAL_LAUNCH = false;
    private static boolean WAIT_FOR_UPDATES_AND_LAUNCH = false;
    private static String POE_VERSION_TO_LAUNCH = "";

    /*******
     * AHK *
     *******
     */
    private static String AHK_PATH = "";

    public static void parseCustomAHK()
    {
        // Parse JSON, remove from list if they are in the json.
        File f = new File(LOCAL_PAL_FOLDER + File.separator + "c_ahk.pal");

        if (f.exists())
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                CustomAHK[] array = objectMapper.readValue(f, CustomAHK[].class);
                for (CustomAHK c : array)
                {
                    customAHKS.add(c);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void addDefaultRepos() throws MalformedURLException
    {
        Repository repository = new Repository("Official PAL Repo",
                new URL("https://raw.githubusercontent.com/POE-Addon-Launcher/Curated-Repo/master/addons.json"),
                //new URL("https://raw.githubusercontent.com/POE-Addon-Launcher/Curated-Repo/24-sept-2/addons.json"),
                new URL("https://raw.githubusercontent.com/POE-Addon-Launcher/Curated-Repo/master/filters.json"));
        repositories.add(repository);


        /*repositories.add(new Repository("Community PAL Repository",
                new URL("https://raw.githubusercontent.com/POE-Addon-Launcher/Community-Repo/master/addons.json"),
                new URL("https://raw.githubusercontent.com/POE-Addon-Launcher/Community-Repo/master/filters.json")));*/
    }

    public static void downloadAllRepos()
    {
        for (Repository r : repositories)
        {
            if (RepoDownloader.isValidRepoURL(r.getADDON_JSON(), true))
            {
                try
                {
                    AddonJson[] array = RepoDownloader.downloadAddonRepo(r.getADDON_JSON());
                    for (AddonJson a : array)
                    {
                        a.setSource(r.getName());
                        Addons.getINSTANCe().addAddon(a);
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (RepoDownloader.isValidRepoURL(r.getFILTER_JSON(), false))
            {
                try
                {
                    FilterJson[] array = RepoDownloader.downloadFilterRepo(r.getFILTER_JSON());
                    for (FilterJson f : array)
                    {
                        //f.setSoruce(r.getName());
                        //Filters.getInstacs.addFilter(f);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check INI.
     * Runs settings first if it needs to.
     */
    public static void checkSettings()
    {
        // Check if everything we need is present.

    }

    public static boolean isGithubApiTokenEnabled()
    {
        return GITHUB_API_TOKEN_ENABLED;
    }

    public static void setGithubApiTokenEnabled(boolean githubApiTokenEnabled)
    {
        System.out.println("SETTING TO: " + githubApiTokenEnabled);
        GITHUB_API_TOKEN_ENABLED = githubApiTokenEnabled;
    }

    public static String getAhkPath()
    {
        return AHK_PATH;
    }

    public static void setAhkPath(String ahkPath)
    {
        AHK_PATH = ahkPath;
    }

    public static String getGithub_API_Token()
    {
        if (Github_API_Token == null)
            return "";
        return Github_API_Token;
    }

    public static void setGithub_API_Token(String github_API_Token)
    {
        Github_API_Token = github_API_Token;
    }

    public static ArrayList<Repository> getRepositories()
    {
        return repositories;
    }

    public static void setRepositories(ArrayList<Repository> repositories)
    {
        UserSettings.repositories = repositories;
    }

    public static String getPathAddons()
    {
        return PATH_ADDONS;
    }

    public static void setPathAddons(String pathAddons)
    {
        PATH_ADDONS = pathAddons;
    }

    public static String getPathInstalldir()
    {
        return PATH_INSTALLDIR;
    }

    public static void setPathInstalldir(String pathInstalldir)
    {
        PATH_INSTALLDIR = pathInstalldir;
    }

    /*
    public static String getPoeSteam()
    {
        return POE_STEAM;
    }

    public static void setPoeSteam(String poeSteam)
    {
        POE_STEAM = poeSteam;
    }

    public static String getPoePath()
    {
        return POE_PATH;
    }

    public static void setPoePath(String poePath)
    {
        POE_PATH = poePath;
    }

    public static String getPoeBeta()
    {
        return POE_BETA;
    }

    public static void setPoeBeta(String poeBeta)
    {
        POE_BETA = poeBeta;
    }
    */

    public static String getLootFilter()
    {
        return LOOT_FILTER;
    }

    public static void setLootFilter(String lootFilter)
    {
        LOOT_FILTER = lootFilter;
    }

    public static boolean isFilterblastApiEnabled()
    {
        return FILTERBLAST_API_ENABLED;
    }

    public static void setFilterblastApiEnabled(boolean filterblastApiEnabled)
    {
        FILTERBLAST_API_ENABLED = filterblastApiEnabled;
    }

    public static boolean isUpdate_request()
    {
        return update_request;
    }

    public static void setUpdate_request(boolean update_request)
    {
        UserSettings.update_request = update_request;
    }

    public static boolean isGithubApiEnabled()
    {
        return GITHUB_API_ENABLED;
    }

    public static void setGithubApiEnabled(boolean githubApiEnabled)
    {
        GITHUB_API_ENABLED = githubApiEnabled;
    }

    public static boolean isDownloadAllUpdatesOnPalLaunch()
    {
        return DOWNLOAD_ALL_UPDATES_ON_PAL_LAUNCH;
    }

    public static void setDownloadAllUpdatesOnPalLaunch(boolean downloadAllUpdatesOnPalLaunch)
    {
        DOWNLOAD_ALL_UPDATES_ON_PAL_LAUNCH = downloadAllUpdatesOnPalLaunch;
    }

    public static boolean isLaunchPoeOnPalLaunch()
    {
        return LAUNCH_POE_ON_PAL_LAUNCH;
    }

    public static void setLaunchPoeOnPalLaunch(boolean launchPoeOnPalLaunch)
    {
        LAUNCH_POE_ON_PAL_LAUNCH = launchPoeOnPalLaunch;
    }

    public static boolean isWaitForUpdatesAndLaunch()
    {
        return WAIT_FOR_UPDATES_AND_LAUNCH;
    }

    public static void setWaitForUpdatesAndLaunch(boolean waitForUpdatesAndLaunch)
    {
        WAIT_FOR_UPDATES_AND_LAUNCH = waitForUpdatesAndLaunch;
    }

    public static String getPoeVersionToLaunch()
    {
        return POE_VERSION_TO_LAUNCH;
    }

    public static void setPoeVersionToLaunch(String poeVersionToLaunch)
    {
        POE_VERSION_TO_LAUNCH = poeVersionToLaunch;
    }

    public static ObservableList<CustomAHK> getCustomAHKS()
    {
        return customAHKS;
    }

    public static void setCustomAHKS(ObservableList<CustomAHK> customAHKS)
    {
        UserSettings.customAHKS = customAHKS;
    }

    public static PALsettings getSettings()
    {
        return settings;
    }

    public static Launcher_Data getLauncher_data()
    {
        return launcher_data;
    }


    public static Boolean getFilterblastApiEnabled()
    {
        return FILTERBLAST_API_ENABLED;
    }

    public static void setFilterblastApiEnabled(Boolean filterblastApiEnabled)
    {
        FILTERBLAST_API_ENABLED = filterblastApiEnabled;
    }

    public static Boolean getGithubApiEnabled()
    {
        return GITHUB_API_ENABLED;
    }

    public static void setGithubApiEnabled(Boolean githubApiEnabled)
    {
        GITHUB_API_ENABLED = githubApiEnabled;
    }

    public static Boolean getGithubApiTokenEnabled()
    {
        return GITHUB_API_TOKEN_ENABLED;
    }

    public static void setGithubApiTokenEnabled(Boolean githubApiTokenEnabled)
    {
        GITHUB_API_TOKEN_ENABLED = githubApiTokenEnabled;
    }
}
