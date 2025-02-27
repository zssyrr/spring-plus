import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郑永帅
 * @since 2024/12/25
 */

public class QuestionEighteen {
    public static void main(String[] args) {
        int[] nums = new int[]{5,5,3,5,1,-5,1,-2};
        List<List<Integer>> result = new ArrayList<>();
        QuestionEighteen questionEighteen = new QuestionEighteen();

        questionEighteen.findNsum (nums, 4, 4, new ArrayList<>(),result);
//        result = result.stream().map(list->list.stream().sorted().collect(Collectors.toList())).collect(Collectors.toList());
//        result = result.stream().distinct().collect(Collectors.toList());
        System.out.println(result);
    }
    private void fork(int[] nums, int target, int N, List<Integer> tempList, List<List<Integer>> result) {
        if (nums.length < 2 || N < 2) {
            return;
        }
        if (N == 2) {
            List<Integer> tmp = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                int diff = target - nums[i];
                if (tmp.contains(diff)) {
                    List<Integer> bufList = new ArrayList<>(tempList);
                    bufList.add(nums[i]);
                    bufList.add(diff);
                    result.add(bufList);
                    if (i != nums.length -1 && nums[i] == nums[i + 1]) {
                        i++;
                    }
                } else {
                    tmp.add(nums[i]);
                }
            }
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (i == 0 || nums[i] != nums[i - 1]) {
                    int[] subNums = new int[nums.length - i - 1];
                    for (int j = i + 1; j < nums.length; j++) {
                        subNums[j - i - 1] = nums[j];
                    }
                    List<Integer> bufList = new ArrayList<>(tempList);
                    bufList.add(nums[i]);
                    long tempTarget = target;
                    if (tempTarget - nums[i] > Integer.MAX_VALUE || tempTarget - nums[i] < Integer.MIN_VALUE) {
                        continue;
                    }
                    fork(subNums, target - nums[i], N - 1, bufList, result);
                }
            }
        }
    }

    void findNsum(int[] nums, int target, int N,
                  List<Integer> tempList,
                  List<List<Integer> > results) {
        if (nums.length < N || N < 2) return;
        if (N == 2) {
            int l = 0, r = nums.length - 1;
            Arrays.sort(nums);
            while (l < r) {
                if (nums[l] + nums[r] == target) {
                    List<Integer> bufList = new ArrayList<>();
                    for (int ww = 0; ww < tempList.size(); ww++)
                        bufList.add(tempList.get(ww));
                    bufList.add(nums[l]);
                    bufList.add(nums[r]);
                    results.add(bufList);
                    l += 1;
                    r -= 1;
                    while (l < r && nums[l] == nums[l - 1])
                        l += 1;
                    while (r > l && nums[r] == nums[r + 1])
                        r -= 1;
                } else if (nums[l] + nums[r] < target) l += 1;
                else r -= 1;
            }
        } else {
            for (int i = 0; i < nums.length; i++)
                if (i == 0 || i > 0 && nums[i - 1] != nums[i]) {
                    int[] buf_nums = new int[nums.length - i - 1];
                    for (int j = i + 1; j < nums.length; j++)
                        buf_nums[j - i - 1] = nums[j];
                    List<Integer> bufList = new ArrayList<>();
                    for (int ww = 0; ww < tempList.size(); ww++)
                        bufList.add(tempList.get(ww));
                    bufList.add(nums[i]);
                    findNsum(buf_nums, target - nums[i], N - 1,
                            bufList, results);
                }
        }
    }
}
