package hackthedrive.bmw.de.hackthedrive.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
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

    private String[] allColumns = { DbHelper.COLUMN_ID,
            DbHelper.COLUMN_JSON };

    public RouteDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean save(Route route){
        logger.d("Save route: %s", route);
        ContentValues newContent = new ContentValues();
        newContent.put(DbHelper.COLUMN_JSON, GsonSerializer.serialize(route));

        return database.insert(DbHelper.TABLE_ROUTES, null, newContent) > 0;
    }

    public List<Route> getAllRoutes() {
        List<Route> comments = new ArrayList<>();

        Cursor cursor = database.query(DbHelper.TABLE_ROUTES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Route comment = cursorToRoute(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Route cursorToRoute(Cursor cursor) {
        if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
            logger.i("Route has not been set yet!");
            return null;
        }
        return GsonDeserializer.deserialize(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_JSON)), Route.class);
    }

    public Route getRoute(long id){
        Cursor cursor = null;
        try {
            cursor =  database.query(true, DbHelper.TABLE_ROUTES, new String[]{DbHelper.COLUMN_ID,DbHelper.COLUMN_JSON}, "_id=" + id, null, null, null,
                    null, null);
            if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
                logger.i("Route has not been set yet!");
                return null;
            }
            return GsonDeserializer.deserialize(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_JSON)), Route.class);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }
}
