package hackthedrive.bmw.de.hackthedrive.adapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 11.01.2015.
 */
public class RouteDataSource {

    private static final LogUtil logger = LogUtil.getLogger(RouteDataSource.class);

    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    private String[] allColumns = {DbHelper.COLUMN_ID,
            DbHelper.COLUMN_JSON};

    public RouteDataSource() {
        dbHelper = DbHelper.getInstance();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void openRead() {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        database.close();
        database = null;
    }

    public boolean save(Route route) {
        open();
        logger.d("Save route: %s", route);
        ContentValues newContent = new ContentValues();
        newContent.put(DbHelper.COLUMN_JSON, GsonSerializer.serialize(route));

        boolean success = database.insert(DbHelper.TABLE_ROUTES, null, newContent) > 0;
        close();
        return success;
    }

    public List<Route> getAllRoutes() {
        openRead();
        List<Route> comments = new ArrayList<>();

        Cursor cursor = database.query(DbHelper.TABLE_ROUTES,
                allColumns, null, null, null, null, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Route route = GsonDeserializer.deserialize(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_JSON)), Route.class);
                comments.add(route);
            }
        }

        // make sure to close the cursor
        cursor.close();
        close();
        return comments;
    }

    public Route getRoute(long id) {
        Cursor cursor = null;
        try {
            cursor = database.query(true, DbHelper.TABLE_ROUTES, new String[]{DbHelper.COLUMN_ID, DbHelper.COLUMN_JSON}, "_id=" + id, null, null, null,
                    null, null);
            if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
                logger.i("Route has not been set yet!");
                return null;
            }
            return GsonDeserializer.deserialize(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_JSON)), Route.class);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean startNewRoute(Double lat, Double lng, Double batteryLevel,Double fuelLevel, Long id) {
        open();
        logger.d("Save start of new route: %s", id);
        ContentValues newContent = new ContentValues();
        newContent.put(DbHelper.COLUMN_ROUTE_ID_RU, id);
        newContent.put(DbHelper.COLUMN_LAT_RU, lat);
        newContent.put(DbHelper.COLUMN_LON_RU, lng);
        newContent.put(DbHelper.COLUMN_BAT_RU, batteryLevel);
        newContent.put(DbHelper.COLUMN_FUEL_RU, fuelLevel);

        boolean success = database.insert(DbHelper.TABLE_ROUTES_USER, null, newContent) > 0;
        close();
        return success;
    }

    public boolean endRoute(Double batteryLevel, Double fuelLevel) {
        open();
        logger.d("Finishing current route");
        ContentValues newContent = new ContentValues();
        newContent.put(DbHelper.COLUMN_BAT_END_RU, batteryLevel);
        newContent.put(DbHelper.COLUMN_FUEL_END_RU, fuelLevel);

        String filter = String.format("%s = (SELECT max(%s) FROM %s)", DbHelper.COLUMN_ID, DbHelper.TABLE_ROUTES_USER);
        boolean success = database.update(DbHelper.TABLE_ROUTES_USER, newContent, filter, null ) > 0;
        close();
        return success;
    }
}
