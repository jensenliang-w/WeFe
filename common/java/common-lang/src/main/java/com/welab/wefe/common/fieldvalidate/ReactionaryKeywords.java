/*
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.welab.wefe.common.fieldvalidate;

import com.welab.wefe.common.util.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 舆情词库
 * <p>
 * 词库来源：https://www.heywhale.com/mw/dataset/5ecf6214162df90036ddfcff/file
 *
 * @author zane
 * @date 2021/12/30
 */
public class ReactionaryKeywords {
    private static final String[] 色情词库 = {"爱女人", "爱液", "按摩棒", "拔出来", "爆草", "包二奶", "暴干", "暴奸", "暴乳", "爆乳", "暴淫", "屄", "被操", "被插", "被干", "逼奸", "仓井空", "插暴", "操逼", "操黑", "操烂", "肏你", "肏死", "操死", "操我", "厕奴", "插比", "插b", "插逼", "插进", "插你", "插我", "插阴", "潮吹", "潮喷", "成人dv", "成人电影", "成人论坛", "成人小说", "成人电", "成人电影", "成人卡通", "成人聊", "成人片", "成人视", "成人图", "成人文", "成人小", "成人电影", "成人论坛", "成人色情", "成人网站", "成人文学", "成人小说", "艳情小说", "成人游戏", "吃精", "赤裸", "抽插", "扌由插", "抽一插", "春药", "大波", "大力抽送", "大乳", "荡妇", "荡女", "盗撮", "多人轮", "发浪", "放尿", "肥逼", "粉穴", "封面女郎", "风月大陆", "干死你", "干穴", "肛交", "肛门", "龟头", "裹本", "国产av", "好嫩", "豪乳", "黑逼", "后庭", "后穴", "虎骑", "花花公子", "换妻俱乐部", "黄片", "几吧", "鸡吧", "鸡巴", "鸡奸", "寂寞男", "寂寞女", "妓女", "激情", "集体淫", "奸情", "叫床", "脚交", "金鳞岂是池中物", "金麟岂是池中物", "精液", "就去日", "巨屌", "菊花洞", "菊门", "巨奶", "巨乳", "菊穴", "开苞", "口爆", "口活", "口交", "口射", "口淫", "裤袜", "狂操", "狂插", "浪逼", "浪妇", "浪叫", "浪女", "狼友", "聊性", "流淫", "铃木麻", "凌辱", "漏乳", "露b", "乱交", "乱伦", "轮暴", "轮操", "轮奸", "裸陪", "买春", "美逼", "美少妇", "美乳", "美腿", "美穴", "美幼", "秘唇", "迷奸", "密穴", "蜜穴", "蜜液", "摸奶", "摸胸", "母奸", "奈美", "奶子", "男奴", "内射", "嫩逼", "嫩女", "嫩穴", "捏弄", "女优", "炮友", "砲友", "喷精", "屁眼", "品香堂", "前凸后翘", "强jian", "强暴", "强奸处女", "情趣用品", "情色", "拳交", "全裸", "群交", "惹火身材", "人妻", "人兽", "日逼", "日烂", "肉棒", "肉逼", "肉唇", "肉洞", "肉缝", "肉棍", "肉茎", "肉具", "揉乳", "肉穴", "肉欲", "乳爆", "乳房", "乳沟", "乳交", "乳头", "三级片", "骚逼", "骚比", "骚女", "骚水", "骚穴", "色逼", "色界", "色猫", "色盟", "色情网站", "色区", "色色", "色诱", "色欲", "色b", "少年阿宾", "少修正", "射爽", "射颜", "食精", "释欲", "兽奸", "兽交", "手淫", "兽欲", "熟妇", "熟母", "熟女", "爽片", "爽死我了", "双臀", "死逼", "丝袜", "丝诱", "松岛枫", "酥痒", "汤加丽", "套弄", "体奸", "体位", "舔脚", "舔阴", "调教", "偷欢", "偷拍", "推油", "脱内裤", "文做", "我就色", "无码", "舞女", "无修正", "吸精", "夏川纯", "相奸", "小逼", "校鸡", "小穴", "小xue", "写真", "性感妖娆", "性感诱惑", "性虎", "性饥渴", "性技巧", "性交", "性奴", "性虐", "性息", "性欲", "胸推", "穴口", "学生妹", "穴图", "亚情", "颜射", "阳具", "杨思敏", "要射了", "夜勤病栋", "一本道", "一夜欢", "一夜情", "一ye情", "阴部", "淫虫", "阴唇", "淫荡", "阴道", "淫电影", "阴阜", "淫妇", "淫河", "阴核", "阴户"};
    private static final String[] 民生词库 = {"打人", "拆迁", "纠纷", "盗窃", "安眠酮", "代药物毒品类：血浆", "普萘洛尔", "呋塞米", "西布曲明", "testosterone", "胰岛素样生长因子", "促红细胞生成素", "地西泮", "尼可刹米", "甲睾酮", "adrenaline", "erythropoietin", "地奈德", "莫达非尼", "氯噻嗪", "苯巴比妥", "促性腺激素", "泼尼松", "麻黄草", "雄烯二醇", "地塞米松", "tamoxifen", "strychnine", "androst", "新型毒品", "杜冷丁", "兴奋剂", "mdma", "海洛因", "海luo因", "heroin", "diamorphine", "diacetylmorphine", "鸦片", "阿芙蓉", "咖啡因", "cocain", "三唑仑", "美沙酮", "麻古", "k粉", "凯他敏", "ketamine", "冰毒", "苯丙胺", "cannabis", "大麻", "爱他死", "氯胺酮", "benzodiazepines", "甲基安非他明", "安非他命", "吗啡", "morphine", "摇头丸", "迷药", "乖乖粉", "narcotic", "麻醉药", "精神药品", "专业代理", "帮忙点一下", "帮忙点下", "请点击进入", "详情请进入", "私人侦探", "私家侦探", "针孔摄象", "调查婚外情", "信用卡提现", "无抵押贷款", "广告代理", "原音铃声", "借腹生子", "找个妈妈", "找个爸爸", "代孕妈妈", "代生孩子", "代开发票", "腾讯客服电话", "销售热线", "免费订购热线", "低价出售", "款到发货", "回复可见", "加盟连锁", "免费二级域名", "免费使用", "免费索取", "蚁力神", "婴儿汤", "售肾", "刻章办", "买小车", "套牌车", "玛雅网", "电脑传讯", "视频来源", "下载速度", "高清在线", "全集在线", "在线播放", "切腹", "自杀", "四海帮", "足球投注", "地下钱庄", "中国复兴党", "阿波罗网", "曾道人", "六合彩", "改卷内幕", "替考试", "隐形耳机", "出售答案", "考中答案", "答an", "da案", "资金周转", "救市", "股市圈钱", "崩盘", "资金短缺", "证监会", "质押贷款", "小额贷款", "周小川", "刘明康", "尚福林", "孔丹", "汉芯造假", "杨树宽", "中印边界谈判结果", "喂奶门", "摸nai门", "酒瓶门", "脱裤门", "75事件", "乌鲁木齐", "新疆骚乱", "针刺", "打针", "食堂涨价", "饭菜涨价", "h1n1", "瘟疫爆发", "yangjia", "y佳", "yang佳", "杨佳", "杨j", "袭警", "杀警", "武侯祠", "川b26931", "贺立旗", "周正毅", "px项目", "骂四川", "家l福", "家le福", "加了服", "麦当劳被砸", "豆腐渣", "这不是天灾", "龙小霞", "震其国土", "yuce", "提前预测", "地震预测", "隐瞒地震", "李四光预测", "蟾蜍迁徙", "踩踏事故", "聂树斌", "万里大造林", "陈相"};
    private static final String[] 暴恐词库 = {"福音会", "中国教徒", "统一教", "观音法门", "清海无上师", "李洪志", "志洪李", "李宏志", "轮功", "法轮", "轮法功", "三去车仑", "氵去车仑", "发论工", "法x功", "法o功", "法0功", "法一轮一功", "轮子功", "车仑工力", "法lun", "fa轮", "法lg", "flg", "fl功", "falungong", "大法弟子", "大纪元", "dajiyuan", "明慧网", "明慧周报", "正见网", "新唐人", "伪火", "退党", "tuidang", "退dang", "超越红墙", "自fen", "真善忍", "九评", "9评", "9ping", "九ping", "jiuping", "藏字石", "集体自杀", "自sha", "zi杀", "suicide", "titor", "逢8必灾", "逢八必灾", "逢9必乱", "逢九必乱", "朱瑟里诺", "根达亚文明", "诺查丹玛斯", "人类灭亡进程表", "按照马雅历法", "推背图", "推bei图", "济世灵文", "诸世纪", "电狗", "电话定位器", "电话拦截器", "电话窃听", "电话监", "电话交友", "电话追杀系统", "电击枪", "电鸡", "电警棒", "枪出售", "枪的制", "枪货到", "枪决女犯", "枪模", "枪手", "枪销售", "枪械制", "枪子弹", "售步枪", "售纯度", "售单管", "售弹簧刀", "售防身", "售狗子", "售虎头", "售火药", "售假币", "售健卫", "售军用", "售猎枪", "售氯胺", "售麻醉", "售枪支", "售热武", "售三棱", "售手枪", "售五四", "售一元硬", "售子弹", "售左轮", "亚砷（酸）酐", "亚砷酸钾", "亚砷酸钠", "亚硒酸", "亚硒酸二钠", "亚硒酸镁", "亚硒酸钠", "亚硒酸氢钠", "亚硝酸乙酯", "亚硝酰乙氧", "氧化二丁基锡", "氧化汞", "氧化铊", "氧化亚铊", "氧氯化磷", "原装弹", "原子弹方法", "原子弹清单", "安眠酮", "代血浆", "普萘洛尔", "呋塞米", "西布曲明", "testosterone", "胰岛素样生长因子", "促红细胞生成素", "地西泮", "尼可刹米", "甲睾酮", "adrenaline", "erythropoietin", "地奈德", "莫达非尼", "氯噻嗪", "苯巴比妥", "促性腺激素", "泼尼松", "麻黄草", "雄烯二醇", "地塞米松", "tamoxifen", "strychnine", "androst", "新型毒品", "杜冷丁", "兴奋剂", "mdma", "海洛因", "海luo因", "heroin", "diamorphine", "diacetylmorphine", "鸦片", "阿芙蓉", "咖啡因", "cocain", "三唑仑", "美沙酮", "麻古", "k粉", "凯他敏", "ketamine", "冰毒", "苯丙胺", "cannabis", "大麻", "爱他死", "氯胺酮", "benzodiazepines", "甲基安非他明", "安非他命", "吗啡"};
    private static final String[] 政治类 = {"习近平", "平近习", "xjp", "习太子", "习明泽", "老习", "温家宝", "温加宝", "温x", "温jia宝", "温宝宝", "温加饱", "温加保", "张培莉", "温云松", "温如春", "温jb", "胡温", "胡x", "胡jt", "胡boss", "胡总", "胡王八", "hujintao", "胡jintao", "胡j涛", "胡惊涛", "胡景涛", "胡紧掏", "湖紧掏", "胡紧套", "锦涛", "hjt", "胡派", "胡主席", "刘永清", "胡海峰", "胡海清", "江泽民", "民泽江", "江哥", "江主席", "江书记", "江浙闽", "江沢民", "江浙民", "择民", "则民", "茳泽民", "zemin", "ze民", "老江", "老j", "江core", "江x", "江派", "江zm", "jzm", "江戏子", "江蛤蟆", "江某某", "江贼", "江猪", "江氏集团", "江绵恒", "江绵康", "王冶坪", "江泽慧", "邓小平", "平小邓", "xiao平", "邓xp", "邓晓平", "邓朴方", "邓榕", "邓质方", "毛泽东", "猫泽东", "猫则东", "猫贼洞", "毛zd", "毛zx", "z东", "ze东", "泽d", "zedong", "毛太祖", "毛相", "主席画像", "改革历程", "朱镕基", "朱容基", "朱镕鸡", "朱容鸡", "朱云来", "李鹏", "李peng", "里鹏", "李月月鸟", "李小鹏", "李小琳", "华主席", "华国", "国锋", "国峰", "锋同志", "白春礼", "薄熙来", "薄一波", "蔡赴朝", "蔡武", "曹刚川", "常万全", "陈炳德", "陈德铭", "陈建国", "陈良宇", "陈绍基", "陈同海", "陈至立", "戴秉国", "丁一平", "董建华", "杜德印", "杜世成", "傅锐", "郭伯雄", "郭金龙", "贺国强", "胡春华", "耀邦", "华建敏", "黄华华", "黄丽满", "黄兴国", "回良玉", "贾庆林", "贾廷安", "靖志远", "李长春", "李春城", "李建国", "李克强", "李岚清", "李沛瑶", "李荣融", "李瑞环", "李铁映", "李先念", "李学举", "李源潮", "栗智", "梁光烈", "廖锡龙", "林树森", "林炎志", "林左鸣", "令计划", "柳斌杰", "刘奇葆", "刘少奇", "刘延东", "刘云山", "刘志军", "龙新民", "路甬祥", "罗箭", "吕祖善", "马飚", "马恺", "孟建柱", "欧广源", "强卫", "沈跃跃", "宋平顺", "粟戎生", "苏树林", "孙家正", "铁凝", "屠光绍", "王东明", "汪东兴", "王鸿举", "王沪宁", "王乐泉", "王洛林", "王岐山", "王胜俊", "王太华", "王学军", "王兆国", "王振华", "吴邦国", "吴定富", "吴官正", "无官正", "吴胜利", "吴仪", "奚国华", "习仲勋", "徐才厚", "许其亮", "徐绍史", "杨洁篪", "叶剑英", "由喜贵", "于幼军", "俞正声", "袁纯清", "曾培炎", "曾庆红", "曾宪梓", "曾荫权", "张德江", "张定发", "张高丽", "张立昌", "张荣坤", "张志国", "赵洪祝", "紫阳", "周生贤", "周永康", "朱海仑", "中南海", "大陆当局", "中国当局", "北京当局", "共产党", "党产共", "共贪党", "阿共", "产党共", "公产党", "工产党", "共c党", "共x党", "共铲", "供产", "共惨", "供铲党", "供铲谠", "供铲裆", "共残党", "共残主义", "共产主义的幽灵", "拱铲", "老共", "中共", "中珙", "中gong"};
    private static final String[] 政治类_反动词库 = {"习近平", "习仲勋", "十九大修宪", "习近平连任", "宪法修正案", "任期限制", "腐败中国", "三个代表", "社会主义灭亡", "打倒", "抵制", "灭亡中国", "亡党亡国", "粉碎四人帮", "激流中国", "特供", "特贡", "特共", "zf大楼", "殃视", "贪污腐败", "强制拆除", "形式主义", "政治风波", "太子党", "上海帮", "北京帮", "清华帮", "红色贵族", "权贵集团", "河蟹社会", "喝血社会", "九风", "9风", "十七大", "十7大", "17da", "九学", "9学", "四风", "4风", "双规", "南街村", "最淫官员", "警匪", "官匪", "独夫民贼", "官商勾结", "城管暴力执法", "强制捐款", "毒豺", "一党执政", "一党专制", "一党专政", "专制政权", "宪法法院", "胡平", "苏晓康", "贺卫方", "谭作人", "焦国标", "万润南", "张志新", "辛��", "高勤荣", "王炳章", "高智晟", "司马璐", "刘晓竹", "刘宾雁", "魏京生", "寻找林昭的灵魂", "别梦成灰", "谁是新中国", "讨伐中宣部", "异议人士", "民运人士", "启蒙派", "选国家主席", "民一主", "min主", "民竹", "民珠", "民猪", "chinesedemocracy", "大赦国际", "国际特赦", "da选", "投公", "公头", "宪政", "平反", "党章", "维权", "昝爱宗", "宪章", "08宪", "08xz", "抿主", "敏主", "人拳", "人木又", "人quan", "renquan", "中国人权", "中国新民党", "群体事件", "群体性事件", "上中央", "去中央", "讨说法", "请愿", "请命", "公开信", "联名上书", "万人大签名", "万人骚动", "截访", "上访", "shangfang", "信访", "访民", "集合", "集会", "组织集体", "静坐", "静zuo", "jing坐", "示威", "示wei", "游行", "you行", "油行", "游xing", "youxing", "官逼民反", "反party", "反共", "抗议", "亢议", "抵制", "低制", "底制", "di制", "抵zhi", "dizhi", "boycott", "血书", "焚烧中国国旗", "baoluan", "流血冲突", "出现暴动", "发生暴动", "引起暴动", "baodong", "灭共", "杀毙", "罢工", "霸工", "罢考", "罢餐"};

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        String[][] list = {色情词库, 民生词库, 暴恐词库, 政治类, 政治类_反动词库};
        for (String[] dic : list) {
            KEYWORDS.addAll(Arrays.asList(dic));
        }
    }

    /**
     * 检查文本中是否包含舆情关键字
     */
    public static boolean contains(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }

        return KEYWORDS
                .parallelStream()
                .anyMatch(x -> str.contains(x));
    }

    public static String match(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }

        return KEYWORDS
                .parallelStream()
                .filter(x -> str.contains(x))
                .findFirst()
                .orElse(null);
    }

    public static void main(String[] args) {
        System.out.println(KEYWORDS);
        System.out.println(KEYWORDS.size());

        contains("罢工");

        for (int i = 0; i < 20; i++) {
            String str = UUID.randomUUID().toString();
            long start = System.currentTimeMillis();
            contains(str);
            long spend = System.currentTimeMillis() - start;
            System.out.println("spend:" + spend);
        }
    }

}
