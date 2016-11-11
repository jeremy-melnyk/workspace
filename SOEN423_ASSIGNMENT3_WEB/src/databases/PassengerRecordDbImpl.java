package databases;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import models.Address;
import models.PassengerRecord;

public class PassengerRecordDbImpl implements PassengerRecordDb {
	private int RECORD_ID = 0;
	private HashMap<Integer, PassengerRecord> records;
	private ReadWriteLock recordsLock;
	private Lock idLock;

	public PassengerRecordDbImpl() {
		super();
		this.records = new HashMap<Integer, PassengerRecord>();
		this.recordsLock = new ReentrantReadWriteLock(true);
		this.idLock = new ReentrantLock(true);
	}

	@Override
	public PassengerRecord getPassengerRecord(Integer id) {
		recordsLock.readLock().lock();
		try {
			return records.get(id);
		} finally {
			recordsLock.readLock().unlock();
		}
	}

	@Override
	public PassengerRecord[] getPassengerRecords() {
		recordsLock.readLock().lock();
		try {
			PassengerRecord[] passengerRecords = new PassengerRecord[records.size()];
			return records.values().toArray(passengerRecords);
		} finally {
			recordsLock.readLock().unlock();
		}
	}

	@Override
	public PassengerRecord removePassengerRecord(Integer id) {
		recordsLock.writeLock().lock();
		try {
			return records.remove(id);
		} finally {
			recordsLock.writeLock().unlock();
		}
	}

	@Override
	public PassengerRecord addPassengerRecord(String lastName, String firstName, Address address, String phoneNumber) {
		int id = 0;
		idLock.lock();
		try {
			id = RECORD_ID++;
		} finally {
			idLock.unlock();
		}

		PassengerRecord record = new PassengerRecord(id, lastName, firstName, address, phoneNumber);
		recordsLock.writeLock().lock();
		try {
			if (!records.containsKey(id)){
				records.put(id, record);	
			}
		} finally {
			recordsLock.writeLock().unlock();
		}
		return record;
	}
}
