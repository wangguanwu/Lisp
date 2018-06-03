package com.pl; 
 /*
 *Created by Intellij  Idea 2017.2
 *Company: SZDX
 *Author:Monster
 *Date: 2018-05-23
 *Time:10:41
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TransForm {
    private List<String> list;
    private Stack<String> cFormat = new Stack<String>();
    private Set<String> set = new HashSet<>();
    public TransForm(){
        Collections.addAll(set, "add","sub","mul","mod","div");
    }
    public void readFile(String path)throws IOException{
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
    public List<String> getList(){
        return list;
    }
    public Set<String> getSet(){
        return set;
    }
    public void transform(){
        for(String l : list ){
            String re =l.substring(1,l.length()-1).trim();
            String opt[] = re.split("\\s+|\\t");
            try {
                String r = process(opt);
                System.out.println(l + " => " + r);
            }catch (IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }

    private String process(String opt[]) {

        StringBuilder sb = new StringBuilder();
        boolean flag = false;//判断是否遇见开括号
        int bsnum = 0;
        int sindex = 0;
        int benum = 0;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < opt.length; i++) {
            if (flag) {
                list.add(opt[i]);
            } else {
                if (!opt[i].equals("(") && !opt[i].equals(")")) {
                    sb.append(opt[i]);
                    if (set.contains(opt[i]))
                        sb.append("(");
                    else if (set.contains(opt[i - 1])) {
                        sb.append(",");
                    }
                }
            }
            if (opt[i].equals("(")) {
                bsnum++;
                if (bsnum == 1) {
                    sindex = i;
                    flag = true;
                    list.add("(");
                }
            } else if (opt[i].equals(")")) {
                benum++;
                if (bsnum == benum) {
                    flag = false;
                    bsnum = 0;
                    benum = 0;
                    String s[] = new String[1];
                    list.remove(0);
                    list.remove(list.size() - 1);
                    s = list.toArray(s);
                    list.removeAll(list);
                    if (set.contains(opt[sindex - 1])) {
                        sb.append(process(s) + ",");
                    } else {
                        sb.append(process(s));
                    }
                }
            }
        }
        return sb.toString() + ")";
    }
    public static void main(String args[])throws Exception{
        TransForm tf = new TransForm();
        tf.readFile("input.txt");
        tf.transform();
    }
}
