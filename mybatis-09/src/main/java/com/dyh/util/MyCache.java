package com.dyh.util;

import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;

public class MyCache implements Cache {
    public String getId() {
        return null;
    }

    public void putObject(Object o, Object o1) {

    }

    public Object getObject(Object o) {
        return null;
    }

    public Object removeObject(Object o) {
        return null;
    }

    public void clear() {

    }

    public int getSize() {
        return 0;
    }

    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
