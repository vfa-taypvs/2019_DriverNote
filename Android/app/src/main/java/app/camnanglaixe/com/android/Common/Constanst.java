package app.camnanglaixe.com.android.Common;

/**
 * Created by phamvietsontay on 11/27/16.
 */
public class Constanst {
    public static final int NUM_OF_TOPICS = 9;
    public static final String TYPE_1 = "nhieuthongtin"; // Default Text type
    public static final String TYPE_2 = "hinhanh"; // Image Signal type
    public static final String TYPE_3 = "motthongtin"; // 1 Trang thong tin

    public static final String TYPE_POST_1 = "text"; // Default list text type
    public static final String TYPE_POST_2 = "dinhnghia"; // Definition List Type
    public static final String TYPE_POST_3 = "hinhanh";// Image Signal List type
    public static final String TYPE_POST_4 = "pdf";// PDF type
    public static final String TYPE_POST_5 = "url";// Url type
    public static final String TYPE_POST_6 = "multi";// nhieu dzu lieu type

    public static String GET = "GET";
    public static String POST = "POST";

    // API CONSTANST
//    public static String SERVER = "https://traffic.dethoima.info/api";
    public static String SERVER = "http://camnangnguoilaixe.com/public/api";
    public static String API_TEST = "http://api.androidhive.info/volley/person_object.json";
    public static String API_TEST2 = "https://httpbin.org/get";
    public static String API_GET_FULL_INFO = SERVER + "?cmd=get_all";
    public static String API_GET_CURRENT_VERSION = SERVER + "?cmd=get_version";

    // API TAG
    public static final String TAG_API_GET_FULL_INFO = "TAG_API_GET_FULL_INFO";
    public static final String TAG_API_GET_CURRENT_VERSION = "TAG_API_GET_CURRENT_VERSION";

    // FILE LOAD
    public static String FILE_NAME_JSON_TOPIC_PREFIX = "TOPIC_";
    public static String FILE_NAME_JSON_TOPIC_FORMAT = ".txt";
    public static String FILE_JSON_TEST = "InitialFile.txt";
    public static final String FILE_DRIVER_DOWNLOAD_ROOT = "/camnanglaixe";
    public static final String FILE_DRIVER_DOWNLOAD_MAIN_PDF = FILE_DRIVER_DOWNLOAD_ROOT + "/pdf";

    public static String HTML_STYLE_SUPPORT = "<style type='text/css'>table {  border-collapse: collapse;} table, th, td { border: 1px solid black;}</style>" ;
}
