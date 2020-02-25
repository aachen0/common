package com.ahjrlc.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型编码表
 *
 * @author aachen0
 */
class FileTypeMap {
    //    文件类型与内容头十六进制对应表
    final static Map<String, String> HEX_MAP = new HashMap<>();

    static {
        HEX_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        HEX_MAP.put("png", "89504E47");  //PNG (png)
        HEX_MAP.put("gif", "47494638");  //GIF (gif)
        HEX_MAP.put("tif", "49492A00");  //TIFF (tif)
        HEX_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        HEX_MAP.put("dwg", "41433130"); //CAD (dwg)
        HEX_MAP.put("html", "68746D6C3E");  //HTML (html)
        HEX_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        HEX_MAP.put("xml", "3C3F786D6C");
        HEX_MAP.put("zip", "504B0304");
        HEX_MAP.put("rar", "52617221");
        HEX_MAP.put("psd", "38425053");  //Photoshop (psd)
        HEX_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        HEX_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        HEX_MAP.put("pst", "2142444E");  //Outlook (pst)
        HEX_MAP.put("xls", "D0CF11E0");  //MS Word
        HEX_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        HEX_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        HEX_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        HEX_MAP.put("eps", "252150532D41646F6265");
        HEX_MAP.put("ps", "252150532D41646F6265");
        HEX_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        HEX_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        HEX_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        HEX_MAP.put("wav", "57415645");  //Wave (wav)
        HEX_MAP.put("avi", "41564920");
        HEX_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        HEX_MAP.put("rm", "2E524D46");  //Real Media (rm)
        HEX_MAP.put("mpg", "000001BA");  //
        HEX_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        HEX_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        HEX_MAP.put("mid", "4D546864");  //MIDI (mid)
    }
}
