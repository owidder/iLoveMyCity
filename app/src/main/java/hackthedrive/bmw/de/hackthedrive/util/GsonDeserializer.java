package hackthedrive.bmw.de.hackthedrive.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public final class GsonDeserializer {
    private GsonDeserializer() {

    }

    private static GsonBuilder sBuilder;

    static {
        sBuilder = new GsonBuilder();
    }

    public static <T> T deserialize(String data, Class<T> resultClass) {
        if (data == null) {
            // avoid NPE
            return null;
        }

        JsonReader reader = getJsonReader(data);
        try {
            Gson gson = sBuilder.create();
            return gson.fromJson(reader, resultClass);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static JsonReader getJsonReader(String data) {
        Reader stringReader = new StringReader(data);

        JsonReader reader = new JsonReader(stringReader);
        reader.setLenient(true);
        return reader;
    }
}
