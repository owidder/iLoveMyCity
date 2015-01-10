package hackthedrive.bmw.de.hackthedrive.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonSerializer {

    private GsonSerializer() {

    }

    private static GsonBuilder sBuilder;

    static {
        sBuilder = new GsonBuilder();

        sBuilder.disableHtmlEscaping();
    }

    public static String serialize(Object element) {
        Gson gson = sBuilder.create();
        return gson.toJson(element);
    }
}
