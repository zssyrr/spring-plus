/**
 * @author 郑永帅
 * @since 2024/12/29
 */

public class QuestionFiftyThree {
    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int len = 1;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < nums.length; j++) {
                int sum = nums[j];
                int k = j;
                while ((k = k + 1) < len) {
                    sum += nums[k];
                }
                if (sum > max) {
                    max = sum;
                }
            }
            if (len < nums.length) {
                len++;
            }
        }
        return max;
    }

    public int maxSubArray2(int[] nums) {
        int max = nums[0];
        for (int i = 0; i < nums.length; i++) {
            int total = 0;
            for (int j = i; j < nums.length; j++) {
                total += nums[j];
                if (total >= max) {
                    max = total;
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        QuestionFiftyThree questionFiftyThree = new QuestionFiftyThree();
        int[] nums = new int[]{1, -2, 3};
//        int fork = questionFiftyThree.fork(nums, 0, nums.length - 1);
//        int fork1 = questionFiftyThree.helper(nums, 0, nums.length - 1);
        int result = questionFiftyThree.maxSubArray1(nums);
        System.out.println(result);
    }

    public int fork(int[] nums, int left, int right) {
        if (left > right) {
            return Integer.MIN_VALUE;
        }
        int mid = (left + right) / 2;
        int leftMax = fork(nums, left, mid - 1);
        int rightMax = fork(nums, mid + 1, right);
        int midLeftMax = 0;
        int total = 0;
        for (int i = mid - 1; i >= 0; i--) {
            total += nums[i];
            if (total > midLeftMax) {
                midLeftMax = total;
            }
        }
        total = 0;
        int midRightMax = 0;
        for (int i = mid + 2; i < nums.length; i++) {
            total += nums[i];
            if (total > midRightMax) {
                midRightMax = total;
            }
        }
        int cross_max_sum = midLeftMax
                + midRightMax
                + nums[mid];
        return Math.max(cross_max_sum, Math.max(leftMax, rightMax));
    }

    public int helper(int[] nums, int l, int r) {
        if (l > r) return -0x7fffffff;
        int mid = (l + r) / 2;
        int left = helper(nums, l, mid - 1);
        int right = helper(nums, mid + 1, r);
        int left_suffix_max_sum = 0;
        int right_prefix_max_sum = 0;
        int total = 0;
        for (int i = mid - 1; i >= l; i--) {
            total += nums[i];
            left_suffix_max_sum = Math.max(left_suffix_max_sum,
                    total);
        }
        total = 0;
        for (int i = mid + 1; i < r + 1; i++) {
            total += nums[i];
            right_prefix_max_sum = Math.max(right_prefix_max_sum,
                    total);
        }
        int cross_max_sum = left_suffix_max_sum
                + right_prefix_max_sum
                + nums[mid];
        return Math.max(cross_max_sum, Math.max(left, right));
    }

    public int maxSubArray1(int[] nums) {
        int curMax = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            curMax = Math.max(curMax + nums[i], nums[i]);

            max = Math.max(curMax, max);
        }
        return max;
    }

    public int maxSubArray3(int[] nums) {
        int sum = 0;
        int maxSum = nums[0];
        int minSum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            maxSum = Math.max(sum - minSum, maxSum);
            minSum = Math.min(sum, minSum);
        }
        return maxSum;
    }
}
