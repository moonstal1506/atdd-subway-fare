package nextstep.path;

import nextstep.station.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private int distance;
    private int duration;
    private int fare;

    public Path(List<Station> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }
}
