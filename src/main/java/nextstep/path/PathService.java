package nextstep.path;

import nextstep.exception.SubwayException;
import nextstep.line.Line;
import nextstep.line.LineRepository;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(() -> new SubwayException("역을 찾을 수 없습니다."));
        Station targetStation = stationRepository.findById(target).orElseThrow(() -> new SubwayException("역을 찾을 수 없습니다."));
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation, type);
        return new PathResponse(path);
    }
}
