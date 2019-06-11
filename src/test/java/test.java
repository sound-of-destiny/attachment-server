import java.io.FileOutputStream;

public class test {
    public static void main(String[] args) {
        try (FileOutputStream fos= new FileOutputStream("00_65_6505_0_65fbf1c1891347fa8010e5f56f5497ac.jpg", true);
                /*DataOutputStream dos = new DataOutputStream(fos)*/) {
            fos.write("hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
