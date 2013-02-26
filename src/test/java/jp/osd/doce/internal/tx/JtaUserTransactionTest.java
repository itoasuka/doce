package jp.osd.doce.internal.tx;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import jp.osd.doce.DoceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JtaUserTransactionTest {
    private UserTransaction tx;

    private JtaUserTransaction jut;

    @Before
    public void setUp() throws Exception {
        tx = createMock(UserTransaction.class);
        jut = new JtaUserTransaction(null, tx);
    }

    @After
    public void tearDown() throws Exception {
        verify(tx);
    }

    @Test
    public void testBegin() throws Exception {
        tx.begin();
        expectLastCall();
        replay(tx);
        jut.begin();
    }

    @Test
    public void testBegin_2() throws Exception {
        tx.begin();
        expectLastCall().andThrow(new SystemException("bad"));
        replay(tx);
        try {
            jut.begin();
        } catch (DoceException e) {
            assertEquals("bad", e.getCause().getMessage());
        }
    }

    @Test
    public void testCommit() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_ACTIVE);
        tx.commit();
        expectLastCall();
        replay(tx);
        jut.commit();
    }

    @Test
    public void testCommit_2() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_NO_TRANSACTION);
        replay(tx);
        jut.commit();
    }

    @Test
    public void testCommit_3() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_ACTIVE);
        tx.commit();
        expectLastCall().andThrow(new SystemException("bad"));
        replay(tx);
        try {
            jut.commit();
        } catch (DoceException e) {
            assertEquals("bad", e.getCause().getMessage());
        }
    }

    @Test
    public void testRollback() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_ACTIVE);
        tx.rollback();
        expectLastCall();
        replay(tx);
        jut.rollback();
    }

    @Test
    public void testRollback_2() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_NO_TRANSACTION);
        replay(tx);
        jut.rollback();
    }

    @Test
    public void testRollback_3() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_ACTIVE);
        tx.rollback();
        expectLastCall().andThrow(new SystemException("bad"));
        replay(tx);
        try {
            jut.rollback();
        } catch (DoceException e) {
            assertEquals("bad", e.getCause().getMessage());
        }
    }

    @Test
    public void testIsActive() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_ACTIVE);
        replay(tx);
        assertTrue(jut.isActive());
    }

    @Test
    public void testIsActive_2() throws Exception {
        expect(tx.getStatus()).andReturn(Status.STATUS_UNKNOWN);
        replay(tx);
        assertFalse(jut.isActive());
    }

    @Test
    public void testIsActive_3() throws Exception {
        expect(tx.getStatus()).andThrow(new SystemException("bad"));
        replay(tx);
        try {
            jut.isActive();
        } catch (DoceException e) {
            assertEquals("bad", e.getCause().getMessage());
        }
    }
}
