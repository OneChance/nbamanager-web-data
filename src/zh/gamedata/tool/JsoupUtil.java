package zh.gamedata.tool;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

public class JsoupUtil {

    public static int TIMEOUT = 30000;

    public static String getJsonContent(String params, String referrer) throws IOException {
        Long reqTime = new Date().getTime();
        String jqueryCallBackName = "jQuery111306562200843946073_" + reqTime;

        Document doc = Jsoup
                .connect("https://slamdunk.sports.sina.com.cn/api?p=radar&callback=" + jqueryCallBackName + params + "&_=" + (reqTime + 1))
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36")
                .ignoreContentType(true)
                .referrer(referrer)
                .timeout(TIMEOUT).get();

        return doc.body().html().replace("try{" + jqueryCallBackName + "(", "")
                .replace(");}catch(e){};", "");
    }

    public static JsonObject getData(JsonObject resultObject) {
        return resultObject.get("result").getAsJsonObject()
                .get("data").getAsJsonObject();
    }
}
