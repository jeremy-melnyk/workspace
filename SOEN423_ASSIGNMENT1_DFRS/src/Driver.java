import database.IPassengerRecordDb;
import database.PassengerRecordDb;
import tests.MockClientReader;
import tests.MockClientWriter;

public class Driver {
	
	public static void main(String[] args) {
		IPassengerRecordDb passengerRecordDb = new PassengerRecordDb();
		MockClientReader mockReader0 = new MockClientReader(passengerRecordDb);
		MockClientWriter mockWriter0 = new MockClientWriter(passengerRecordDb);
		MockClientReader mockReader1 = new MockClientReader(passengerRecordDb);
		MockClientWriter mockWriter1 = new MockClientWriter(passengerRecordDb);
		Thread t1 = new Thread(mockReader0);
		Thread t2 = new Thread(mockWriter0);
		Thread t3 = new Thread(mockReader1);
		Thread t4 = new Thread(mockWriter1);
		t2.start();
		t4.start();
		t1.start();
		t3.start();
	}

}
