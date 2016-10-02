package tests;
import org.junit.Test;

import database.IPassengerRecordDb;
import database.PassengerRecordDb;

public class PassengerRecordDbTests {

	private IPassengerRecordDb passengerRecordDb;

	@Test
	public void setUp() {
		this.passengerRecordDb = new PassengerRecordDb();
	}

	@Test
	public void testConcurrentWriteRead() {
		MockClientReader mockReader0 = new MockClientReader(this.passengerRecordDb);
		MockClientWriter mockWriter0 = new MockClientWriter(this.passengerRecordDb);
		MockClientReader mockReader1 = new MockClientReader(this.passengerRecordDb);
		MockClientWriter mockWriter1 = new MockClientWriter(this.passengerRecordDb);
		Thread t1 = new Thread(mockReader0);
		Thread t2 = new Thread(mockWriter0);
		Thread t3 = new Thread(mockReader1);
		Thread t4 = new Thread(mockWriter1);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
