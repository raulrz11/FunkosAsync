package utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IdGenerator {
    private int id = 1;
    private Lock lockId = new ReentrantLock(true);

    public int getMyId() {
        lockId.lock();
        try {
            return id++;
        }finally {
            lockId.unlock();
        }
    }

    public void setMyId(int value){
        lockId.lock();
        try {
            id = value;
        }finally {
            lockId.unlock();
        }
    }
}
