package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.FlightDb;
import database.IFlightDb;
import enums.FlightClassEnum;
import enums.FlightParameter;
import models.City;
import models.Flight;
import models.FlightModificationOperation;
import models.FlightParameterValues;

public class FlightDbTests
{
	private IFlightDb flightDb;

	@Before
	public void setUp() throws Exception
	{
		this.flightDb = new FlightDb();		
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testFlightDb()
	{
		assertNotNull(this.flightDb);
	}

	@Test
	public void testNumberOfFlights()
	{
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), i, i+1, i+2);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), i, i+1, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
		}
		int numOfFlights = this.flightDb.numberOfFlights();
		int expectedNumOfFlights = 30;
		assertEquals(expectedNumOfFlights, numOfFlights);
	}

	@Test
	public void testAddFlight()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		boolean result = this.flightDb.addFlight(mtlFlight);
		assertTrue(result);
	}

	@Test
	public void testGetFlight()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		this.flightDb.addFlight(mtlFlight);
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(mtlFlight, retrievedFlight);
	}

	@Test
	public void testRemoveFlight()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		this.flightDb.addFlight(mtlFlight);
		Flight removedFlight = this.flightDb.removeFlight(0);
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(mtlFlight, removedFlight);
		assertNull(retrievedFlight);
	}

	@Test
	public void testEditFlight()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		this.flightDb.addFlight(mtlFlight);
		
		Date newDate = new Date(1000);
		City newDestination = new City("Washington", "WST");
		
		FlightParameterValues params = new FlightParameterValues(newDestination, newDate, 20, 30, 40);
		FlightModificationOperation flightModificationOperationFirst = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.FIRST);
		FlightModificationOperation flightModificationOperationBusiness = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.BUSINESS);
		FlightModificationOperation flightModificationOperationEconomy = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.ECONOMY);
		
		this.flightDb.editFlight(0, flightModificationOperationFirst, params);
		this.flightDb.editFlight(0, flightModificationOperationBusiness, params);
		this.flightDb.editFlight(0, flightModificationOperationEconomy, params);
		this.flightDb.editFlight(0, new FlightModificationOperation(FlightParameter.DATE, FlightClassEnum.ECONOMY), params);
		this.flightDb.editFlight(0, new FlightModificationOperation(FlightParameter.DESTINATION, FlightClassEnum.ECONOMY), params);
		
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(20, retrievedFlight.getFirstClass().getSeats());
		assertEquals(30, retrievedFlight.getBusinessClass().getSeats());
		assertEquals(40, retrievedFlight.getEconomyClass().getSeats());
		assertEquals(newDate, retrievedFlight.getDate());
		assertEquals(newDestination, retrievedFlight.getDestination());
	}
	
	@Test
	public void testNegativeSeats()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 2, 2, 3);
		this.flightDb.addFlight(mtlFlight);
		
		Date newDate = new Date(1000);
		City newDestination = new City("Washington", "WST");
		
		FlightParameterValues params = new FlightParameterValues(newDestination, newDate, 0, 1, 2);
		FlightModificationOperation flightModificationOperationFirst = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.FIRST);
		FlightModificationOperation flightModificationOperationBusiness = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.BUSINESS);
		FlightModificationOperation flightModificationOperationEconomy = new FlightModificationOperation(FlightParameter.SEATS, FlightClassEnum.ECONOMY);
		
		mtlFlight.getFirstClass().acquireSeat();
		mtlFlight.getFirstClass().acquireSeat();
		mtlFlight.getBusinessClass().acquireSeat();
		mtlFlight.getEconomyClass().acquireSeat();
		
		this.flightDb.editFlight(0, flightModificationOperationFirst, params);
		this.flightDb.editFlight(0, flightModificationOperationBusiness, params);
		this.flightDb.editFlight(0, flightModificationOperationEconomy, params);
		
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(0, retrievedFlight.getFirstClass().getSeats());
		assertEquals(-2, retrievedFlight.getFirstClass().getAvailableSeats());
		assertEquals(1, retrievedFlight.getBusinessClass().getSeats());
		assertEquals(0, retrievedFlight.getBusinessClass().getAvailableSeats());
		assertEquals(2, retrievedFlight.getEconomyClass().getSeats());
		assertEquals(1, retrievedFlight.getEconomyClass().getAvailableSeats());
	}

	@Test
	public void testGetFlights()
	{
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), i, i+1, i+2);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), i, i+1, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
			flights.add(wstFlight);
			flights.add(ndlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.getFlights();
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
	}

	@Test
	public void testGetAvailableFlights()
	{
		List<Flight> availableFlights = new ArrayList<Flight>();
		for(int i = 1; i <= 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 0, 0, 0);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), 0, 0, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			availableFlights.add(wstFlight);
			availableFlights.add(ndlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.getAvailableFlights();
		assertArrayEquals(availableFlights.toArray(), retrievedFlights.toArray());
	}

	@Test
	public void testGetFlightsDate()
	{
		Date date = new Date(1000);
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), date, i, i+1, i+2);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), i, i+1, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.getFlights(date);
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
	}

	@Test
	public void testRemoveFlights()
	{
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), i, i+1, i+2);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), i, i+1, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
			flights.add(wstFlight);
			flights.add(ndlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.removeFlights();
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
		assertEquals(0, this.flightDb.numberOfFlights());
	}

	@Test
	public void testRemoveFlightsDate()
	{
		Date date = new Date(1000);
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(new City("Montreal", "MTL"), date, i, i+1, i+2);
			Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), i, i+1, i+2);
			Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), i, i+1, i+2);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.removeFlights(date);
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
		assertEquals(20, this.flightDb.numberOfFlights());
	}

	@Test
	public void testAcquireSeat()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 0, 0, 0);
		Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), 0, 1, 0);
		Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), 0, 0, 5);
		this.flightDb.addFlight(mtlFlight);
		this.flightDb.addFlight(wstFlight);
		this.flightDb.addFlight(ndlFlight);
		assertFalse(this.flightDb.acquireSeat(0, FlightClassEnum.FIRST));
		assertTrue(this.flightDb.acquireSeat(1, FlightClassEnum.BUSINESS));
		assertTrue(this.flightDb.acquireSeat(2, FlightClassEnum.ECONOMY));
		assertEquals(0, mtlFlight.getFirstClass().getAvailableSeats());
		assertEquals(0, wstFlight.getBusinessClass().getAvailableSeats());
		assertEquals(4, ndlFlight.getEconomyClass().getAvailableSeats());
	}

	@Test
	public void testReleaseSeat()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 0, 0, 0);
		Flight wstFlight = new Flight(new City("Washington", "WST"), new Date(), 0, 1, 0);
		Flight ndlFlight = new Flight(new City("NewDelhi", "NDL"), new Date(), 0, 0, 5);
		this.flightDb.addFlight(mtlFlight);
		this.flightDb.addFlight(wstFlight);
		this.flightDb.addFlight(ndlFlight);
		assertFalse(this.flightDb.acquireSeat(0, FlightClassEnum.FIRST));
		assertTrue(this.flightDb.acquireSeat(1, FlightClassEnum.BUSINESS));
		assertTrue(this.flightDb.acquireSeat(2, FlightClassEnum.ECONOMY));
		assertEquals(0, mtlFlight.getFirstClass().getAvailableSeats());
		assertEquals(0, wstFlight.getBusinessClass().getAvailableSeats());
		assertEquals(4, ndlFlight.getEconomyClass().getAvailableSeats());
		assertFalse(this.flightDb.releaseSeat(0, FlightClassEnum.FIRST));
		assertTrue(this.flightDb.releaseSeat(1, FlightClassEnum.BUSINESS));
		assertTrue(this.flightDb.releaseSeat(2, FlightClassEnum.ECONOMY));
		assertEquals(1, wstFlight.getBusinessClass().getAvailableSeats());
		assertEquals(5, ndlFlight.getEconomyClass().getAvailableSeats());
	}

}
