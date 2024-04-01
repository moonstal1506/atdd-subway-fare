package nextstep.path;

import nextstep.line.Line;
import nextstep.station.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private int distance;
    private int duration;
    private List<Line> usedLine;

    public Path(List<Station> stations, int distance, int duration, List<Line> usedLine) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.usedLine = usedLine;
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

    public List<Line> getUsedLine() {
        return usedLine;
    }
}
