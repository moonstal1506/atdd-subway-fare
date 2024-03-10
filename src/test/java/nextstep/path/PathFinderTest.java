package nextstep.path;

import nextstep.exception.SubwayException;
import nextstep.line.Line;
import nextstep.section.Section;
import nextstep.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 수내역;
    private Station 정자역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private Line 수인분당선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        수내역 = new Station("수내역");
        정자역 = new Station("정자역");

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10, 5);
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10, 5);
        삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 2, 5);
        수인분당선 = new Line("수인분당선", "yellow", 수내역, 정자역, 2, 5);

        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 3, 5));
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void findPath() {
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.findPath(Arrays.asList(이호선, 신분당선, 삼호선), 교대역, 양재역);

        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5L);
    }

    @DisplayName("출발역과 도착역이 같은 경우 에러를 반환한다.")
    @Test
    void validateEqualsStation() {
        PathFinder pathFinder = new PathFinder();

        assertThatThrownBy(() -> pathFinder.findPath(Arrays.asList(이호선, 신분당선, 삼호선), 교대역, 교대역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러를 반환한다.")
    @Test
    void validatePathExists() {
        PathFinder pathFinder = new PathFinder();

        assertThatThrownBy(() -> pathFinder.findPath(Arrays.asList(이호선, 수인분당선), 강남역, 수내역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 에러를 반환한다.")
    @Test
    void validateStationExists() {
        PathFinder pathFinder = new PathFinder();

        assertThatThrownBy(() -> pathFinder.findPath(Arrays.asList(이호선), 강남역, 수내역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining("존재하지 않은 역입니다.");
    }

}
