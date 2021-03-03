import com.dyh.dao.UserMapper;
import com.dyh.pojo.User;
import com.dyh.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;


public class MyTest {

    @Test
    public void test() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user1 = mapper.queryUserById(1);
        System.out.println("================================================================================");
        System.out.println(user1);
        //mapper.updateUser(new User(2,"aaaaa", "bbbbb"));

        //sqlSession.clearCache();
        // 手动清理缓存

        System.out.println("================================================================================");
        User user2 = mapper.queryUserById(1);
        System.out.println(user2);
        System.out.println("================================================================================");
        System.out.println(user1 == user2);
        System.out.println("================================================================================");

        sqlSession.close();
    }

    @Test
    public void test2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.queryUserById(1);
        System.out.println(user);
        sqlSession.close();

        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user2 = mapper2.queryUserById(1);
        System.out.println(user2);
        sqlSession2.close();
    }
}
