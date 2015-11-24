package sp;

public class Reference
{
    public static String MAIN = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
    public static String Folder = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/StartupADAM.jar", "").substring(1);


    public static final String TEKSTFOLDER =  Folder+ "/Files/text.txt";
    public static final String TEMPFOLDER =  Folder+ "/Files/temp.txt";
    public static final String PRESETFOLDER = Folder+ "/Files/Presets.txt";
    public static final String TESTFOLDER = Folder+ "/Files/Test.txt";


    public static int test = 1;
}
