package test.service;

import jp.osd.doce.Transactional;
import test.dao.FooDao;
import test.entity.Foo;

import com.google.inject.Inject;

public class Test2ServiceImpl implements Test2Service {
	@Inject
	private FooDao fooDao;

	@Override
	@Transactional({"", "test"})
	public void test() {
		fooDao.dropTable();
		fooDao.createTable();
		Foo foo = new Foo();
		foo.id = 1;
		foo.name = "Mike";
		test2(foo);
	}

	@Override
	@Transactional
	public void test2(Foo foo) {
		fooDao.insert(foo);
	}

	@Override
	@Transactional
	public void throwException() {
		test();
		throw new RuntimeException("This is a test!");
	}

	@Override
	@Transactional({"", "test"})
	public Foo get(int id) {
		return fooDao.findById(id);
	}

	@Override
	@Transactional
	public void dispose() {
		fooDao.dropTable();
	}

}
