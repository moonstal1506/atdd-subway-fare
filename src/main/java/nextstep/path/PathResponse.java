package nextstep.path;

import nextstep.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(Path path, int fare) {
        this.stations = path.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
        this.distance = path.getDistance();
        this.duration = path.getDuration();
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
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
