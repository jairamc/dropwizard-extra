package com.datasift.dropwizard.hbase.scanner;

import com.datasift.dropwizard.hbase.util.PermitReleasingCallback;
import com.stumbleupon.async.Deferred;
import org.hbase.async.KeyValue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * A Scanner that bounds its simultaneous requests with a {@link Semaphore}.
 */
public class BoundedRowScanner implements RowScanner {

    private RowScanner scanner;
    private Semaphore semaphore;

    public BoundedRowScanner(RowScanner scanner, Semaphore semaphore) {
        this.scanner = scanner;
        this.semaphore = semaphore;
    }

    public byte[] getCurrentKey() {
        return scanner.getCurrentKey();
    }

    public void setStartKey(byte[] start_key) {
        scanner.setStartKey(start_key);
    }

    public void setStartKey(String start_key) {
        scanner.setStartKey(start_key);
    }

    public void setStopKey(byte[] stop_key) {
        scanner.setStopKey(stop_key);
    }

    public void setStopKey(String stop_key) {
        scanner.setStopKey(stop_key);
    }

    public void setFamily(byte[] family) {
        scanner.setFamily(family);
    }

    public void setFamily(String family) {
        scanner.setFamily(family);
    }

    public void setQualifier(byte[] qualifier) {
        scanner.setQualifier(qualifier);
    }

    public void setQualifier(String qualifier) {
        scanner.setQualifier(qualifier);
    }

    public void setKeyRegexp(String regexp) {
        scanner.setKeyRegexp(regexp);
    }

    public void setKeyRegexp(String regexp, Charset charset) {
        scanner.setKeyRegexp(regexp, charset);
    }

    public void setServerBlockCache(boolean populate_blockcache) {
        scanner.setServerBlockCache(populate_blockcache);
    }

    public void setMaxNumRows(int max_num_rows) {
        scanner.setMaxNumRows(max_num_rows);
    }

    public void setMaxNumKeyValues(int max_num_kvs) {
        scanner.setMaxNumKeyValues(max_num_kvs);
    }

    public void setMinTimestamp(long timestamp) {
        scanner.setMinTimestamp(timestamp);
    }

    public long getMinTimestamp() {
        return scanner.getMinTimestamp();
    }

    public void setMaxTimestamp(long timestamp) {
        scanner.setMaxTimestamp(timestamp);
    }

    public long getMaxTimestamp() {
        return scanner.getMaxTimestamp();
    }

    public void setTimeRange(long min_timestamp, long max_timestamp) {
        scanner.setTimeRange(min_timestamp, max_timestamp);
    }

    public Deferred<Object> close() {
        semaphore.acquireUninterruptibly();
        return scanner.close().addBoth(new PermitReleasingCallback<Object>(semaphore));
    }

    public Deferred<ArrayList<ArrayList<KeyValue>>> nextRows() {
        semaphore.acquireUninterruptibly();
        return scanner.nextRows().addBoth(new PermitReleasingCallback<ArrayList<ArrayList<KeyValue>>>(semaphore));
    }

    public Deferred<ArrayList<ArrayList<KeyValue>>> nextRows(int rows) {
        semaphore.acquireUninterruptibly();
        return scanner.nextRows(rows).addBoth(new PermitReleasingCallback<ArrayList<ArrayList<KeyValue>>>(semaphore));
    }
}
