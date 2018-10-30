package test;

import java.util.*;

public class HashMapTest {
    public static void main (String[] args){
        HashMap<Integer,User>users = new HashMap<Integer, User>();
        users.put(1,new User("张三",25));
        users.put(2,new User("王五",28));
        users.put(3,new User("李四",22));
        Set<Map.Entry<Integer, User>> entries = users.entrySet();
        Set<String>set = new HashSet<String>();


    }
    public static  HashMap<Integer,User> sortHashMap(HashMap<Integer,User> map){
        Set<Map.Entry<Integer, User>> entrySet = map.entrySet();
        System.out.println(entrySet);
        ArrayList<Map.Entry<Integer, User>> list = new ArrayList<Map.Entry<Integer, User>>(entrySet);
        Collections.sort(list, new Comparator<Map.Entry<Integer, User>>() {
            public int compare(Map.Entry<Integer, User> o1, Map.Entry<Integer, User> o2) {
                return o2.getValue().getAge()-o1.getValue().getAge();
            }
        });
        LinkedHashMap<Integer,User>linkedHashMap = new LinkedHashMap<Integer, User>();
        for(Map.Entry<Integer,User>entry:list){
            linkedHashMap.put(entry.getKey(),entry.getValue());

        }
        return linkedHashMap;
    }

}
