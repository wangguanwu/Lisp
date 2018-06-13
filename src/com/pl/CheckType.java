package com.pl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CheckType {
    private List<String> list = null;
    private Set<String> int_set = null;//参数是整数类型的操作
    private Set<String> bool_set = null;//参数是布尔类型
    private Set<String> bool_return = null;//返回是布尔类型
    private Set<String> int_return = null;//返回是整型

    public CheckType() {
        bool_set = new LinkedHashSet<String>();
        int_set = new HashSet<String>();
        bool_return = new HashSet<String>();
        int_return = new HashSet<String>();
        Collections.addAll(int_set, "add", "sub", "mul", "div", "mod", "gt", "lt", "equ");
        Collections.addAll(bool_set, "and", "or", "not");
        Collections.addAll(bool_return, "gt", "lt", "equ", "and", "or", "not");
        Collections.addAll(int_return, "add", "sub", "mul", "div", "mod");
    }

    public void readFile(String path) throws IOException {//读取输入文件
        list = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        while ((line = br.readLine()) != null) {
            list.add(line);//输入的表达式保存在list列表中
        }
        fileInputStream.close();
        br.close();
    }

    public void check() {//处理输入文件
        for (int i = 0; i < list.size(); i++) {
            try {
                String sp[] = list.get(i).split("\\s+|\\t");
                System.out.println(list.get(i) + " => " + this.checkType(sp));//检查参数类型
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    private boolean isValid(String a) throws IllegalArgumentException {
        try {
            int n = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(a + "不是一个数字,请重新输入");
        }
        return true;
    }

    private boolean isArgsValid(String left, String right) throws IllegalArgumentException {//判断参数是否合法
        if (left.equals("int") && right.equals("int")) {
            return true;
        } else if (left.equals("int")) {//right is not int
            isValid(right);
        } else if (right.equals("int")) {
            isValid(left);
        } else {
            if (isValid(left) && isValid(right)) {//判断输入的操作数是否合法
                return true;
            }
        }
        return true;
    }

    private boolean boolNumber(String bool) throws IllegalArgumentException {//判断一个参数是否是boolean型
        if (bool.equals("bool")) {
            return true;
        }
        if(bool.equals("true")||bool.equals("false"))
            return true ;
        else{
            throw new IllegalArgumentException();
        }
    }
    private boolean isArgsValidBool(String left, String right) throws IllegalArgumentException {//判断两个bool型参数是否合法
        boolNumber(left);
        boolNumber(right);
        return true;
    }

    private int number(String a) {
        return Integer.parseInt(a);
    }

    private String processTwo(String opt, String left, String right) throws IllegalArgumentException {//处理两个参数
        try {
            if (this.int_return.contains(opt)) {//返回值是整型
                if (isArgsValid(left, right))
                    return "int";
            } else if (this.bool_return.contains(opt)) {//返回值是bool型
                if (this.int_set.contains(opt)) {//整型参数
                    if (isArgsValid(left, right))
                        return "bool";
                } else {//布尔型参数
                    if (this.isArgsValidBool(left, right)) {
                        return "bool";
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return "e";//输入操作数如果非法，那么返回e作为计算结果
        }
        return "e";
    }

    private String processOne(String opt, String arg) {
        try {
            boolean b = this.boolNumber(arg);
            return "bool";
        } catch (IllegalArgumentException e) {
            return "e";
        }
    }

    private String checkType(String opt[]) throws IllegalArgumentException {
        Deque<String> opt_syn = new LinkedList<String>();//运算符号栈
        Deque<String> opt_data = new LinkedList<String>();//运算数据栈
        String result = "";
        for (int i = 0; i < opt.length; i++) {
            String c = opt[i];
            switch (c) {
                case "(":
                    opt_syn.addFirst(c);
                    break;//开括号进栈
                case ")":
                    String ops = opt_syn.pollFirst(); //运算符出栈
                    opt_syn.pollFirst();//开括号出栈
                    String right = opt_data.pollFirst();//取得右操作数
                    if (!ops.equals("not")) {
                        String left = opt_data.pollFirst();//取得左操作数
                        result = processTwo(ops, left, right);//得到运算结果
                    } else {
                        result = processOne(ops, right);
                    }
                    opt_data.addFirst(result);//运算结果进栈
                    break;
                default:
                    if (this.int_set.contains(c) || this.bool_set.contains(c)) {
                        opt_syn.addFirst(c);
                    } else {
                        opt_data.addFirst(c);
                    }
                    break;//其他运算符数据进栈
            }
        }
        return opt_data.pollFirst();
    }
    public static void main(String args[]) throws IOException {
        CheckType checkType = new CheckType();
        checkType.readFile("check.txt");
        checkType.check();

    }

}
