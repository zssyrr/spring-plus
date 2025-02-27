import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author 郑永帅
 * @since 2024/12/30
 */

public class Question179 {
    public static void main(String[] args) {
        Question179 question179 = new Question179();
        System.out.println(question179.maxSeries(new int[]{3, 30, 34, 5, 9}));
    }

    private String maxSeries(int[] nums) {
        if (Arrays.stream(nums).allMatch(num -> num == 0)) {
            return "0";
        }
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 第一种方式
//                String left = o1 + o2;
//                String right = o2 + o1;
//                return -left.compareTo(right);
                // 第二种方式
                int len1 = o1.length();
                int len2 = o2.length();
                int lim = Math.min(len1, len2);
                char[] v1 = o1.toCharArray();
                char[] v2 = o2.toCharArray();

                int k = 0;
                while (k < lim) {
                    char c1 = v1[k];
                    char c2 = v2[k];
                    if (c1 != c2) {
                        return c2 - c1;
                    }
                    k++;
                }
                if (len1 > len2){
                    return compare(o1.substring(len2), o2);
                } else if (len1 < len2) {
                    return compare(o1,o2.substring(len1));
                } else {
                    return 0;
                }
            }
        };
        return Arrays.stream(nums).mapToObj(String::valueOf)
                .sorted(comparator)
                .collect(Collectors.joining());
    }
}
