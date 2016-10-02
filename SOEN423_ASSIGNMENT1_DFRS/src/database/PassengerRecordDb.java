package database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import concurrent.ConcurrentDb;
import models.Passenger;
import models.PassengerRecord;

public class PassengerRecordDb extends ConcurrentDb implements IPassengerRecordDb {
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	private HashMap<Character, List<PassengerRecord>> passengerRecords;

	public PassengerRecordDb() {
		this.passengerRecords = new HashMap<Character, List<PassengerRecord>>();
	}

	@Override
	public boolean addRecord(PassengerRecord passengerRecord) {
		if (passengerRecord == null) {
			return false;
		}

		Passenger passenger = passengerRecord.getPassenger();
		if (passenger == null) {
			return false;
		}

		String lastName = passenger.getLastName();
		if (lastName == null || lastName.length() < 1) {
			return false;
		}

		char firstLetter = Character.toUpperCase(lastName.charAt(0));
		List<PassengerRecord> passengerRecordList = null;
		readWriteLock.writeLock().lock();
		try {
			if (this.passengerRecords.containsKey(firstLetter)) {
				readWriteLock.writeLock().unlock();
				passengerRecordList = this.passengerRecords.get(firstLetter);
				readWriteLock.writeLock().lock();
				if (!passengerRecordList.contains(passengerRecord)) {
					passengerRecordList.add(passengerRecord);
				} else {
					// Record already exists.
					return false;
				}
			} else {
				passengerRecordList = new ArrayList<PassengerRecord>();
				passengerRecordList.add(passengerRecord);
				this.passengerRecords.put(firstLetter, passengerRecordList);
			}
			return true;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public List<PassengerRecord> retrieveRecords(char character) {
		char firstLetter = Character.toUpperCase(character);
		readWriteLock.readLock().lock();
		try {
			if (this.passengerRecords.containsKey(firstLetter)) {
				return this.passengerRecords.get(firstLetter);
			}
			return null;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public PassengerRecord retrieveRecord(int recordId) {
		if (recordId < 0) {
			return null;
		}

		readWriteLock.readLock().lock();
		try {
			Set<Character> passengerRecordsKeys = this.passengerRecords.keySet();
			for (char key : passengerRecordsKeys) {
				List<PassengerRecord> passengerRecordList = this.passengerRecords.get(key);
				if (passengerRecordList == null) {
					continue;
				}
				int passengerRecordsListSize = passengerRecordList.size();
				for (int j = 0; j < passengerRecordsListSize; ++j) {
					PassengerRecord passengerRecord = passengerRecordList.get(j);
					if (passengerRecord == null) {
						continue;
					}
					if (passengerRecord.getRecordId() == recordId) {
						return passengerRecord;
					}
				}
			}
			return null;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public PassengerRecord removeRecord(int recordId) {
		if (recordId < 0) {
			return null;
		}

		readWriteLock.readLock().lock();
		try {
			int passengerRecordsSize = this.passengerRecords.size();
			for (int i = 0; i < passengerRecordsSize; ++i) {
				List<PassengerRecord> passengerRecordList = this.passengerRecords.get(i);
				if (passengerRecordList == null) {
					continue;
				}
				int passengerRecordsListSize = passengerRecordList.size();
				for (int j = 0; j < passengerRecordsListSize; ++j) {
					PassengerRecord passengerRecord = passengerRecordList.get(j);
					if (passengerRecord == null) {
						continue;
					}
					if (passengerRecord.getRecordId() == recordId) {
						readWriteLock.readLock().unlock();
						readWriteLock.writeLock().lock();
						PassengerRecord foundPassengerRecord = passengerRecordList.remove(j);
						readWriteLock.readLock().lock();
						readWriteLock.writeLock().unlock();
						return foundPassengerRecord;
					}
				}
			}
			return null;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public List<PassengerRecord> retrieveRecords(Passenger passenger) {
		if (passenger == null) {
			return null;
		}

		String firstName = passenger.getFirstName();
		if (firstName == null || firstName.length() < 1) {
			return null;
		}

		String lastName = passenger.getLastName();
		if (lastName == null || lastName.length() < 1) {
			return null;
		}

		List<PassengerRecord> passengerRecordsForPassenger = new ArrayList<PassengerRecord>();
		char firstLetter = Character.toUpperCase(lastName.charAt(0));
		readWriteLock.readLock().lock();
		try {
			if (this.passengerRecords.containsKey(firstLetter)) {
				List<PassengerRecord> passengerRecordList = this.passengerRecords.get(firstLetter);
				for (PassengerRecord passengerRecord : passengerRecordList) {
					Passenger passengerOnRecord = passengerRecord.getPassenger();
					if (passengerOnRecord == null) {
						continue;
					}
					String passengerOnRecordFirstName = passengerOnRecord.getFirstName();
					String passengerOnRecordLastName = passengerOnRecord.getLastName();

					if (passengerOnRecordFirstName != null && passengerOnRecordLastName != null) {
						if (firstName.equals(passengerOnRecordFirstName)
								&& lastName.equals(passengerOnRecordLastName)) {
							passengerRecordsForPassenger.add(passengerRecord);
						}
					}
				}
			}
			return passengerRecordsForPassenger;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public List<PassengerRecord> removeRecords(Passenger passenger) {
		if (passenger == null) {
			return null;
		}

		String firstName = passenger.getFirstName();
		if (firstName == null || firstName.length() < 1) {
			return null;
		}

		String lastName = passenger.getLastName();
		if (lastName == null || lastName.length() < 1) {
			return null;
		}

		List<PassengerRecord> passengerRecordsForPassenger = new ArrayList<PassengerRecord>();
		char firstLetter = Character.toUpperCase(lastName.charAt(0));
		readWriteLock.readLock().lock();
		try {
			if (this.passengerRecords.containsKey(firstLetter)) {
				List<PassengerRecord> passengerRecordList = this.passengerRecords.get(firstLetter);
				int passengerRecordListSize = passengerRecordList.size();
				for (int i = passengerRecordListSize - 1; i >= 0; --i) {
					PassengerRecord passengerRecord = passengerRecordList.get(i);
					if (passengerRecord == null) {
						continue;
					}
					Passenger passengerOnRecord = passengerRecord.getPassenger();
					if (passengerOnRecord == null) {
						continue;
					}
					String passengerOnRecordFirstName = passengerOnRecord.getFirstName();
					String passengerOnRecordLastName = passengerOnRecord.getLastName();
					if (passengerOnRecordFirstName != null && passengerOnRecordLastName != null) {
						if (firstName.equals(passengerOnRecordFirstName)
								&& lastName.equals(passengerOnRecordLastName)) {
							readWriteLock.readLock().unlock();
							readWriteLock.writeLock().lock();
							passengerRecordsForPassenger.add(passengerRecordList.remove(i));
							readWriteLock.readLock().lock();
							readWriteLock.writeLock().unlock();
						}
					}
				}
			}
			return passengerRecordsForPassenger;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}
}
