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
import enums.FlightClass;
import enums.FlightParameter;
import models.City;
import models.Flight;
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
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
		}
		int numOfFlights = this.flightDb.numberOfFlights();
		int expectedNumOfFlights = 30;
		assertEquals(expectedNumOfFlights, numOfFlights);
	}

	@Test
	public void testNumberOfFlightsFlightClass()
	{
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
		}
		int numOfFlights = this.flightDb.numberOfFlights(FlightClass.FIRST);
		int expectedNumOfFlights = 10;
		assertEquals(expectedNumOfFlights, numOfFlights);
	}

	@Test
	public void testAddFlight()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 10);
		boolean result = this.flightDb.addFlight(mtlFlight);
		assertTrue(result);
	}

	@Test
	public void testGetFlight()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 10);
		this.flightDb.addFlight(mtlFlight);
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(mtlFlight, retrievedFlight);
	}

	@Test
	public void testRemoveFlight()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 10);
		this.flightDb.addFlight(mtlFlight);
		Flight removedFlight = this.flightDb.removeFlight(0);
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(mtlFlight, removedFlight);
		assertNull(retrievedFlight);
	}

	@Test
	public void testEditFlight()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 10);
		this.flightDb.addFlight(mtlFlight);
		
		int newSeats = 5;
		Date newDate = new Date(1000);
		FlightClass newFlightClass = FlightClass.BUSINESS;
		City newDestination = new City("Washington", "WST");
		
		FlightParameterValues params = new FlightParameterValues();
		params.setSeats(newSeats);
		params.setDate(newDate);
		params.setFlightClass(newFlightClass);
		params.setDestination(newDestination);
		
		this.flightDb.editFlight(0, FlightParameter.SEATS, params);
		this.flightDb.editFlight(0, FlightParameter.DATE, params);
		this.flightDb.editFlight(0, FlightParameter.FLIGHTCLASS, params);
		this.flightDb.editFlight(0, FlightParameter.DESTINATION, params);
		
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(newSeats, retrievedFlight.getSeats());
		assertEquals(newDate, retrievedFlight.getDate());
		assertEquals(newFlightClass, retrievedFlight.getFlightClass());
		assertEquals(newDestination, retrievedFlight.getDestination());
	}
	
	@Test
	public void testNegativeSeats()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 2);
		this.flightDb.addFlight(mtlFlight);
		
		int newSeats = -5;
		Date newDate = new Date(1000);
		FlightClass newFlightClass = FlightClass.BUSINESS;
		City newDestination = new City("Washington", "WST");
		
		FlightParameterValues params = new FlightParameterValues();
		params.setSeats(newSeats);
		params.setDate(newDate);
		params.setFlightClass(newFlightClass);
		params.setDestination(newDestination);
		
		mtlFlight.acquireSeat();
		mtlFlight.acquireSeat();
		
		this.flightDb.editFlight(0, FlightParameter.SEATS, params);
		this.flightDb.editFlight(0, FlightParameter.DATE, params);
		this.flightDb.editFlight(0, FlightParameter.FLIGHTCLASS, params);
		this.flightDb.editFlight(0, FlightParameter.DESTINATION, params);
		
		Flight retrievedFlight = this.flightDb.getFlight(0);
		assertEquals(0, retrievedFlight.getSeats());
		assertEquals(-2, retrievedFlight.getAvailableSeats());
		assertEquals(newDate, retrievedFlight.getDate());
		assertEquals(newFlightClass, retrievedFlight.getFlightClass());
		assertEquals(newDestination, retrievedFlight.getDestination());
	}

	@Test
	public void testGetFlights()
	{
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
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
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 0);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
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
	public void testGetFlightsFlightClass()
	{
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(ndlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.getFlights(FlightClass.BUSINESS);
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
	}

	@Test
	public void testGetFlightsDate()
	{
		Date date = new Date(1000);
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), date, i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
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
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
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
	public void testRemoveFlightsFlightClass()
	{
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.removeFlights(FlightClass.FIRST);
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
		assertEquals(20, this.flightDb.numberOfFlights());
	}

	@Test
	public void testRemoveFlightsDate()
	{
		Date date = new Date(1000);
		List<Flight> flights = new ArrayList<Flight>();
		for(int i = 0; i < 10; ++i){
			Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), date, i);
			Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), i);
			Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), i);
			this.flightDb.addFlight(mtlFlight);
			this.flightDb.addFlight(wstFlight);
			this.flightDb.addFlight(ndlFlight);
			flights.add(mtlFlight);
		}
		List<Flight> retrievedFlights = this.flightDb.removeFlights(FlightClass.FIRST);
		assertArrayEquals(flights.toArray(), retrievedFlights.toArray());
		assertEquals(20, this.flightDb.numberOfFlights());
	}

	@Test
	public void testAcquireSeat()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 0);
		Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), 1);
		Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), 5);
		this.flightDb.addFlight(mtlFlight);
		this.flightDb.addFlight(wstFlight);
		this.flightDb.addFlight(ndlFlight);
		assertFalse(this.flightDb.acquireSeat(0));
		assertTrue(this.flightDb.acquireSeat(1));
		assertTrue(this.flightDb.acquireSeat(2));
		assertEquals(0, mtlFlight.getAvailableSeats());
		assertEquals(0, wstFlight.getAvailableSeats());
		assertEquals(4, ndlFlight.getAvailableSeats());
	}

	@Test
	public void testReleaseSeat()
	{
		Flight mtlFlight = new Flight(FlightClass.FIRST, new City("Montreal", "MTL"), new Date(), 0);
		Flight wstFlight = new Flight(FlightClass.ECONOMY, new City("Washington", "WST"), new Date(), 1);
		Flight ndlFlight = new Flight(FlightClass.BUSINESS, new City("NewDelhi", "NDL"), new Date(), 5);
		this.flightDb.addFlight(mtlFlight);
		this.flightDb.addFlight(wstFlight);
		this.flightDb.addFlight(ndlFlight);
		assertFalse(this.flightDb.acquireSeat(0));
		assertTrue(this.flightDb.acquireSeat(1));
		assertTrue(this.flightDb.acquireSeat(2));
		assertFalse(this.flightDb.releaseSeat(0));
		assertTrue(this.flightDb.releaseSeat(1));
		assertTrue(this.flightDb.releaseSeat(2));
		assertEquals(0, mtlFlight.getAvailableSeats());
		assertEquals(1, wstFlight.getAvailableSeats());
		assertEquals(5, ndlFlight.getAvailableSeats());
	}

}
