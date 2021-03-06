package jp.osd.doce.internal.provider;

import static org.junit.Assert.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.Scopes;

public class UserTransactionProviderTest {

	@Test
	public void testGet() {
		Module m = new AbstractModule() {

			@Override
			protected void configure() {
				try {
					bind(UserTransaction.class).toProvider(
							new UserTransactionProvider(null, new InitialContext()))
							.in(Scopes.SINGLETON);
				} catch (NamingException e) {
					throw new RuntimeException(e);
				}
			}
		};
		try {
			Guice.createInjector(m).getInstance(UserTransaction.class);
			fail();
		} catch (ProvisionException e) {
			assertEquals("Lookup error : java:comp/UserTransaction", e
					.getCause().getMessage());
		}
	}

}
