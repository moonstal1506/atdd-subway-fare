package nextstep.path;

import nextstep.line.Line;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathWeightEdge extends DefaultWeightedEdge {

    private Line line;
    private int distance;
    private int duration;

    public PathWeightEdge(Line line, int distance, int duration) {
        this.line = line;
        this.distance = distance;
        this.duration = duration;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
