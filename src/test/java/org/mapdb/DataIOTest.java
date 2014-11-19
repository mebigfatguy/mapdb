package org.mapdb;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mapdb.DataIO.*;

public class DataIOTest {

    @Test public void parity1() {
        assertEquals(Long.parseLong("1", 2), parity1Set(0));
        assertEquals(Long.parseLong("10", 2), parity1Set(2));
        assertEquals(Long.parseLong("111", 2), parity1Set(Long.parseLong("110", 2)));
        assertEquals(Long.parseLong("1110", 2), parity1Set(Long.parseLong("1110", 2)));
        assertEquals(Long.parseLong("1011", 2), parity1Set(Long.parseLong("1010", 2)));
        assertEquals(Long.parseLong("11111", 2), parity1Set(Long.parseLong("11110", 2)));

        assertEquals(0, parity1Get(Long.parseLong("1", 2)));
        try {
            parity1Get(Long.parseLong("0", 2));
            fail();
        }catch(InternalError e){
            //TODO check mapdb specific error;
        }
        try {
            parity1Get(Long.parseLong("110", 2));
            fail();
        }catch(InternalError e){
            //TODO check mapdb specific error;
        }
    }

    @Test
    public void testPackLongBidi() throws Exception {
        DataOutputByteArray b = new DataOutputByteArray();

        long max = (long) 1e14;
        for(long i=0;i<max;i=i+1 +i/100000){
            b.pos=0;
            long size = packLongBidi(b,i);
            assertTrue(i>100000 || size<6);
            assertEquals(b.pos,size);
            assertEquals(i | (size<<56), unpackLongBidi(b.buf,0));
            assertEquals(i | (size<<56), unpackLongBidiReverse(b.buf, (int) size));
        }
    }
}