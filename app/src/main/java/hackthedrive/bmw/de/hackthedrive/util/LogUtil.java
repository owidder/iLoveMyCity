package hackthedrive.bmw.de.hackthedrive.util;

import android.os.Process;
import android.util.Log;


/**
 * Common logging for CSC application and dependent modules.
 * 
 * @author anne, mari
 */
public class LogUtil {

  private final String         loggingFrom;

  private static final boolean ANDROID_LOGGING_AVAILABLE = isAndroidLoggingAvailable();

  // same constants as in Androids Log-Class to be independant from them
  private static final int     VERBOSE                   = 2;
  private static final int     DEBUG                     = 3;
  private static final int     INFO                      = 4;
  private static final int     WARN                      = 5;
  private static final int     ERROR                     = 6;

  private static int           logLevelThreshold         = VERBOSE;

  /**
   * Get a logger directly for a specified class.
   */
  public static LogUtil getLogger(Class<?> clazz) {
    return new LogUtil(clazz.getSimpleName());
  }

  /**
   * Get a logger for the current class.
   * <p>
   * Tip: Use {@link #getLogger(Class)} for faster performance!
   */
  public static LogUtil getLogger() {
    // we have to use new Throwable() here because Thread.currentThread().getStackTrace() works differently across Java
    // & Android :-/
    final String className = new Throwable().getStackTrace()[1].getClassName();
    final String simpleName = className.substring(className.lastIndexOf(".") + 1);
    return new LogUtil(simpleName);
  }

  private LogUtil(String loggingClassName) {
    this.loggingFrom = loggingClassName;
  }

  private static boolean isAndroidLoggingAvailable() {
    try {
      Process.myTid();
      return true;
      // NOSONAR
    } catch (Throwable t) {
      return false;
    }
  }

  /**
   * Log a message with the specified level, message and placeholder values. Only for internal use.
   */
  protected void log(int logLevel, String message, Object... placeholderValues) {
    try {
      if (logLevelThreshold <= logLevel) {
        final String formattedMessage = getFormattedMessage(message, placeholderValues);
        final String loggingFromTag = getLoggingFromTag();
        if (ANDROID_LOGGING_AVAILABLE) {
          switch (logLevel) {
            case VERBOSE:
              Log.v(loggingFromTag, formattedMessage);
              break;
            case DEBUG:
              Log.d(loggingFromTag, formattedMessage);
              break;
            case INFO:
              Log.i(loggingFromTag, formattedMessage);
              break;
            case WARN:
              Log.w(loggingFromTag, formattedMessage);
              break;
            case ERROR:
              Log.e(loggingFromTag, formattedMessage);
              break;
          }
        }
        else {
          System.out.println(loggingFromTag + " " + formattedMessage);
        }
      }
    } catch (Exception e) {
      logLoggingException(e);
    }
  }

  /**
   * Log a message with the specified level, throwable, message and placeholder values. Only for internal use.
   */
  protected void log(int logLevel, Throwable throwable, String message, Object... placeholderValues) {
    try {
      if (logLevelThreshold <= logLevel) {
        final String formattedMessage = getFormattedMessage(message, placeholderValues);
        final String loggingFromTag = getLoggingFromTag();
        if (ANDROID_LOGGING_AVAILABLE) {
          switch (logLevel) {
            case VERBOSE:
              Log.v(loggingFromTag, formattedMessage, throwable);
              break;
            case DEBUG:
              Log.d(loggingFromTag, formattedMessage, throwable);
              break;
            case INFO:
              Log.i(loggingFromTag, formattedMessage, throwable);
              break;
            case WARN:
              Log.w(loggingFromTag, formattedMessage, throwable);
              break;
            case ERROR:
              Log.e(loggingFromTag, formattedMessage, throwable);
              break;
          }
        }
        else {
          System.out.println(loggingFromTag + " " + formattedMessage);
        }
      }
    } catch (Exception e) {
      logLoggingException(e);
    }
  }

  /**
   * log debug message
   */
  public void d(String message, Object... placeholderValues) {
    log(DEBUG, message, placeholderValues);
  }

  /**
   * log error message
   */
  public void e(String message, Object... placeholderValues) {
    log(ERROR, message, placeholderValues);
  }

  /**
   * log info message
   */
  public void i(String message, Object... placeholderValues) {
    log(INFO, message, placeholderValues);
  }

  /**
   * log verbose message
   */
  public void v(String message, Object... placeholderValues) {
    log(VERBOSE, message, placeholderValues);
  }

  /**
   * log warn message
   */
  public void w(String message, Object... placeholderValues) {
    log(WARN, message, placeholderValues);
  }

  /**
   * log debug message with throwable
   */
  public void d(Throwable throwable, String message, Object... placeholderValues) {
    log(DEBUG, throwable, message, placeholderValues);
  }

  /**
   * log error message with throwable
   */
  public void e(Throwable throwable, String message, Object... placeholderValues) {
    log(ERROR, throwable, message, placeholderValues);
  }

  /**
   * log info message with throwable
   */
  public void i(Throwable throwable, String message, Object... placeholderValues) {
    log(INFO, throwable, message, placeholderValues);
  }

  /**
   * log verbose message with throwable
   */
  public void v(Throwable throwable, String message, Object... placeholderValues) {
    log(VERBOSE, throwable, message, placeholderValues);
  }

  /**
   * log warn message with throwable
   */
  public void w(Throwable throwable, String message, Object... placeholderValues) {
    log(WARN, throwable, message, placeholderValues);
  }

  private void logLoggingException(Exception e) {
    if (ANDROID_LOGGING_AVAILABLE) {
      Log.e(getLoggingFromTag(), "Could not use custom logging implementation!", e);
    }
    else {
      System.out.println(getLoggingFromTag() + " Could not use custom logging implementation! " + e.getMessage());
    }
  }

  /**
   * Change current logLevel until restart of application.
   * 
   * @param newLevel
   *          - Use android.util.Log constants
   *          (VERBOSE, DEBUG, INFO, WARN, ERROR).
   */
  public void changeLogLevelTemporary(int newLevel) {
    logLevelThreshold = newLevel;
  }

  /**
   * Get current logLevel.
   * 
   * @return See android.util.Log constants (VERBOSE, DEBUG, INFO, WARN, ERROR).
   */
  public int getCurrentLogLevel() {
    return logLevelThreshold;
  }

  public String getLoggingFromTag() {
    return "bmwgroup.csc";
  }

  private String getFormattedMessage(String message, Object... placeholderValues) {
    final String msg;

    if (message == null) {
      msg = "No message to log";
    }
    else if (placeholderValues.length == 0) {
      msg = message;
    }
    else {
      msg = String.format(message, placeholderValues);
    }

    // do not use String.format() again because there might be unknown formatting symbols!
    if (ANDROID_LOGGING_AVAILABLE) {
      return "[" + Process.myTid() + "] " + loggingFrom + ": " + msg;
    }
    else {
      return loggingFrom + ": " + msg;
    }
  }

}
