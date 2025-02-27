import java.util.*;

/**
 * @author 郑永帅
 * @since 2024/12/26
 */

public class Question454 {

    public static void main(String[] args) {
        int[] nums1 = new int[]{0,1,-1};
        int[] nums2 = new int[]{-1,1,0};
        int[] nums3 = new int[]{0,0,1};
        int[] nums4 = new int[]{-1,1,1};
        Question454 question454 = new Question454();
        System.out.println(question454.fork(nums1, nums2, nums3, nums4));
    }

    public int fork(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
//        List<Integer> tempList = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : nums1) {
            for (int i1 : nums2) {
//                tempList.add(i + i1);
                if (map.containsKey(i + i1)) {
                    map.put(i + i1, map.get(-i - i1) + 1);
                } else {
                    map.put(i + i1, 1);
                }
            }
        }
        int count = 0;
        for (int i : nums3) {
            for (int i1 : nums4) {
//                if (tempList.contains(-1*(i+i1))) {
//                    count++;
//                }
                if (map.containsKey(-1*(i+i1))) {
                    count += map.get(-1*(i+i1));
                }
            }
        }
//`        for (Integer i : tempList) {
//            List<Integer> diffList = new ArrayList<>();
//            for (int i1 : nums3) {
//                diffList.add(0-i-i1);
//            }
//            for (int i1 : nums4) {
//                if (diffList.contains(i1)) {
//                    count++;
//                }
//            }
//        }`
        return count;
    }
}
