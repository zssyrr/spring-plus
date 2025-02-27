import java.util.Arrays;

/**
 * @author 郑永帅
 * @since 2024/12/28
 */

public class QuestionSixteen {
    public static void main(String[] args) {
        QuestionSixteen questionSixteen = new QuestionSixteen();
        System.out.println(questionSixteen.targetThreeSum(new int[]{-84,92,26,19,-7,9,42,-51,8,30,-100,-13,-38}, 78));
    }

    private int targetThreeSum(int[] nums, int target) {
        Arrays.sort(nums);
        int result = target;
        int diff = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i-1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == target) {
                    return sum;
                }
                int abs = Math.abs(sum - target);
                if (abs < diff) {
                    diff = abs;
                    result = sum;
                }
                if (sum > target) {
                    right--;
                } else {
                    left++;
                }
            }
        }
        return result;
    }
}
