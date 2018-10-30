package JU4C;

public class Demo {
    private String name ="laowang";
    private int age;

    public Demo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String speak(String str) {
        return str;
    }
    public String talk(String str)
    {
        return str;
    }
    public String methodNoParameters()
    {
        return name;
    }

    public String methodCustomParameters(ParameterClass parameter, String str)
    {
        return str;
    }

    public String methodHaveChildObj(ParameterClass parameter, String str)
    {
        parameter.childTalk(str);
        return str;

    }

}
