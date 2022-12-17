package com.example.common_utils.deserializers;

import java.util.ArrayList;
import java.util.HashMap;

public final class ConstructorHolder {
    private String name = null;
    private HashMap<String, String[]> args = new HashMap<>();

    private ConstructorHolder(String name, HashMap<String, String[]> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String[]> getArgs() {
        return args;
    }

    public static ConstructorHolder parseFromString(String buildString) throws Exception {
        String[] buildRes = new String[2];

        parsing:
        for (int i = 0; i < buildString.length(); i++) {
            char c = buildString.charAt(i);
            if (c == '(') {
                for (int j = buildString.length() - 1; j >= i; j--) {
                    char t = buildString.charAt(j);
                    if (t == ')') {
                        buildRes[0] = buildString.substring(0, i);
                        buildRes[1] = buildString.substring(i + 1, j);
                        break parsing;
                    }
                }
            }
        }
        String name = buildRes[0];
        String args = buildRes[1];
        return new ConstructorHolder(name, parseArgs(args));
    }

    private static HashMap<String, String[]> parseArgs(String strArgs) {
        HashMap<String, String[]> args = new HashMap<>();

        int lastInd = 0;
        int bracketsBalance = 0;
        int state = 0; // 0 - бежим по имени аргумента, 1 - бежим по значению аргумента, 2 - бежим по внутренней кухне аргумента,
        String curArgName = null;
        for (int i = 0; i <= strArgs.length(); i++) {
            if (i >= strArgs.length()) {
                args.put(curArgName, parseArrayArgs(strArgs.substring(lastInd, i)));
                break;
            }
            char curChar = strArgs.charAt(i);
            if (state == 0) {
                if (curChar != '=') {
                    continue;
                }
                curArgName = strArgs.substring(lastInd, i);
                state = 1;
                lastInd = i + 1;
            } else if (state == 1) {
                if (curChar == '(') {
                    bracketsBalance++;
                    state = 2;
                    continue;
                }

                if (curChar == ',') {
                    args.put(curArgName, parseArrayArgs(strArgs.substring(lastInd, i)));
                    curArgName = null;
                    state = 0;
                    lastInd = i + 2;
                    i++;
                    continue;
                }
            } else if (state == 2) {
                if (curChar == '(')
                    bracketsBalance++;

                if (curChar == ')' && --bracketsBalance == 0) {
                    state = 1;
                    continue;
                }
            }
        }

        return args;
    }

    private static String[] parseArrayArgs(String arrString) {
        if (arrString == null || arrString.length() == 0 || arrString.charAt(0) != '(') {
            return new String[] { arrString };
        }

        ArrayList<String> list = new ArrayList<>();

        int lastInd = 1;
        int bracketBalance = 0;
        for (int i = 1; i < arrString.length(); i++) {
            if (i == arrString.length() - 1) {
                list.add(arrString.substring(lastInd, i));
                break;
            }
            char cur = arrString.charAt(i);
            switch (cur) {
                case '(':
                    bracketBalance++;
                    break;
                case ')':
                    bracketBalance--;
                    break;
                case ',':
                    if (bracketBalance != 0)
                        continue;
                    list.add(arrString.substring(lastInd, i));
                    lastInd = i + 2;
                    i++;
            }
        }

        return list.toArray(new String[0]);
    }
}
