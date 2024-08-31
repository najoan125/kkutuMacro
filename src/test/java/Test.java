public class Test {
    public static void main(String[] args) {
        String start = "역(력)";
        System.out.println(start.split("\\(")[0]);
        System.out.println(start.split("\\(")[1].replace(")", ""));
    }
}
