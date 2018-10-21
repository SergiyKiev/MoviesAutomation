package utils;

public class PalindromeFinder {
    public static String intermediatePalindrome(String s, int left, int right) {
        if (left > right) return null;
        while (left >= 0 && right < s.length()
                && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return s.substring(left + 1, right);
    }
    
    public static boolean isPalindromeInString(String s) {
        if (s == null) return false;
        
        String longest = s.substring(0, 1);
        for (int i = 0; i < s.length() - 1; i++) {
            //odd cases like 121
            String palindrome = intermediatePalindrome(s.toLowerCase(), i, i);
            if (palindrome.length() > longest.length()) {
                longest = palindrome;
            }
            //even cases like 1221
            palindrome = intermediatePalindrome(s.toLowerCase(), i, i + 1);
            if (palindrome.length() > longest.length()) {
                longest = palindrome;
            }
        }
        
        if (longest != null && longest.length() >= 2)
            return true;
        else
            return false;
    }

}
