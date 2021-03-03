# Mybatis

## 1. Mybatis项目创建流程（查询为例子）：

### 环境搭建

​	现有一个数据表 mybatis/user

```html
id  name  pwd
1    A    123
2    B    111
3    C    456
```



1. 创建一个maven工程，将src文件夹删除，将该工程作为parent，创建一个新的module

2. 在parent工程中编写pom.xml，引入依赖：

   1. mysql
   2. mybatis
   3. junit

   此时子modul继承了parent的pom.xml，不用重新加入依赖

   ```xml
   <dependencies>
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>5.1.47</version>
           </dependency>
   
           <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
           <dependency>
               <groupId>org.mybatis</groupId>
               <artifactId>mybatis</artifactId>
               <version>3.5.2</version>
           </dependency>
   
   
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.11</version>
               <scope>test</scope>
           </dependency>
       </dependencies>
   ```

   

3. 在两个pom.xml中都加入资源过滤：

      ```xml
   <build>
           <resources>
               <resource>
                   <directory>src/main/resources</directory>
                   <includes>
                       <include>**/*.properties</include>
                       <include>**/*.xml</include>
                   </includes>
   
               </resource>
               <resource>
                   <directory>src/main/java</directory>
                   <includes>
                       <include>**/*.properties</include>
                       <include>**/*.xml</include>
                   </includes>
   
               </resource>
           </resources>
   
   </build>
   ```

### MybatisUtils.java（/src/main/java/com.dyh/utils）

```java
package com.dyh.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//sqlSessionFactory
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            //使用mybatis第一步
            // 从 XML 中构建 SqlSessionFactory
						// 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。
            // SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。
            // 而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。
            String source = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(source);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

}
```

### 设置mybatis-config.xml (/resource)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;userUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/dyh/dao/UserMapper.xml"/>
    </mappers>
</configuration>
```



### 根据数据表创建 User.java(/main/java/com.dyh/pojo)

私有变量

无参有参

Set & get

toString

```java
package com.dyh.pojo;

public class User {
    private int id;
    private String name;
    private String pwd;

    public User(){

    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
```



### 构建UserDao.java interface (mapper) (/main/java/com.dyh/dao)

```java
package com.dyh.dao;
import com.dyh.pojo.User;
import java.util.List;

public interface UserDao{
  List<User> getUserList();
}
```



### 构建UserMapper.xml  (/main/java/com.dyh/dao)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace綁定一個Dao/mapper接口-->
<mapper namespace="com.dyh.dao.UserDao">
    <select id="getUserList" resultType="com.dyh.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
```



### 测试test/java/com.dyh/dao/UserDaoTest.java

```java
package com.dyh.dao;

import com.dyh.pojo.User;
import com.dyh.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {
    @Test
    public void test() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

      //从 SqlSessionFactory 中获取 SqlSession
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> userList = userDao.getUserList();


        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }
}
```



## 2. 增删改查

1. 在dao/mapper层中的UserMapper.java中加入方法（e.g int addUser(User user)）
2. 配置dao/UserMapper.xml
3. 测试接口，**增删改查需要提交事务**

### Select

选择，查询语句:

- id：对应namespace中方法名
- resultType：Sql语句执行的返回值
- parameterType：参数类型

1. 在dao/mapper层中的UserMapper.java中加入方法

   ```java
    User getUerById(int id);
   ```

2. 配置dao/UserMapper.xml

   ```xml
       <select id="getUserList" resultType="com.dyh.pojo.User">
           select * from mybatis.user
       </select>
   
       <select id="getUerById" parameterType="int" resultType="com.dyh.pojo.User">
           select * from mybatis.user where id=#{id}
       </select>
   ```

3. 测试接口

   ```java
   @Test
       public void getUserById() {
           SqlSession sqlSession = MybatisUtils.getSqlSession();
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           User user = mapper.getUerById(1);
           System.out.println(user);
   
           sqlSession.close();
       }
   ```

### Insert

1. 在dao/mapper层中的UserMapper.java中加入方法

   ```java
   int addUser(User user);
   ```

2. 配置dao/UserMapper.xml

   ```xml
       <insert id="addUser" parameterType="com.dyh.pojo.User">
           insert into mybatis.user (id, name, pwd) values (#{id}, #{name}, #{pwd})
       </insert>
   ```

   

3. 测试接口 **增删改查需要提交事务**

   ```java
   @Test
       public void addUser() {
           SqlSession sqlSession = MybatisUtils.getSqlSession();
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           int res = mapper.addUser(new User(4, "H", "333"));
   
           //commit transaction
           sqlSession.commit();
           sqlSession.close();
       }
   ```

### Update

1. 在dao/mapper层中的UserMapper.java中加入方法

   ```java
   int updateUser(User user);
   ```

2. 配置dao/UserMapper.xml

   ```xml
       <update id="updateUser" parameterType="com.dyh.pojo.User">
           update mybatis.user set name=#{name},pwd=#{pwd} where id=#{id}
       </update>
   ```

3. 测试接口 **增删改查需要提交事务**

   ```java
       @Test
       public void updateUser() {
           SqlSession sqlSession = MybatisUtils.getSqlSession();
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           int res = mapper.updateUser(new User(4, "D", "234"));
   
           //commit transaction
           sqlSession.commit();
           sqlSession.close();
       }
   ```

### Delete

1. 在dao/mapper层中的UserMapper.java中加入方法

   ```java
   int deleteUser(int id);
   ```

2. 配置dao/UserMapper.xml

   ```xml
       <delete id="deleteUser" parameterType="int">
           delete from mybatis.user where id=#{id}
       </delete>
   ```

3. 测试接口 **增删改查需要提交事务**

   ```java
       @Test
       public void deleteUser() {
           SqlSession sqlSession = MybatisUtils.getSqlSession();
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           int res = mapper.deleteUser(4);
   
           //commit transaction
           sqlSession.commit();
           sqlSession.close();
       }
   ```

### 模糊查询

1. dao/UserMapper.java

   ```java
   List<User> getUserLike(String value);
   ```

2. UserMapper.xml

   ```xml
   <select id="getUserLike" resultType="com.dyh.pojo.User">
           select * from mybatis.user where name like #{value}
       </select>
   ```

3. test

   ```java
   @Test
       public void getUserLike() {
           SqlSession sqlSession = MybatisUtils.getSqlSession();
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           List<User> userLike = mapper.getUserLike("%A%");
           for (User user : userLike) {
               System.out.println(user);
           }
   
           sqlSession.close();
       }
   ```




### 结果集映射（属性和字段名字不一致）

```xml
<!--結果集映射-->
    <resultMap id="UserMap" type="User">
<!--        column数据库中的字段 property实体类中的属性-->
<!--        <result column="id" property="id"/>-->
<!--        <result column="name" property="name"/>-->
        <result column="pwd" property="password"/>
    </resultMap>
```



## 3. 作用域（Scope）和生命周期

理解我们之前讨论过的不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的并发问题。

------

### SqlSessionFactoryBuilder

这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。

### SqlSessionFactory

SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。

### SqlSession

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它



## 4. Mybatis 注解开发

### 编写接口：

```java
public interface UserMapper {
    @Select("select * from user")
    List<User> getUser();
}
```

### 在mybatis-config.xml中绑定接口:

  ```xml
<!--    绑定接口-->
<mappers>
    <mapper class="com.dyh.dao.UserMapper"/>
</mappers>
  ```

### 注解CRUD

在工具类创建的时候自动提交事务

```java
    public static SqlSession getSqlSession() {
        //自动commit
        return sqlSessionFactory.openSession(true);
    }
```

UserMapper.java

```java
    //方法多个参数 参数前都加@Param
    @Select("select * from user where id = #{id}")
    User getUserByID(@Param("id") int id);

    @Insert("insert into user(id,name,pwd) values (#{id},#{name},#{password})")
    int addUser(User user);

    @Update("update user set name=#{name},pwd=#{password} where id=#{id}")
    int updateUser(User user);

    @Delete("delete from user where id=#{uid}")
    int deleteUser(@Param("uid") int id);
```





## 5. lombok

```xml
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
    </dependencies>
```



```java
@Data: 无参构造，get，set，tostring，hashcode，equals

@AllArgsConstructor:有参构造

@NoArgsConstructor：无参构造
```



## 6. 多对一关系处理

mysql: 嵌套查询/子查询

### 测试环境搭建：

1. 导入lombok
2. 新建实体类（Teacher，Student）
3. 建立mapper接口
4. 建立mapper.xml
5. 核心配置文件中绑定mapper接口或文件
6. 测试查询是否成功



### 按照查询嵌套处理：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dyh.dao.StudentMapper">
    
    <!--思路：
        1. 查询所有的学生信息
        2. 根据查询出来的学生的tid，寻找对应的老师 子查询
    -->
    <select id="getStudent" resultMap="StudentTeacher">
        select * from mybatis.student;
    </select>

    <resultMap id="StudentTeacher" type="Student">
<!--        复杂的属性，需要单独处理，对象：association 集合：collection-->
        <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
    </resultMap>

    <select id="getTeacher" resultType="Teacher">
        select * from mybatis.teacher where id = #{id};
    </select>
</mapper>
```



### 按照结果嵌套处理（推荐）：

```xml
    <!--按照结果嵌套处理-->
    <select id="getStudent2" resultMap="StudentTeacher2">
        select s.id sid,s.name sname,t.name tname
        from mybatis.student s, mybatis.teacher t
        where s.tid = t.id;
    </select>


    <resultMap id="StudentTeacher2" type="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <association property="teacher" javaType="Teacher">
        <!--老师表里的字段映射出的结果-->
            <result property="name" column="tname"/>
        </association>
    </resultMap>
```



## 7.  一对多关系

e.g: 一个老师有多个学生

实体类：

Student.java

```java
package com.dyh.pojo;

import lombok.Data;

@Data
public class Student {
    private int id;
    private String name;
    private int tid;
}
```

Teacher.java

```java
package com.dyh.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}

```

### 按照查询嵌套处理：

```xml
    <select id="getTeacher2" resultMap="TeacherStudent2">
        select *
        from mybatis.teacher where id=#{id};
    </select>
    <resultMap id="TeacherStudent2" type="Teacher">
        <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentByTeacherID" column="id"/>
    </resultMap>
    
    <select id="getStudentByTeacherID" resultType="Student">
        select *
        from mybatis.student where tid=#{tid};
    </select>
```



### 按照结果嵌套处理（推荐）：

```xml
    <select id="getTeacher" resultMap="TeacherStudent">
        select s.id sid, s.name sname, t.name tname, t.id tid
        from mybatis.student s, mybatis.teacher t
        where s.tid = t.id and t.id = #{tid};
    </select>
    <resultMap id="TeacherStudent" type="Teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
        <collection property="students" ofType="Student">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="tid"/>
        </collection>
    </resultMap>
```



## 8. 动态sql

#### if

BlogMapper.java

```xml
List<Blog> queryBlogsIF(Map map);
    //map负责接收参数
```



BlogMapper.xml

```xml
    <select id="queryBlogsIF" parameterType="map"
            resultType="blog">
        SELECT * FROM mybatis.blog
        <where>
            <if test="title != null">
                title = #{title}
            </if>
            <if test="author != null">
                AND author = #{author}
            </if>
        </where>

    </select>
```

Test.java

```java
@Test
    public void queryBlogsIF() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();

        //map.put("title", "Java");
        map.put("author", "dyh"); //param 
        //key: sql语句中的变量 val：传入的参数值
        List<Blog> blogs = mapper.queryBlogsIF(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }

        sqlSession.close();
    }
```

拼接后sql：

```mysql
==>  Preparing: SELECT * FROM mybatis.blog WHERE author = ? 
==> Parameters: dyh(String)
```



#### choose when

类似java 的 switch 语句，执行到满足一个跳出

```java
List<Blog> queryBlogsChoose(Map map);
```

```xml
    <select id="queryBlogsChoose" parameterType="map" resultType="blog">
        SELECT * FROM mybatis.blog
        <where>
            <choose>
                <when test="title != null">
                    title = #{title}
                </when>
                <when test="author != null">
                    author = #{author}
                </when>
                <otherwise>
                    and views = #{views}
                </otherwise>
            </choose>
        </where>

    </select>
```

```java
    @Test
    public void queryBlogsChoose() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();
        map.put("views", 9999);
        List<Blog> blogs = mapper.queryBlogsChoose(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
```

```mysql
==>  Preparing: SELECT * FROM mybatis.blog WHERE views = ? 
==> Parameters: 9999(Integer)
```



#### set

可用于update

```java
int UpdateBlog(Map map);
```

```xml
   <update id="UpdateBlog" parameterType="map">
        update mybatis.blog
        <set>
            <if test="title != null">
                title = #{title},
            </if>
            <if test="author != null">
                author = #{author},
            </if>
            <if test="views != null">
                views = #{views}
            </if>
        </set>
        where id = #{id}
    </update>
```

```java
    @Test
    public void updateBlogs() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();

        map.put("title", "Java");
        map.put("author", "aaa");
        map.put("views", 1000);
        map.put("id", "e054fc59538c4788a1f0c27b8046bbdf");
        mapper.UpdateBlog(map);
        sqlSession.close();
    }
```

```mysql
==>  Preparing: update mybatis.blog SET title = ?, author = ?, views = ? where id = ? 
==> Parameters: Java(String), aaa(String), 1000(Integer), e054fc59538c4788a1f0c27b8046bbdf(String)
```

#### sql标签

```xml
    //sql标签抽查公共部分，在需要使用的地方include即可
    <sql id="query-title-author">
        <if test="title != null">
            title = #{title}
        </if>
        <if test="author != null">
            AND author = #{author}
        </if>
    </sql>

    <select id="queryBlogsIF" parameterType="map"
            resultType="blog">
        SELECT * FROM mybatis.blog
        <where>
            <include refid="query-title-author"></include>
        </where>

    </select>
```

#### ForEach

```java
List<Blog> queryBlogForEach(Map map);
```

```xml
<!--    select * from mybatis.blog where 1=1 and (id=1 or id=2 or id=3)-->
    <select id="queryBlogForEach" parameterType="map" resultType="blog">
        select * from mybatis.blog
        <where>
            <foreach collection="ids" item="id" open="and (" separator="or" close=")">
                id = #{id}
            </foreach>
        </where>
    </select>
```

```java
    @Test
    public void queryBlogForEach() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("be97768b15204849a98556f1be6c8f32");
        ids.add("050d9d4fbcb8493ead78f8b983eaa081");
        map.put("ids",ids);
        List<Blog> blogs = mapper.queryBlogForEach(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
```



## 9. 缓存

### 一级缓存

- 本地缓存：SqlSession级别
  - 与数据库同一次会话期间查询到的数据会放在本地缓存中
  - 以后如果需要获取相同的数据，直接从缓存中拿，没有必要再去查询数据库

### 二级缓存

- 全局缓存，基于namespace级别，一个namespace对应一个二级缓存

- 工作机制

  - 一个会话查询一条数据，这条数据会被放在以及缓存中
  - 如果当前会话关闭，这个会话对应的一级缓存就没了，但是我们想要的是，会话关闭了，一级缓存中的数据保存到二级缓存中
  - 新的会话查询信息，就可以从二级缓存中获取内容
  - 不同的mapper查出的数据会放在自己对应的缓存（map）中

- 步骤：

  - 开启全局缓存

    ```xml
    <setting name="cacheEnabled" value="true"/>
    ```

  - 在当前mapper.xml中使用二级缓存

    - 将实体类序列化：

      ```java
      //org.apache.ibatis.cache.CacheException: Error serializing object.  Cause: java.io.NotSerializableException: com.dyh.pojo.User
      
      package com.dyh.pojo;
      
      import lombok.AllArgsConstructor;
      import lombok.Data;
      import lombok.NoArgsConstructor;
      
      import java.io.Serializable;
      
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class User implements Serializable {
          private int id;
          private String name;
          private String pwd;
      
      
      }
      ```

      

    ```xml
    <cache/>
    ```

    或者自定义参数

    ```xml
    <cache
      eviction="FIFO"
      flushInterval="60000"
      size="512"
      readOnly="true"/>
    ```

    


## 10. 自定义缓存 Ehcache

### 导入依赖：pom.xml

```xml
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.1.0</version>
        </dependency>
```

### mapper下配置：

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

### resource下创建ehcache.xml:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dyh.dao.UserMapper">

    <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>

    <select id="queryUserById" resultType="user">
        select * from mybatis.user where id=#{id}
    </select>
    
    <update id="updateUser" parameterType="user">
        update mybatis.user set name=#{name}, pwd=#{pwd} where id = #{id};
    </update>
</mapper>
```

