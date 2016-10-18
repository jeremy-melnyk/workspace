package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.IPassengerRecordDb;
import database.PassengerRecordDb;
import enums.FlightClassEnum;
import models.Address;
import models.City;
import models.Flight;
import models.Passenger;
import models.PassengerRecord;

public class PassengerRecordDbTests
{
	IPassengerRecordDb passengerRecordDb;
	
	@Before
	public void setUp() throws Exception
	{
		this.passengerRecordDb = new PassengerRecordDb();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testPassengerRecordDb()
	{
		assertNotNull(this.passengerRecordDb);
	}

	@Test
	public void testNumberOfRecords()
	{
		Flight mtlFlight = new Flight( new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight, FlightClassEnum.BUSINESS, new Date());
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		int expected = 2;
		int actual = this.passengerRecordDb.numberOfRecords();
		assertEquals(expected, actual);
	}

	@Test
	public void testNumberOfRecordsFlightClass()
	{
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		int expected = 1;
		int actual = this.passengerRecordDb.numberOfRecords(FlightClassEnum.BUSINESS);
		assertEquals(expected, actual);
	}

	@Test
	public void testAddRecord()
	{
		Flight mtlFlight = new Flight( new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.ECONOMY, new Date());
		boolean result = false;
		try
		{
			result = this.passengerRecordDb.addRecord(firstRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		assertTrue(result);
	}

	@Test
	public void testGetRecord()
	{
		Flight mtlFlight = new Flight( new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		PassengerRecord retrievedRecord = this.passengerRecordDb.getRecord(0);
		assertEquals(firstRecord, retrievedRecord);
	}

	@Test
	public void testRemoveRecord()
	{
		Flight mtlFlight = new Flight( new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		PassengerRecord retrievedRecord = this.passengerRecordDb.removeRecord(0);
		assertEquals(firstRecord, retrievedRecord);
		assertEquals(0, this.passengerRecordDb.numberOfRecords());
	}

	@Test
	public void testGetRecords()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		records.add(firstRecord);
		records.add(secondRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.getRecords();
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
	}

	@Test
	public void testGetRecordsFlightClass()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		records.add(firstRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.getRecords(FlightClassEnum.FIRST);
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
	}

	@Test
	public void testGetRecordsChar()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		records.add(secondRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.getRecords('G');
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
	}

	@Test
	public void testRemoveRecords()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.BUSINESS, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.FIRST, new Date());
		records.add(firstRecord);
		records.add(secondRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.removeRecords();
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
		assertEquals(0, this.passengerRecordDb.numberOfRecords());
	}

	@Test
	public void testRemoveRecordsFlightClass()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		records.add(secondRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.removeRecords(FlightClassEnum.BUSINESS);
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
		assertEquals(1, this.passengerRecordDb.numberOfRecords());
	}

	@Test
	public void testRemoveRecordsChar()
	{
		List<PassengerRecord> records = new ArrayList<PassengerRecord>();
		Flight mtlFlight = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Flight mtlFlight2 = new Flight(new City("Montreal", "MTL"), new Date(), 10, 20, 30);
		Address firstAddress = new Address("SomeStreet", "SomeCity", "SomeProvince", "SomePostalCode", "SomeCountry");
		Passenger firstPassenger = new Passenger("John", "Doe", "5141234578", firstAddress);
		Passenger secondPassenger = new Passenger("Alex", "Galchenyuk", "5141234578", firstAddress);
		
		PassengerRecord firstRecord = new PassengerRecord(firstPassenger, mtlFlight, FlightClassEnum.FIRST, new Date());
		PassengerRecord secondRecord = new PassengerRecord(secondPassenger, mtlFlight2, FlightClassEnum.BUSINESS, new Date());
		records.add(secondRecord);
		try
		{
			this.passengerRecordDb.addRecord(firstRecord);
			this.passengerRecordDb.addRecord(secondRecord);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		List<PassengerRecord> retrievedRecords = this.passengerRecordDb.removeRecords('G');
		assertArrayEquals(records.toArray(), retrievedRecords.toArray());
		assertEquals(1, this.passengerRecordDb.numberOfRecords());
	}

}
