import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郑永帅
 * @since 2025/1/8
 */

public class Question166 {

    public static void main(String[] args) {
        Question166 question166 = new Question166();
        System.out.println(question166.maxRepeating(1, 1000));
        System.out.println(question166.fractionToDecimal(1, 1000));
    }

    public String fractionToDecimal(long numerator,
                                    long denominator) {
        String res = "";
        if ((double)numerator / (double)denominator < 0.0) res = res + "-";
        numerator = Math.abs(numerator);
        denominator = Math.abs(denominator);
        long n = numerator / denominator;
        long remainder = numerator % denominator;
        res = res + String.valueOf(n);
        if (remainder == 0) return res;
        res = res + ".";
        int idx = res.length();
        Map<Long, Integer> seen = new HashMap<Long, Integer>();
        while (remainder != 0 && seen.get(remainder) == null) {
            seen.put(remainder, idx);
            idx = idx + 1;
            n = (remainder * 10) / denominator;
            remainder = (remainder * 10) % denominator;
            res = res + String.valueOf(n);
        }
        if (remainder != 0) {
            StringBuffer ano_str =  new StringBuffer(res);
            ano_str.insert(seen.get(remainder), "(");
//            ano_str.append("(");
            res = ano_str.toString();
            res += ")";
        }
        return res;
    }

    public String maxRepeating(int a, int b) {
        if (b == 0) {
            return "";
        }
        if (a % b == 0) {
            return a / b + "";
        }
        StringBuilder prefix = new StringBuilder(a / b < 0 ? "-" : "");
        a = Math.abs(a);
        b = Math.abs(b);
        prefix.append(a / b).append(".");


        Long c = (long) (a % b);
        Map<Long, Integer> map = new HashMap<>();
        int idx = prefix.length();
        while (c != 0 && !map.containsKey(c)) {
            prefix.append(c * 10 / b);
            map.put(c, idx);
            idx++;
            c = c * 10 % b;
        }

        StringBuilder builder = new StringBuilder(prefix);
        if (c != 0) {
            builder.insert(map.get(c), "(");
            builder.append(")");
        }
        return builder.toString();



//        List<Integer> list1 = getList(list, a, b);
//        if (list1.isEmpty()) {
//            return prefix + list.stream().map(String::valueOf).collect(Collectors.joining(""));
//        } else {
//            String suffix = "(" + list1.stream().map(String::valueOf).collect(Collectors.joining("")) + ")";
//            list.removeAll(list1);
//            return prefix + list.stream().map(String::valueOf).collect(Collectors.joining("")) + suffix;
//        }
    }

    public List<Integer> getList(List<Integer> list, int a, int b) {
        if (a % b == 0) {
            return new ArrayList<>();
        }
        int c = a % b;
        if (list.contains(c)) {
            return list.subList(list.indexOf(c), list.size());
        } else {
            list.add(c);
        }
        return getList(list, c * 10, b);
    }
}
