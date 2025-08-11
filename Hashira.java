import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Hashira {
    public static void main(String[] args) {
        try {
            String fileName = "input.json"; // change for second test case

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int n = 0, k = 0;
            List<Point> points = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Read n
                if (line.startsWith("\"n\"")) {
                    n = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                }
                // Read k
                else if (line.startsWith("\"k\"")) {
                    k = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                }
                // Read a root (x, base, value)
                else if (line.matches("^\"\\d+\".*\\{")) {
                    int x = Integer.parseInt(line.substring(1, line.indexOf("\"", 1)));

                    String baseLine = br.readLine().trim();
                    String valueLine = br.readLine().trim();

                    int base = Integer.parseInt(baseLine.split(":")[1].replace(",", "").replace("\"", "").trim());
                    String value = valueLine.split(":")[1].replace("\"", "").replace(",", "").trim();

                    // Decode y value from given base
                    BigInteger yDecoded = new BigInteger(value, base);

                    points.add(new Point(x, yDecoded));

                    // Skip closing brace
                    br.readLine();
                }
            }
            br.close();

            // Sort by x
            points.sort(Comparator.comparingInt(p -> p.x));

            // Only take first k points
            List<Point> selected = points.subList(0, k);

            // Calculate constant term (c)
            BigInteger c = lagrangeConstant(selected);

            System.out.println("Constant term (c) = " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lagrange interpolation to find P(0)
    static BigInteger lagrangeConstant(List<Point> pts) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < pts.size(); i++) {
            BigInteger term = pts.get(i).y;

            for (int j = 0; j < pts.size(); j++) {
                if (i != j) {
                    BigInteger num = BigInteger.ZERO.subtract(BigInteger.valueOf(pts.get(j).x));
                    BigInteger den = BigInteger.valueOf(pts.get(i).x - pts.get(j).x);
                    term = term.multiply(num).divide(den);
                }
            }
            result = result.add(term);
        }
        return result;
    }
}


