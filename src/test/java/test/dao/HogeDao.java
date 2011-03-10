package test.dao;

import jp.osd.doma.guice.InjectConfig;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;

import test.entity.Hoge;

@Dao
@InjectConfig
public interface HogeDao {
	@Script
	void createTable();
	
	@Insert
	int insert(Hoge hoge);
	
	@Select
	Hoge findById(int id);
	
	@Script
	void dropTable();
}
