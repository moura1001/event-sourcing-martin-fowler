package moura.learn.eventsourcing;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Country;
import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;
import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.event.DepartureEvent;
import moura.learn.eventsourcing.event.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventSourcingApplicationTests {

	Ship kr;
	Port sfo, la, yyv;
	Cargo refact;
	EventProcessor eProc;

	@BeforeEach
	void setUp(){
		eProc = new EventProcessor();
		refact = new Cargo("Refactoring");
		kr = new Ship("King Roy");
		sfo = new Port("San Francisco", Country.US);
		la = new Port("Los Angeles", Country.US);
		yyv = new Port("Vancouver", Country.CANADA);
	}

	@Test
	void ArrivalSetsShipsLocation() {
		ArrivalEvent ev = new ArrivalEvent(new Date(2005,11,1), sfo, kr);
		eProc.Process(ev);
		assertThat(eProc.NumberOfEvents()).isEqualTo(1);
		assertThat(kr.GetPort()).isEqualTo(sfo);
	}

    @Test
    void DeparturePutsShipsOutToSea() {
        eProc.Process(new ArrivalEvent(new Date(2005,10,1), la, kr));
        eProc.Process(new ArrivalEvent(new Date(2005,11,1), sfo, kr));
        eProc.Process(new DepartureEvent(new Date(2005,10,1), sfo, kr));
		assertThat(eProc.NumberOfEvents()).isEqualTo(3);
        assertThat(kr.GetPort()).isEqualTo(Port.AT_SEA);
    }

}
