package test.service;

import test.entity.Hoge;

import com.google.inject.ImplementedBy;

@ImplementedBy(TestServiceImpl.class)
public interface TestService {
	void test();
	
	void test2(Hoge hoge);
	
	void throwException();
	
	Hoge get(int id);
	
	void dispose();
}
