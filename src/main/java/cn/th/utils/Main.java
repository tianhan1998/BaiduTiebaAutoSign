package cn.th.utils;

import org.apache.http.message.BasicHeader;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------------欢迎来到百度贴吧一键签到程序---------------");
        Utils.checkConfig();
        Utils.allSigned();
        while(true){
            int stat=Utils.menu();
            if(stat==0)break;
            if(stat==-1){
                for(int i=0;i<20;i++) {
                    System.out.println("\n");
                }
                System.out.println("输入有误");
            }
        }
        System.out.println("再见");
        System.exit(0);
    }

}
