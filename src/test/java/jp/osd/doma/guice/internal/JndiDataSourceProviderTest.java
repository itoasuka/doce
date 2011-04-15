/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.osd.doma.guice.DomaGuiceException;

import org.junit.Test;

import com.google.inject.Provider;

/**
 * @author asuka
 */
public class JndiDataSourceProviderTest {

	/**
	 * {@link jp.osd.doma.guice.internal.JndiDataSourceProvider#get()} のためのテスト・メソッド。
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		Context ctx = createMock(Context.class);
		expect(ctx.lookup("test")).andThrow(new NamingException("test"));
		replay(ctx);
		Provider<DataSource> p = new JndiDataSourceProvider(ctx, "test");
		try {
			p.get();
		} catch (DomaGuiceException e) {
			assertEquals("test", e.getCause().getMessage());
		}
	}

}
