package test.service;

import com.google.inject.ImplementedBy;

import test.entity.Foo;

@ImplementedBy(Test2ServiceImpl.class)
public interface Test2Service {
	void test();
	
	void test2(Foo foo);
	
	void throwException();
	
	Foo get(int id);
	
	void dispose();
}
