package test.service;

import jp.osd.doce.Transactional;
import test.dao.HogeDao;
import test.entity.Hoge;

import com.google.inject.Inject;

public class TestServiceImpl implements TestService {
	@Inject
	private HogeDao hogeDao;

	@Override
	@Transactional
	public void test() {
		hogeDao.createTable();
		Hoge hoge = new Hoge();
		hoge.id = 1;
		hoge.name = "Mike";
		test2(hoge);
	}

	@Override
	@Transactional
	public void test2(Hoge hoge) {
		hogeDao.insert(hoge);
	}

	@Override
	@Transactional
	public void throwException() {
		test();
		throw new RuntimeException("This is a test!");
	}

	@Override
	@Transactional
	public Hoge get(int id) {
		return hogeDao.findById(id);
	}

	@Override
	@Transactional
	public void dispose() {
		hogeDao.dropTable();
	}
}
