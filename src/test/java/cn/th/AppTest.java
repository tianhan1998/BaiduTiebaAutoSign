package cn.th;

import static org.junit.Assert.assertTrue;

import cn.th.utils.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import javax.swing.text.ElementIterator;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        CloseableHttpClient client= HttpClients.createDefault();
        try {
            URIBuilder uriBuilder=new URIBuilder("http://tieba.baidu.com/f/like/mylike");
            uriBuilder.addParameter("v","1585890605627");
            uriBuilder.addParameter("pn","1");
            HttpGet get=new HttpGet();
            get.setHeader("Cookie","BAIDUID=BEE0DC838C0C40DCEFED5A6AABA45027:FG=1; BIDUPSID=BEE0DC838C0C40DCF394F52E6EEAFC63; PSTM=1582164591; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; BDUSS=FZWfkVSZ1Q5YW1lQ1lEQVNkLU5nLVY2fnNmb0VaOHVUWGxNWEdQYjJ1aWdkM1ZlRVFBQUFBJCQAAAAAAAAAAAEAAACoAQkFZGFidTExMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKDqTV6g6k1ea; TIEBAUID=847a0c60a71f930ac0f404f8; TIEBA_USERTYPE=a42b78ec7919f87a8e02f270; Hm_lvt_98b9d8c2fd6608d564bf2ac2ae642948=1585884928,1585885190,1585885946,1585888758; bdshare_firstime=1582192420080; rpln_guide=1; STOKEN=b24e9594ce9b9400089f2eca0a75649cb5dc09947dcc8570bb8c40c71c5002b2; showCardBeforeSign=1; IS_NEW_USER=47e2f74f71279c59f50c65ac; H_PS_PSSID=1446_31120_21091_30907_31086; Hm_lpvt_98b9d8c2fd6608d564bf2ac2ae642948=1585891975; 84476328_FRSVideoUploadTip=1; st_data=412ab9acb00e2b44e85dbfec3d9793c624113b86fb5d9df98ed9a62c12db5cb7194ec282d4e65e5b2c81a4fd6d158339849a2d5d5c1693e8fa34ef86d9981f7075d12f3a670f9db38edfc8fa05eef93f782d3a7b33abe29dcca172346ba179a54ea27e5046cf4b8bd3826ba03652c0bd7aad944cd595b66242ee0e0aa4467630; st_key_id=17; st_sign=dbdfd266; wise_device=0; delPer=1; PSINO=2");
            get.setHeader("Host","tieba.baidu.com");
            get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");
            get.setURI(uriBuilder.build());
            CloseableHttpResponse response=client.execute(get);
            HttpEntity httpEntity=response.getEntity();
//            System.out.println(EntityUtils.toString(httpEntity,"utf-8"));
            Document document= Jsoup.parse(EntityUtils.toString(httpEntity,"utf-8"));
            Elements elements=document.select(".pagination").select("a");
            String lastindex= null;
            for (Element e: elements) {
                if(e.text().equals("下一页")){
                    break;
                }
                lastindex=e.text();
            }
            System.out.println(lastindex);
//            Elements elements=document.select("tr");
//            System.out.println(elements.select("td > a").not("[class]").text());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void t2(){
        System.out.println(Utils.getTbs());
    }
    @Test
    public void t1(){
        System.out.println(Utils.getTieBaLiked());
    }
    @Test
    public void t3(){
        String uid=Utils.getTieBaUid();
        System.out.println(uid);
        System.out.println(Utils.getTieBaNameByUid(uid));
    }
    @Test
    public void t4(){
        Utils.oneKeySigned();
    }
    @Test
    public void t5(){
        System.out.println(Utils.getAllUnsignedTieBa());
    }
    @Test
    public void t6(){
        Utils.allSigned();
    }
}
