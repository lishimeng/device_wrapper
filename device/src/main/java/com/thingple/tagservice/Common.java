package com.thingple.tagservice;

import android.util.Log;

public class Common {

    private static String TAG = "Device Comm";

    public static byte bcc(byte[] data, int offset, int size) {
        byte b = 0;

        for(int i = 0; i < size; ++i) {
            b ^= data[offset + i];
        }

        return b;
    }

    private static String hex2Word(byte b) {
        return "" + "0123456789ABCDEF".charAt(15 & b >> 4) + "0123456789ABCDEF".charAt(b & 15);
    }

    public static String arrByte2String(byte[] buf, int offset, int size) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < size; ++i) {
            sb.append(hex2Word(buf[offset + i]));
            if(i < buf.length - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    static int hexToInt(char ch) {
        if(97 <= ch && ch <= 102) {
            return ch - 97 + 10;
        } else if(65 <= ch && ch <= 70) {
            return ch - 65 + 10;
        } else if(48 <= ch && ch <= 57) {
            return ch - 48;
        } else {
            throw new IllegalArgumentException(String.valueOf(ch));
        }
    }

    String hexToAscii(String s) throws IllegalArgumentException {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);

        for(int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);
            sb.append((char)(hexToInt(a) << 4 | hexToInt(b)));
        }

        return sb.toString();
    }

    public static void hexStr2Bytes(String src, byte[] desc, int offset, int max) {
        src = src.replaceAll(" ", "");
        int l = src.length() / 2;
        if(l > max) {
            l = max;
        }

        for(int i = 0; i < l; ++i) {
            int m = i * 2 + 1;
            int n = m + 1;
            String str1 = "0x" + src.substring(i * 2, m) + src.substring(m, n);

            try {
                desc[offset + i] = Integer.decode(str1).byteValue();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }

    }

    public static int byte2Int(byte b) {
        return b < 0?b + 256:b;
    }

    public static int bytes2Int(byte[] data, int offset) {
        return byte2Int(data[offset]) + byte2Int(data[offset + 1]) * 256 + byte2Int(data[offset + 2]) * 256 * 256 + byte2Int(data[offset + 3]) * 256 * 256 * 256;
    }

    public static short checkSum(byte[] b, int offset, int size) {
        short nRet = 0;

        for(int i = 0; i < size; ++i) {
            nRet = (short)(nRet + b[i + offset]);
        }

        return nRet;
    }

    public static short byteToShort(byte b) {
        short r = (short)b;
        if(r < 0) {
            r = (short)(r + 256);
        }

        return r;
    }

    public static void memcpy(byte[] desBuf, byte[] srcBuf, int desOffset, int srcOffset, int count) {
        for(int i = 0; i < count; ++i) {
            desBuf[desOffset + i] = srcBuf[srcOffset + i];
        }

    }

    public static int byteToInt(byte b) {
        int l = b;
        if(b < 0) {
            l = b + 256;
        }

        if(l > 255) {
            l = 0;
        }

        return l;
    }

    public static short crc(byte[] data, int offset, int size) {
        int j = 0;

        int crc;
        for(crc = 0; size-- != 0; ++j) {
            for(int i = 128; i != 0; i /= 2) {
                if((crc & '耀') != 0) {
                    crc *= 2;
                    crc ^= 4129;
                } else {
                    crc *= 2;
                }

                if((byte2Int(data[offset + j]) & i) != 0) {
                    crc ^= 4129;
                }
            }
        }

        short ret = (short)crc;
        if(ret < 0) {
            ret = (short)(ret + 65536);
        }

        return ret;
    }
}