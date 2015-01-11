package hackthedrive.bmw.de.hackthedrive.adapter;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 11.01.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final LogUtil logger = LogUtil.getLogger(DbHelper.class);

    private static DbHelper singleton;

    public static final String TABLE_ROUTES = "nav_route";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_JSON = "json";
    private static final String DATABASE_NAME = "hackthedrive.db";

    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String DATABASE_CREATE = String
            .format("create table if not exists %s ( %s integer primary key autoincrement, %s text not null) ",
            TABLE_ROUTES, COLUMN_ID,COLUMN_JSON);

    public static final String TABLE_ROUTES_USER = "nav_route";
    public static final String COLUMN_ID_RU = "_id";
    public static final String COLUMN_ROUTE_ID_RU = "routeId";
    public static final String COLUMN_LAT_RU = "lat";
    public static final String COLUMN_LON_RU = "lon";
    public static final String COLUMN_BAT_RU = "bat";
    public static final String COLUMN_FUEL_RU = "fuel";
    public static final String COLUMN_BAT_END_RU = "bat_end";
    public static final String COLUMN_FUEL_END_RU = "fuel_end";
    private static final String DATABASE_CREATE_ROUTE_USER = String.format("create table if not exists $s ( %s integer primary key autoincrement,  %s integer, " +
                    "%s DOUBLE, %s DOUBLE, %s DOUBLE, %s DOUBLE, %s DOUBLE, %s DOUBLE )",
            TABLE_ROUTES_USER,
            COLUMN_ID_RU,
            COLUMN_ROUTE_ID_RU,
            COLUMN_LAT_RU,
            COLUMN_LON_RU,
            COLUMN_BAT_RU,
            COLUMN_FUEL_RU,
            COLUMN_BAT_END_RU,
            COLUMN_FUEL_END_RU
            );

    protected DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance() {
        return singleton;
    }

    public static DbHelper createInstance(Context context) {
        if (singleton == null) {
            singleton = new DbHelper(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        logger.d("Creating all DB tables");
        try {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_ROUTE_USER);
            logger.d("Create table: %s", DATABASE_CREATE);
            logger.d("Create table: %s", DATABASE_CREATE_ROUTE_USER);
        } catch (SQLException ex){
            logger.w(ex,"Database could not be created.");
        }
        logger.d("Finished creation of all DB tables");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.d("Downgrading DB from version %d to %d (not implemented yet)", oldVersion, newVersion);
    }

    SQLiteDatabase getDb() {
        return getWritableDatabase();
    }

    @Override
    public void close() {
        getDb().close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // to be defined
    }

    /**
     * Executes the statement on the database. Catches any exceptions that
     * occur. Used for {@link #onUpgrade} when the statement might have been
     * executed before in a previous un-final version.
     */
    public void executeSQLSafely(SQLiteDatabase db, String statement) {
        try {
            db.execSQL(statement);
        } catch (Exception e) {
            logger.e(e, "SQLException occured during execution of statement: %s", statement);
        }
    }
}
