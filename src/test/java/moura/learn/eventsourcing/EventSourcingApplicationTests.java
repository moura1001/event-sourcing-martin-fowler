package moura.learn.eventsourcing;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Country;
import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;
import moura.learn.eventsourcing.event.*;
import moura.learn.eventsourcing.service.Registry;
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
        eProc.Process(new DepartureEvent(new Date(2005,10,8), la, kr));
        eProc.Process(new ArrivalEvent(new Date(2005,11,1), sfo, kr));
        eProc.Process(new DepartureEvent(new Date(2005,11,5), sfo, kr));
		assertThat(eProc.NumberOfEvents()).isEqualTo(4);
        assertThat(kr.GetPort()).isEqualTo(Port.AT_SEA);
    }

	@Test
	void VisitingCanadaMarksCargo() {
		eProc.Process(new LoadEvent(new Date(2005, 11, 1), refact, kr));
		eProc.Process(new ArrivalEvent(new Date(2005,11,2), yyv, kr));
		eProc.Process(new DepartureEvent(new Date(2005,11,3), yyv, kr));
		eProc.Process(new ArrivalEvent(new Date(2005,11,4), sfo, kr));
		assertThat(kr.LoadQuantity()).isEqualTo(1);
		eProc.Process(new UnloadEvent(new Date(2005,11,5), refact, kr));
		assertThat(kr.LoadQuantity()).isEqualTo(0);
		assertThat(eProc.NumberOfEvents()).isEqualTo(5);
		assertThat(refact.HasBeenInCanada()).isTrue();
	}

	@Test
	void OnArrivalSendsNotification() {
		Registry registry = new Registry(new MockCustomEventGateway());
		//Registry registry = new Registry(new CustomEventGateway(eProc));
		Port port = new Port("Test", Country.US, registry);
		ArrivalEvent ev = new ArrivalEvent(new Date(2005-1900,11,1), port, kr);
		eProc.Process(ev);
		assertThat(eProc.NumberOfEvents()).isEqualTo(1);
		assertThat(kr.GetPort()).isEqualTo(port);
		assertThat(((MockCustomEventGateway) registry.GetCustomNotificationGateway()).isNotified()).isTrue();
	}

	@Test
	void ReversingTheLoadEventLeavesTheShipEmpty() {
		Ship oi = new Ship("Old Ironsides");
		eProc.Process(new ArrivalEvent(new Date(2005,11,1), sfo, oi));
		eProc.Process(new LoadEvent(new Date(2005, 11, 1), refact, oi));
		eProc.Process(new DepartureEvent(new Date(2005,11,2), sfo, oi));
		eProc.Process(new ArrivalEvent(new Date(2005,11,3), la, oi));
		eProc.Process(new UnloadEvent(new Date(2005, 11, 3), refact, oi));
		assertThat(oi.LoadQuantity()).isEqualTo(0);

		LoadEvent ev = new LoadEvent(new Date(2005, 11, 4), refact, kr);
		eProc.Process(ev);
		assertThat(eProc.NumberOfEvents()).isEqualTo(6);
		assertThat(kr.LoadQuantity()).isEqualTo(1);
		assertThat(refact.GetShip()).isEqualTo(kr);
		assertThat(refact.GetPort()).isNull();
		ev.Reverse();
		assertThat(kr.LoadQuantity()).isEqualTo(0);
		assertThat(refact.GetShip()).isNull();
		assertThat(refact.GetPort()).isEqualTo(la);
	}

	@Test
	void RevertsArrivalInCanada() {
		Cargo cargo = new Cargo("Test");

		eProc.Process(new ArrivalEvent(new Date(2005,11,1), sfo, kr));
		eProc.Process(new LoadEvent(new Date(2005, 11, 1), refact, kr));
		eProc.Process(new LoadEvent(new Date(2005, 11, 1), cargo, kr));
		eProc.Process(new DepartureEvent(new Date(2005,11,2), sfo, kr));
		assertThat(kr.LoadQuantity()).isEqualTo(2);

		ArrivalEvent ev = new ArrivalEvent(new Date(2005, 11, 3), yyv, kr);
		eProc.Process(ev);
		assertThat(kr.GetPort()).isEqualTo(yyv);
		assertThat(refact.GetShip()).isEqualTo(kr);
		assertThat(refact.GetPort()).isNull();
		assertThat(refact.HasBeenInCanada()).isTrue();
		assertThat(cargo.GetShip()).isEqualTo(kr);
		assertThat(cargo.GetPort()).isNull();
		assertThat(cargo.HasBeenInCanada()).isTrue();

		ev.Reverse();
		assertThat(kr.GetPort()).isEqualTo(Port.AT_SEA);
		assertThat(refact.GetShip()).isEqualTo(kr);
		assertThat(refact.GetPort()).isNull();
		assertThat(refact.HasBeenInCanada()).isFalse();
		assertThat(cargo.GetShip()).isEqualTo(kr);
		assertThat(cargo.GetPort()).isNull();
		assertThat(cargo.HasBeenInCanada()).isFalse();

		assertThat(kr.LoadQuantity()).isEqualTo(2);
	}

    @Test
    void IgnoresInconsistentArrivalAndDepartureEventsFromTheSameShip() {
        eProc.Process(new ArrivalEvent(new Date(2005,10,1), la, kr));
        eProc.Process(new ArrivalEvent(new Date(2005,11,1), sfo, kr));
		assertThat(kr.GetPort()).isEqualTo(la);

		eProc.Process(new DepartureEvent(new Date(2005,11,8), sfo, kr));
        eProc.Process(new DepartureEvent(new Date(2005,9,1), la, kr));
        assertThat(eProc.NumberOfEvents()).isEqualTo(1);
        assertThat(kr.GetPort()).isEqualTo(la);

		eProc.Process(new DepartureEvent(new Date(2005,11,8), la, kr));
        assertThat(eProc.NumberOfEvents()).isEqualTo(2);
        assertThat(kr.GetPort()).isEqualTo(Port.AT_SEA);
    }

}
