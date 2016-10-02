package concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentDb {
	private final Lock mutex = new ReentrantLock(false);
    private int readers;
    private int writers;
    private int writerRequests;

    public ConcurrentDb(){
        this.readers = 0;
        this.writers = 0;
        this.writerRequests = 0;
    }

    public void requestRead(){
    	this.mutex.lock();
        while(this.writers > 0 || this.writerRequests > 0){
        	this.mutex.unlock();
        	try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	this.mutex.lock();
    	}
        ++this.readers;
        this.mutex.unlock();
    }

    public void releaseRead(){
    	this.mutex.lock();
    	--this.readers;
    	this.mutex.unlock();
    }

    public void requestWrite(){
    	this.mutex.lock();
        ++this.writerRequests;
        while(this.readers > 0 || this.writers > 0){
        	this.mutex.unlock();
            try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            this.mutex.lock();
        }      
        --this.writerRequests;
        ++this.writers; 
        this.mutex.unlock();
    }

    public void releaseWrite(){
    	this.mutex.lock();
        --this.writers;
        this.mutex.unlock();
    }
}
