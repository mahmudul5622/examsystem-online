package osa.ora.log;
/*
 * @author Osama Oransa
 */

public class Logger {
    private String className;
    private static  boolean allowDebugging=true;
    private static  boolean allowWarning=true;
    private static  boolean allowInfo=true;
    private static  boolean allowError=true;

    /**
     * @return the allowDebugging
     */
    public static boolean isAllowDebugging() {
        return allowDebugging;
    }

    /**
     * @param aAllowDebugging the allowDebugging to set
     */
    public static void setAllowDebugging(boolean aAllowDebugging) {
        allowDebugging = aAllowDebugging;
    }

    /**
     * @return the allowWarning
     */
    public static boolean isAllowWarning() {
        return allowWarning;
    }

    /**
     * @param aAllowWarning the allowWarning to set
     */
    public static void setAllowWarning(boolean aAllowWarning) {
        allowWarning = aAllowWarning;
    }

    /**
     * @return the allowInfo
     */
    public static boolean isAllowInfo() {
        return allowInfo;
    }

    /**
     * @param aAllowInfo the allowInfo to set
     */
    public static void setAllowInfo(boolean aAllowInfo) {
        allowInfo = aAllowInfo;
    }

    /**
     * @return the allowError
     */
    public static boolean isAllowError() {
        return allowError;
    }

    /**
     * @param aAllowError the allowError to set
     */
    public static void setAllowError(boolean aAllowError) {
        allowError = aAllowError;
    }
    
    private Logger(String className){
        this.className=className;
    }

    public static Logger getLogger (String className) {        
        return new Logger(className);
    }
    public void debug(String debug){
        System.out.println(className+" : "+debug);
    }  
    public void error(String error){
        System.err.println(className+" : "+error);
    }
    public void warn(String warn){
        System.out.println(className+" : "+warn);
    }  
    public void info(String info){
        System.out.println(className+" : "+info);
    }
}

