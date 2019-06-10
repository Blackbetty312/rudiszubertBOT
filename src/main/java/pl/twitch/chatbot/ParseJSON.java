package pl.twitch.chatbot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ParseJSON {

    public static JSONObject readJsonFromUrl(int streamsCount) throws IOException, JSONException {

        URL apiPath = new URL("https://api.twitch.tv/helix/streams?first=" + streamsCount + "&language=pl");
        HttpURLConnection apiPathConnection = (HttpURLConnection) apiPath.openConnection();
        apiPathConnection.setRequestProperty("Client-ID", "v9wklrcgp86fw5bjgs6ki8opoaxkq5");
        InputStream is = apiPathConnection.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static List<String> getListOfStreamers(int streamersCount) throws IOException {
        List<String> streamers = new ArrayList<>();
        JSONObject streamy = ParseJSON.readJsonFromUrl(streamersCount);
        JSONArray s2 = streamy.getJSONArray("data");
        for(Object o : s2) {
            JSONObject jsonObject = (JSONObject) o;
//            System.out.println(jsonObject.getString("user_name") + " [" + jsonObject.getInt("viewer_count") + "] " + jsonObject.getString("type"));
//            if(jsonObject.getString("type").equals("live") && jsonObject.getInt("viewer_count") > 20) {
            if(jsonObject.getString("type").equals("live")) {
                streamers.add(jsonObject.getString("user_name").toLowerCase());
            }
        }
        return streamers;
    }
}
