import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashclass {
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger num = new BigInteger(1, messageDigest);//to kanei thetiko to 1
            return num.toString();
        } catch (NoSuchAlgorithmException e) {
            return "Error!";
        }
    }
    public static String hashString(String s){
        BigInteger hash ;

        hash = new BigInteger(getMD5(s));
        hash = hash.mod(BigInteger.valueOf(3));
        //System.out.println(hash);

        return String.valueOf(hash);

    }
}
