package com.pl;

import sun.plugin.viewer.context.IExplorerAppletContext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Channel;
import java.util.*;

public class Calculator {
    private List<String> list = null ;
    private Set<String> set = null;

    public Calculator() {
        set = new LinkedHashSet<String>();
        Collections.addAll(set,"add","sub","mul","div","mod");
    }

    public void readFile(String path)throws IOException {
        list = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        while((line = br.readLine())!=null){
            list.add(line);
        }
        fileInputStream.close();
        br.close();
    }
    public void calcul(){
        for(int i= 0 ; i < list.size() ; i++){
            try {
                String sp[] = list.get(i).split("\\s+|\\t");
                System.out.println(calcul(sp));
            }catch(IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }
    private boolean isValid(String a)throws IllegalArgumentException{
       try{
           int n = Integer.parseInt(a) ;
       }catch (NumberFormatException e){
           throw new IllegalArgumentException(a+"不是一个数字,请重新输入");
       }
       return true ;
    }
    private int number( String a ){
        return Integer.parseInt(a) ;
    }
    private String processOne(String opt , String left,String right )throws IllegalArgumentException{
        String result = "";
        int l = 0 , r = 0;

        try{
            if( isValid(left )){
                l = this.number(left) ;
            }
            if(isValid(right))
                r = this.number(right) ;
            switch (opt){
                case "add" : result=add( l ,r );break;
                case "sub" : result=sub( l ,r );break;
                case "mul" : result=mul( l ,r );break;
                case "div" : result=div( l ,r );break;
                case "mod" : result=mod( l ,r );break;
            }
            return String.valueOf(result);
        }catch (IllegalArgumentException e){
           return "e";
        }
    }
    private String calcul(String opt[])throws IllegalArgumentException{
        Deque<String> opt_syn = new LinkedList<String>();//运算符号栈
        Deque<String> opt_data = new LinkedList<String>();//运算数据栈
        String result ="";
        for( int i = 0 ; i < opt.length ; i++){
            String c = opt[i];
            switch (c){
                case "(":   opt_syn.addFirst(c);break ;//开括号进栈
                case ")":   String ops = opt_syn.pollFirst(); //运算符出栈
                            opt_syn.pollFirst();//开括号出栈
                            String right = opt_data.pollFirst();//取得右操作数
                            String left = opt_data.pollFirst();//取得左操作数
                            result =processOne(ops, left , right );//得到运算结果
                            opt_data.addFirst(result);//运算结果进栈
                            break ;
                case "add": opt_syn.addFirst(c);break;
                case "sub": opt_syn.addFirst(c);break;
                case "mul": opt_syn.addFirst(c);break;
                case "div": opt_syn.addFirst(c);break;
                case "mod": opt_syn.addFirst(c);break;
                default:    opt_data.addFirst(c);break ;
            }
        }
        return opt_data.pollFirst();
    }
    private String add( int a, int b )throws IllegalArgumentException{
        return String.valueOf(a + b );
    }

    private String sub( int a , int b )throws IllegalArgumentException{
        return String.valueOf( a- b );
    }
    private String mul( int a , int b )throws IllegalArgumentException{
        return String.valueOf(a*b );
    }
    private String div(int a , int b )throws IllegalArgumentException{
       try {
           int r = a / b ;
           return String.valueOf(r);
       }catch (ArithmeticException e){
           return "e" ;
       }
    }
    private String mod(int a , int b )throws IllegalArgumentException{
        try {
            int r = a % b ;
            return String.valueOf(r);
        }catch (ArithmeticException e){
            return "e" ;
        }
    }
    public static void main(String args[])throws IOException {
        Calculator calculator = new Calculator();
        calculator.readFile("input.txt");
        calculator.calcul();

    }

}
