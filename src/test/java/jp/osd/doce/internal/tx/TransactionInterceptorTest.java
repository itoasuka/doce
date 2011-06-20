package jp.osd.doce.internal.tx;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

import java.util.Properties;

import jp.osd.doce.DoceModule;

import org.junit.Test;
import org.seasar.doma.jdbc.dialect.H2Dialect;

import test.dao.HogeDao;
import test.entity.Hoge;
import test.service.TestService;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class TransactionInterceptorTest {
    private final Injector injector;

    public TransactionInterceptorTest() {
        final Properties domaProperties = new Properties();
        domaProperties.setProperty("JDBC.url",
                "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1");
        domaProperties.setProperty("JDBC.username", "foo");
        domaProperties.setProperty("JDBC.password", "bar");
        domaProperties.setProperty("Doma.dialect", H2Dialect.class.getName());
        injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                Names.bindProperties(binder, domaProperties);
            }
        }, new DoceModule.Builder().addDaoTypes(HogeDao.class).create());
    }

    @Test
    public void testInvoke() {
        TestService service = injector.getInstance(TestService.class);
        service.test();
        Hoge hoge = service.get(1);
        assertEquals("Mike", hoge.name);
        service.dispose();
        try {
            service.throwException();
            fail();
        } catch (RuntimeException e) {
            // OK
        }
        hoge = service.get(1);
        assertNull(hoge);
    }
}
