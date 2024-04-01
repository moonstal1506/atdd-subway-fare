package nextstep.path;

import nextstep.exception.SubwayException;
import nextstep.line.Line;
import nextstep.path.fare.*;
import nextstep.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubwayMap {

    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType) {
        validateEqualsStation(source, target);

        GraphPath<Station, PathWeightEdge> path = getGraphPath(source, target, pathType);

        int distance = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDistance).sum();
        int duration = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDuration).sum();
        List<Line> usedLine = path.getEdgeList().stream().map(PathWeightEdge::getLine).distinct().collect(Collectors.toList());

        return new Path(path.getVertexList(), distance, duration, usedLine);
    }

    private void validateEqualsStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같습니다.");
        }
    }

    private GraphPath<Station, PathWeightEdge> getGraphPath(Station source, Station target, PathType pathType) {
        WeightedMultigraph<Station, PathWeightEdge> graph = createGraph(pathType);
        validateStationExists(graph, source, target);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, PathWeightEdge> path = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new SubwayException("출발역과 도착역이 연결이 되어 있지 않습니다."));
        return path;
    }

    private WeightedMultigraph<Station, PathWeightEdge> createGraph(PathType pathType) {
        WeightedMultigraph<Station, PathWeightEdge> graph = new WeightedMultigraph(PathWeightEdge.class);

        lines.forEach(line -> line.getSections()
                .forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    PathWeightEdge edge = new PathWeightEdge(line, section.getDistance(), section.getDuration());
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    graph.addEdge(upStation, downStation, edge);
                    graph.setEdgeWeight(edge, pathType.getWeight(section));
                }));

        return graph;
    }

    private void validateStationExists(WeightedMultigraph<Station, PathWeightEdge> graph,
                                       Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new SubwayException("존재하지 않은 역입니다.");
        }
    }

    public void isValidateRoute(Station source, Station target) {
        this.findPath(source, target, PathType.DISTANCE);
    }

    public Fare calculateFare(Station source, Station target, List<Line> usedLine) {
        int shortestDistance = getShortestDistance(source, target);
        FareCalculatorHandler fareCalculatorHandler = buildCalculatorChain();
        Fare fare = fareCalculatorHandler.handleFareCalculate(shortestDistance, Fare.DEFAULT_FARE);
        Fare lineExtraFare = calculateLineExtraFare(usedLine);
        return fare.plus(lineExtraFare);
    }

    private int getShortestDistance(Station source, Station target) {
        GraphPath<Station, PathWeightEdge> graphPath = getGraphPath(source, target, PathType.DISTANCE);
        return graphPath.getEdgeList().stream().mapToInt(PathWeightEdge::getDistance).sum();
    }

    private static FareCalculatorHandler buildCalculatorChain() {
        FareCalculatorHandler baseFareCalculator = new BaseFareCalculator();
        baseFareCalculator.setNextHandler(new FirstExtraFareCalculator())
                .setNextHandler(new SecondExtraCalculator());
        return baseFareCalculator;
    }

    private Fare calculateLineExtraFare(List<Line> usedLine) {
        int lineExtraFare = usedLine.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
        return Fare.of(lineExtraFare);
    }

}
