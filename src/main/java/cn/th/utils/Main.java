package cn.th.utils;

import cn.th.utils.Utils;
import org.apache.http.message.BasicHeader;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author tianh
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("---------------欢迎来到百度贴吧一键签到程序---------------");
        System.out.println("作者:田涵");
        Utils.checkConfig();
        Utils.allSigned(false);
        while(true){
            int stat=Utils.menu();
            if(stat==0) {
                break;
            }
            if(stat==-1){
                for(int i=0;i<20;i++) {
                    System.out.println("\n");
                }
                System.err.println("输入有误");
            }
        }
        System.out.println("再见");
        System.exit(0);
    }

}
