package uk.co.traintrackapp.traintrack.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationBoard {

    private ArrayList<String> nrccMessages;
    private ArrayList<ServiceItem> trainServices;
    private ArrayList<TubeLine> tubeLines;

    public StationBoard() {
        nrccMessages = new ArrayList<>();
        trainServices = new ArrayList<>();
        tubeLines = new ArrayList<>();
    }

    public StationBoard(JSONObject json) {
        this();
        try {
            JSONArray jsonNrccMessages = json.getJSONArray("nrcc_messages");
            for (int i = 0; i < jsonNrccMessages.length(); i++) {
                nrccMessages.add(jsonNrccMessages.getString(i));
            }
            JSONArray jsonTrainServices = json.getJSONArray("train_services");
            for (int i = 0; i < jsonTrainServices.length(); i++) {
                ServiceItem item = new ServiceItem(jsonTrainServices.getJSONObject(i));
                trainServices.add(item);
            }
            JSONArray jsonTubeLines = json.getJSONArray("tube_lines");
            for (int i = 0; i < jsonTubeLines.length(); i++) {
                TubeLine line = new TubeLine(jsonTubeLines.getJSONObject(i));
                tubeLines.add(line);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public ArrayList<String> getNrccMessages() {
        return nrccMessages;
    }

    public ArrayList<ServiceItem> getTrainServices() {
        return trainServices;
    }

    public ArrayList<TubeLine> getTubeLines() {
        return tubeLines;
    }

    public String toString() {
        return trainServices.toString();
    }

    public static StationBoard getDepartures(String uuid) {
        return getStationBoard(uuid + "/departures");
    }

    public static StationBoard getArrivals(String uuid) {
        return getStationBoard(uuid + "/arrivals");
    }

    public static StationBoard getUnderground(String uuid) {
        return getStationBoard(uuid + "/tube");
    }

    private static StationBoard getStationBoard(String path) {
        JSONObject json = new JSONObject();
        String jsonString = Utils.httpGet(Utils.API_BASE_URL + "/stations/" + path);
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return new StationBoard(json);
    }

}
