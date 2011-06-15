package module;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

public class Settings {
	
	private static Properties props;
	
	//register shutdown-hook and load settings on first usage of class
	static{
		addShutdownHook();
		loadSettings();
	}
	
	private static void addShutdownHook(){
	Runtime.getRuntime().addShutdownHook(new Thread() {
	      public void run() {
	       saveSettings();
	      }
	    });
	}
	
	
	private static void loadSettings() {
		props = new Properties();
		
		FileInputStream read;
		try {
			read = new FileInputStream( Globals.settingsFile );
			props.load(read);

		} catch (Exception e) {
			System.err.println("warning: file not found");
			return;
		}
		
		
	}
	
	public static String getLastPath(){
		return props.getProperty("lastPath");
	}
	public static void setLastPath (String path){
		props.setProperty("lastPath",path);
	}
	
	
	


	private static void saveSettings() {
		try {
			FileOutputStream propOutFile =
			     new FileOutputStream( Globals.settingsFile );
			props.save(propOutFile, "auto shutdown hook-save");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
