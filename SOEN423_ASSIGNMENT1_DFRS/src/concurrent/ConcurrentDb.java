package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentDb
{
	private Lock threadQueue;
	private Lock readerCountAccess;
	private Semaphore writePermit;
	private int readers;

	public ConcurrentDb() {
		// Queue for reader and writer threads
		this.threadQueue = new ReentrantLock(true);
		// Limit read count access to one reader thread
		this.readerCountAccess = new ReentrantLock(true);
		// One writer permit
		this.writePermit = new Semaphore(1, true);
		// Number of concurrent readers
		this.readers = 0;
	}

	protected void requestRead()
	{
		// Next thread in line for a request
		this.threadQueue.lock();
		// Next thread in line for reader count access
		this.readerCountAccess.lock();
		if (this.readers == 0)
		{
			try
			{
				// Acquire write permit if you are the first reader
				this.writePermit.acquire();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		++this.readers;
		// Signal next thread in line for a request
		this.threadQueue.unlock();
		// Signal next thread in line for reader count access
		this.readerCountAccess.unlock();
	}

	protected void releaseRead()
	{
		// Next thread in line for reader count access
		this.readerCountAccess.lock();
		--this.readers;
		if (this.readers == 0)
		{
			// Release write permit if you are the last reader
			this.writePermit.release();
		}
		// Signal next thread in line for reader count access
		this.readerCountAccess.unlock();
	}

	protected void requestWrite()
	{
		// Next thread in line for a request
		this.threadQueue.lock();
		try
		{
			// Acquire write permit
			this.writePermit.acquire();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// Signal next thread in line for a request
		this.threadQueue.unlock();
	}

	protected void releaseWrite()
	{
		// Release write permit
		this.writePermit.release();
	}
}
