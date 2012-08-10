package test.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;

import test.entity.Foo;

@Dao
@InjectTestConfig
public interface FooDao {
    @Script
    void createTable();

    @Insert
    int insert(Foo foo);

    @Select
    Foo findById(int id);

    @Script
    void dropTable();
}
