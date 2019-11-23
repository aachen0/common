package com.ahjrlc.common;

import com.ahjrlc.common.annotation.FieldDesc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */
public class CommonUtil {
    private final static char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final static char[] CHARS_NUMBER = "0123456789".toCharArray();
    private final static Map<String, String> FILE_TYPE = new HashMap<>();

    static {
        FILE_TYPE.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE.put("png", "89504E47");  //PNG (png)
        FILE_TYPE.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE.put("xml", "3C3F786D6C");
        FILE_TYPE.put("zip", "504B0304");
        FILE_TYPE.put("rar", "52617221");
        FILE_TYPE.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE.put("eps", "252150532D41646F6265");
        FILE_TYPE.put("ps", "252150532D41646F6265");
        FILE_TYPE.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE.put("avi", "41564920");
        FILE_TYPE.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE.put("mpg", "000001BA");  //
        FILE_TYPE.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE.put("mid", "4D546864");  //MIDI (mid)
    }


    /**
     * 根据一个文件的byte数组解析文件类型，如果解析失败，返回null
     *
     * @param fileBytes 文件数组
     * @return 文件类型
     */
    public static String getFileType(byte[] fileBytes) {
        if (fileBytes != null && fileBytes.length > 0) {
            String fileTypeHex = getFileHexString(fileBytes).toUpperCase();
            for (Map.Entry<String, String> entry : FILE_TYPE.entrySet()) {
                String fileTypeHexValue = entry.getValue();
                if (fileTypeHex.startsWith(fileTypeHexValue)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = Math.max(b.length, 28);
        for (int i = 0; i < length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return new String(stringBuilder);
    }


    /**
     * 对一个字符串序列对中间部分指定长度用指定字符替换，生产打码字符串
     *
     * @param oriString    原始字符串序列
     * @param mosaic       指定马赛克字符
     * @param mosaicLength 指定马赛克长度
     * @return 打码后打字符串
     */
    public static String mosaic(CharSequence oriString, char mosaic, int mosaicLength) {
        if (oriString != null) {
            int totalLength = oriString.length();
            mosaicLength = Math.min(mosaicLength, totalLength);
            int startIndex = Math.round((totalLength - mosaicLength) / 2.0f);
            int endIndex = startIndex + mosaicLength;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < totalLength; i++) {
                if (i >= startIndex && i < endIndex) {
                    sb.append(mosaic);
                } else {
                    sb.append(oriString.charAt(i));
                }
            }
            return new String(sb);
        } else {
            return null;
        }

    }

    public static String inString(List powerIds) {
        if (powerIds != null && powerIds.size() > 0) {
            String s = powerIds.toString();
            if (powerIds.get(0) instanceof Number) {
                return s.replace('[', '(').replace(']', ')');
            } else {
                return s.replace("[", "('").replace("]", "')").replace(",", "','").replace(" ", "");
            }
        }
        return null;
    }

    /**
     * 将下划线分割的命名转换为java的驼峰命名
     *
     * @param _name_ 下划线名称
     * @param isCap  首字母是否大写
     * @return 驼峰命名的实体类名
     */
    public static String camel(String _name_, boolean isCap) {
        if (_name_ != null && !"".equals(_name_.trim())) {
            int size = _name_.length();
            char[] chars = _name_.toCharArray();
            String firstChar = chars[0] + "";
            if (isCap) {
                firstChar = firstChar.toUpperCase();
            }
            StringBuffer entityName = new StringBuffer(firstChar);
            for (int i = 1; i < size; i++) {
                if (chars[i] != '_') {
                    entityName.append(chars[i]);
                } else {
                    i++;
                    if (i < size) {
                        entityName.append((chars[i] + "").toUpperCase());
                    }
                }
            }
            return new String(entityName);
        }
        return null;
    }

    /**
     * 比较某个对象的每个属性，并给出结果，如果有一个对象是null则返回null，否则返回比较结果
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 差异列表
     */
    public static List<Change> compare(Object o1, Object o2) {
        if (o1 != null && o2 != null) {
            List<Change> changes = new ArrayList<>();
            Class<?> srcClazz = o1.getClass();
            if (!srcClazz.getName().equals(o2.getClass().getName())) {
                Change change = new Change();
                change.fieldName = "不同对象，无法比较";
                changes.add(change);
                return changes;
            }
            Class superclass = srcClazz.getSuperclass();
            Field[] fields = srcClazz.getDeclaredFields();
            Field[] superFields = superclass.getDeclaredFields();
            List<Field> allFields = new ArrayList<>();
            Collections.addAll(allFields, fields);
            Collections.addAll(allFields, superFields);
            for (Field field : allFields) {
                FieldDesc annotation = field.getAnnotation(FieldDesc.class);
                String filedTitle = "";
                if (annotation != null) {
                    filedTitle = annotation.value();
                }
                String fieldName = field.getName();
                String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method method;
                    try {
                        method = srcClazz.getMethod(getterName);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    Object srcObject = method.invoke(o1);
                    String oriValue = srcObject == null ? "null" : toStringPlus(srcObject);
                    Object targetObject = method.invoke(o2);
                    String newValue = targetObject == null ? "null" : toStringPlus(targetObject);
                    if (!oriValue.equals(newValue)) {
                        Change change = new Change();
                        change.setFieldName(filedTitle);
                        change.setOriginValue(oriValue);
                        change.setNewValue(newValue);
                        changes.add(change);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return changes;
        }
        return null;
    }

    private static class Change {
        private String fieldName;
        private String originValue;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getOriginValue() {
            return originValue;
        }

        public void setOriginValue(String originValue) {
            this.originValue = originValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        private String newValue;
    }

    private static String toStringPlus(Object o) {
        if (o instanceof Date) {
            Date oriDate = (Date) o;
            return new SimpleDateFormat(CommonConst.DATE_PATTERN).format(oriDate);
        }
        if (o instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) o;
            return decimal.stripTrailingZeros().toString();
        }
        return o.toString();
    }


    public static String generateQueryCode() {
        String code = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        code = code + generateRandomCode(CHARS, 4);
        return code;
    }

    /**
     * 生成6位数字手机验证
     *
     * @return 验证码
     */
    public static String generateVerifyCode() {
        return generateRandomCode(CHARS_NUMBER, 6);
    }

    /**
     * 使用指定字符集生成指定长度随机码
     *
     * @param chars 字符集
     * @param size  随机码长度
     * @return 随机码
     */
    public static String generateRandomCode(char[] chars, int size) {
        if (size > 0) {
            int length = chars.length;
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < size; i++) {
                code.append(chars[new Random().nextInt(length)]);
            }
            return new String(code);
        } else {
            return "";
        }
    }

    public static Calendar convertDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 判断一个对象是否为null或者空串或者空集合
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        // 字符串
        if (object instanceof String) {
            return ((String) object).trim().length() < 1;
        }
        // 集合
        if (object instanceof Collection) {
            return ((Collection) object).size() < 1;
        }
        // 数组无元素，或者有一个空的元素都为空
        if (object.getClass().isArray()) {
            Object[] objects = (Object[]) object;
            return objects.length < 1;
        }
        return false;
    }
}