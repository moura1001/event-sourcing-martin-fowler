package moura.learn.eventsourcing;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Country;
import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;
import moura.learn.eventsourcing.event.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
	void contextLoads() {
	}

}
