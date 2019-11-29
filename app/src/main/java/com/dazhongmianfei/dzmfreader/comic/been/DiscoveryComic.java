package com.dazhongmianfei.dzmfreader.comic.been;

import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.book.been.ReadHistory;

import java.util.List;

public class DiscoveryComic  extends BaseAd {
    public String comic_id;//": 10086, //漫画id
    public String cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面
    public String description;//"": "美貌千金与帅气王爷", //描述
    public List<BaseTag> tag;
    public String flag;//"": "更新至32话", //角标
    public String title;//"": "乔乔的奇妙冒险", //漫画名称

    @Override
    public String toString() {
        return "DiscoveryComic{" +
                "comic_id='" + comic_id + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", tag=" + tag +
                ", flag='" + flag + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
