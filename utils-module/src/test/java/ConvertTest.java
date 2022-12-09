import com.horkr.util.convert.TypeConvertor;

import java.util.Map;

/**
 * @author 卢亮宏
 */
public class ConvertTest {
    public static void main(String[] args) {
        TypeConvertor instance = TypeConvertor.instance;
    }

    static class Girl{
        private String name;

        private int age;

        private Map<String,Object> innerMap;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Map<String, Object> getInnerMap() {
            return innerMap;
        }

        public void setInnerMap(Map<String, Object> innerMap) {
            this.innerMap = innerMap;
        }
    }


    static class GirlDto{
        private String name1;

        private int age1;

        private String cupSize;

        private boolean open;
    }
}
