package cn.th.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Utils {
    private static Scanner scan=new Scanner(System.in);
    private static CloseableHttpClient client;
    private static Header[] headers;
    private static final String tbs_url = "http://tieba.baidu.com/dc/common/tbs";
    private static final String tieba_liked_url = "https://tieba.baidu.com/mo/q/newmoindex";
    private static final String tieba_uname_url = "http://tieba.baidu.com/im/pcmsg/query/getUserInfo";
    private static final String one_key_sign_url = "https://tieba.baidu.com/tbmall/onekeySignin1";
    private static final String sign_url = "https://tieba.baidu.com/sign/add";
    private static int retry_num = 3;

    static {
        client = HttpClients.createDefault();
        headers = new Header[3];
        headers[0] = new BasicHeader("Host", "tieba.baidu.com");
        headers[1] = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");
    }
    public static void setCookieInHeaders(String value){
        headers[2]=new BasicHeader("Cookie","BDUSS="+value);
    }
    public static void setCookieAndRetryNumInHeaders(String value, int retryNum){
        headers[2]=new BasicHeader("Cookie","BDUSS="+value);
        retry_num=retryNum;
    }
    public static String getTbs() {
        HttpGet get;
        try {
            get = getHttpGet(new URIBuilder(tbs_url));
            CloseableHttpResponse response = client.execute(get);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            response.close();
            return json.getString("tbs");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpGet getHttpGet(URIBuilder uriBuilder) {
        HttpGet get = new HttpGet();
        get.setHeaders(headers);
        try {
            get.setURI(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return get;
    }

    public static HttpGet getHttpGet(URIBuilder uri, List<NameValuePair> lists) {
        HttpGet get = new HttpGet();
        uri.setParameters(lists);
        get.setHeaders(headers);
        try {
            get.setURI(uri.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return get;
    }

    public static HttpPost getHttpPost(URIBuilder uri) {
        HttpPost post = new HttpPost();
        try {
            post.setURI(uri.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        post.setHeaders(headers);
        return post;
    }

    public static HttpPost getHttpPost(URIBuilder uri, List<NameValuePair> lists) {
        HttpPost post = new HttpPost();
        uri.setParameters(lists);
        try {
            post.setURI(uri.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        post.setHeaders(headers);
        return post;
    }

    public static String getTieBaUid() {
        HttpGet httpget;
        try {
            httpget = getHttpGet(new URIBuilder(tieba_liked_url));
            CloseableHttpResponse response = client.execute(httpget);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            response.close();
            return json.getJSONObject("data").getString("uid");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTieBaNameByUid(String uid) {
        HttpGet get;
        List<NameValuePair> lists = new ArrayList<>();
        lists.add(new BasicNameValuePair("chatUid", uid));
        try {
            get = getHttpGet(new URIBuilder(tieba_uname_url), lists);
            CloseableHttpResponse response = client.execute(get);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            String name = json.getJSONObject("chatUser").getString("uname");
            response.close();
            return name;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getTieBaLiked() {
        List<String> lists = new ArrayList<>();
        HttpGet httpget;
        try {
            httpget = getHttpGet(new URIBuilder(tieba_liked_url));
            CloseableHttpResponse response = client.execute(httpget);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("like_forum");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                lists.add(jsonObject.getString("forum_name"));
            }
            response.close();
            return lists;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getAllUnsignedTieBa() {
        List<String> lists = new ArrayList<>();
        HttpGet httpget = null;
        try {
            httpget = getHttpGet(new URIBuilder(tieba_liked_url));
            CloseableHttpResponse response = client.execute(httpget);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("like_forum");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!"1".equals(jsonObject.getString("is_sign"))) {
                    lists.add(jsonObject.getString("forum_name"));
                }
            }
            response.close();
            return lists;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean oneKeySigned(boolean easy) throws InterruptedException {
        System.out.println("---------------一键签到启动---------------");
        List<NameValuePair> lists = new ArrayList<>();
        HttpPost post;
        try {
            lists.add(new BasicNameValuePair("ie", "utf-8"));
            lists.add(new BasicNameValuePair("tbs", getTbs()));
            post = getHttpPost(new URIBuilder(one_key_sign_url), lists);
            CloseableHttpResponse response = client.execute(post);
            JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
            response.close();
            String stat=json.getString("no");
            switch (stat) {
                case "2280006":
                    System.out.println("您已经一键签到过了");
                    return false;
                case "0":
                    String success = json.getJSONObject("data").getString("signedForumAmount");
                    String fail = json.getJSONObject("data").getString("unsignedForumAmount");
                    System.out.println("一键签到成功，已经一键签到了" + success + "个吧" + "签到失败" + fail + "个吧");
                    return true;
                case "1990055":
                    System.err.println("签到失败，请检查你的BDUSS是否正确");
                    if(!easy) {
                        throw new BDUSSException("BDUSS有误");
                    }else{
                        return false;
                    }
                default:
                    System.out.println(json.getString("error") + "代码" + stat);
                    return false;
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return false;
        } catch (BDUSSException e) {
            changeConfig();
            Main.main(null);
            return false;
        }
    }

    public static void signedByList(List<String> name, List<String> fails, Boolean retry) throws InterruptedException {
        List<NameValuePair> params = new ArrayList<>();
        for (String s : name) {
            Thread.sleep(1345);
            params.clear();
            try {
                params.add(new BasicNameValuePair("ie", "utf-8"));
                params.add(new BasicNameValuePair("tbs", getTbs()));
                params.add(new BasicNameValuePair("kw", s));
                HttpPost post;
                post = getHttpPost(new URIBuilder(sign_url), params);
                CloseableHttpResponse response = client.execute(post);
                JSONObject json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
                String status = json.getString("no");
                if ("0".equals(status)) {
                    System.out.println(s + "吧签到成功!");
                } else if ("1101".equals(status)) {
                    System.out.println(s + "吧已经签到过");
                //验证码处理
                }else if("2150040".equals(status)&&json.getString("error").equals("need vcode")){
                    String vcode_str= json.getJSONObject("data").getString("captcha_vcode_str");
                    params.add(new BasicNameValuePair("captcha_input_str","de"));
                    params.add(new BasicNameValuePair("captcha_vcode_str",vcode_str));
                    post = getHttpPost(new URIBuilder(sign_url), params);
                    response = client.execute(post);
                    json = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity(), "utf-8"));
                    status = json.getString("no");
                    if (status.equals("0")) {
                        System.out.println(s + "吧签到成功!");
                    } else {
                        System.err.println(s + "吧签到出错------->" + json.getString("error"));
                        if (retry) {
                            fails.add(s);
                        }
                    }
                } else {
                    System.err.println(s + "吧签到出错------->" + json.getString("error"));
                    if (retry) {
                        fails.add(s);
                    }
                }
                response.close();
            } catch (Exception e) {
                System.out.println("签到"+s+"吧时出现问题");
                e.printStackTrace();
                if(retry){
                    fails.add(s);
                }
            }
        }
    }

    public static void allSigned(boolean easy) throws InterruptedException {
        List<String> fail = new ArrayList<>();
        if (!oneKeySigned(easy)) {
            System.err.println("一键签到失败");
        }
        System.out.println("---------------轮询签到启动---------------");
        List<String> allUnsigned = getAllUnsignedTieBa();
        if (allUnsigned != null) {
            System.out.println("获取未签到贴吧成功！当前还有" + allUnsigned.size() + "个吧未签到");
            signedByList(allUnsigned, fail, true);
            if(fail.size()!=0) {
                for (int i = 1; i <= retry_num; i++) {
                    System.out.println("一共有" + fail.size() + "个吧签到失败，重试签到次数" + i + "/" + retry_num);
                    List<String> newFailed = new ArrayList<>();
                    signedByList(fail, newFailed, true);
                    fail.clear();
                    if (newFailed.size() != 0) {
                        fail=new ArrayList<>(newFailed);
                        newFailed.clear();
                    } else {
                        System.out.println("所有签到失败贴吧补签成功！");
                        break;
                    }
                }
                if(fail.size()!=0){
                    System.err.println(fail.size()+"个吧签到失败，请重新执行签到程序");
                }
            }else{
                System.out.println("所有贴吧签到成功!!");
            }
        } else {
            System.err.println("获取未签到贴吧失败");
        }
    }
    public static void changeConfig() {
        try {
            Properties properties = new Properties();
            String bduss;
            System.out.println("读取配置文件中......");
            properties.load(new BufferedInputStream(new FileInputStream(new File("./config.properties"))));
            System.out.println("您当前的BDUSS为"+properties.getProperty("BDUSS"));
            System.out.println("请输入您的新BDUSS");
            bduss=scan.nextLine();
            properties.setProperty("BDUSS",bduss);
            properties.store(new FileOutputStream(new File("./config.properties")),"AutoSignedConfigFile");
            Utils.setCookieInHeaders(bduss);
            System.out.println("存储新的BDUSS成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void checkConfig() {
        try {
            File conf = new File("./config.properties");
            Properties properties = new Properties();
            String bduss;
            if (!conf.exists()) {
                System.err.println("未找到对应配置文件,自动创建配置文件中");
                System.out.println("请输入您百度对应的BDUSS，通常在浏览器F12的存储项里，注：BDUSS不会泄露给别人，包括我");
                bduss = scan.nextLine();
                properties.setProperty("BDUSS", bduss);
                properties.store(new FileOutputStream("config.properties"),"AutoSignedConfigFile");
                System.out.println("创建配置文件成功!");
                Utils.setCookieInHeaders(bduss);
            } else {
                System.out.println("导入配置文件中......");
                properties.load(new BufferedInputStream(new FileInputStream(conf)));
                Utils.setCookieInHeaders(properties.getProperty("BDUSS"));
                System.out.println("导入配置文件成功!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int menu() throws InterruptedException {
        System.out.println("---------------菜单---------------");
        System.out.println("1.一键签到(一键签到api+轮训签到)[推荐!]");
        System.out.println("2.一键api签到(网页版，最多签到50个)");
        System.out.println("3.修改BDUSS");
        System.out.println("4.获取所有喜欢的吧");
        System.out.println("5.获取所有未签到吧名单");
        System.out.println("6.退出");
        String oper = scan.nextLine();
        if (StringUtil.isNumeric(oper)) {
            int op = Integer.parseInt(oper);
            switch (op) {
                case 1:
                    Utils.allSigned(false);
                    break;
                case 2:
                    Utils.oneKeySigned(false);
                    break;
                case 3:Utils.changeConfig();
                    break;
                case 4:
                    System.out.println(Utils.getTieBaLiked());
                    break;
                case 5:
                    System.out.println(Utils.getAllUnsignedTieBa());
                    break;
                case 6:
                    return 0;
                default:
                    return -1;
            }
            return 1;
        }
        return -1;
    }
}
