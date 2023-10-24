package com.example.common.util;

public class StringExUtils {
    /**
     * 在字符串末尾填充字符
     *
     * @param input       输入字符串
     * @param length      最终长度
     * @param paddingChar 填充字符
     * @return
     */
    public static String padEnd(String input, int length, char paddingChar) {
        if (input.length() >= length) {
            // 如果字符串长度大于或等于指定长度，不需要填充
            return input;
        } else {
            int paddingLength = length - input.length();
            StringBuilder paddedString = new StringBuilder(input);
            for (int i = 0; i < paddingLength; i++) {
                paddedString.append(paddingChar);
            }
            return paddedString.toString();
        }
    }

    /**
     * 将字符串某个位置往后的字符替换成目标字符
     *
     * @param input     输入字符串
     * @param start     替换开始位置
     * @param targeChar 目标字符
     * @return
     */
    public static String replaceRange(String input, int start, char targeChar) {
        String StrStart = input.substring(0, start);
        int length = input.length();
        return padEnd(StrStart, length, targeChar);
    }

    public static void main(String[] args) {
        String str = "50011211";
        int length = str.length();
        for(int i=6; i<=length; i+=2) {
            String code = replaceRange(str, i, '0');
            String name = "重庆";
            if(i != 2) {
                name = "dddd";
            }

            System.out.println(code + "==" + name);
        }
    }
}
