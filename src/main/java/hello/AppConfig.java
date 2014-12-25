package hello;

public class AppConfig {

	final static String applicationFolderPfad	= "/home/roman/Documents/01_curepathway/work2/cuwy_sb2w_3_develop-w2/";
	final static String innerDbFolderPfad		= "src/main/webapp/db/";
	final static String urlDb = "jdbc:h2:file:~/01_hol_2/db-h2/holvait1-dev/lp24protocol";
	final static String innerOpenDbFolderPfad = "src/main/webapp/cuwy/db/";

	public final static String epicriseDbPrefix = "epicrise/epicrise_";

	public static String getEpicriseDbJsonName(int hId) { return epicriseDbPrefix+ hId+ ".json";}

}
