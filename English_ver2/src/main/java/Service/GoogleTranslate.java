package Service;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class GoogleTranslate {
    private static final String GOOGLE_TRANSLATE_URL = "http://translate.google.com/translate_a/single";

    /**
     * Private to prevent instantiation
     */
    private GoogleTranslate() {
    }

    private static String generateURL(String sourceLanguage, String targetLanguage, String text) {
        String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);

        String sb = GOOGLE_TRANSLATE_URL +
                "?client=webapp" +
                "&hl=en" +
                "&sl=" + sourceLanguage +
                "&tl=" + targetLanguage +
                "&q=" + encoded +
                "&multires=1" +
                "&otf=0" +
                "&pc=0" +
                "&trs=1" +
                "&ssel=0" +
                "&tsel=0" +
                "&kc=1" +
                "&dt=t" +
                "&ie=UTF-8" +
                "&oe=UTF-8" +
                "&tk=" + generateToken(text);

        return sb;
    }

    public static String translate(String sourceLanguage, String targetLanguage, String text) throws IOException {
        String urlText = generateURL(sourceLanguage, targetLanguage, text);
        URL url = new URL(urlText);
        String rawData = urlToText(url);//Gets text from Google
        if (rawData == null) {
            return null;
        }
        String[] raw = rawData.split("\"");//Parses the JSON
        if (raw.length < 2) {
            return null;
        }
        return raw[1];//Returns the translation
    }


    private static String urlToText(URL url) throws IOException {
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");

        try (Reader r = new java.io.InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8)) {
            StringBuilder buf = new StringBuilder();
            int ch;
            while ((ch = r.read()) != -1) {
                buf.append((char) ch);
            }
            return buf.toString();
        }
    }

    public static String translateMultipleSentences(String sourceLanguage, String targetLanguage, String text) throws IOException {
        String[] sentences = text.split("[.!?]");
        StringBuilder translations = new StringBuilder();

        for (String sentence : sentences) {
            String translation = translate(sourceLanguage, targetLanguage, sentence.trim());
            translations.append(translation);

            if (sentences.length > 1) {
                translations.append(". ");
            }
        }

        return translations.toString().trim();
    }

    private static int[] TKK() {
        return new int[]{0x6337E, 0x217A58DC + 0x5AF91132};
    }

    private static int shr32(int x, int bits) {
        if (x < 0) {
            long x_l = 0xffffffffL + x + 1;
            return (int) (x_l >> bits);
        }
        return x >> bits;
    }

    private static int RL(int a, String b) {
        for (int c = 0; c < b.length() - 2; c += 3) {
            int d = b.charAt(c + 2);
            d = d >= 65 ? d - 87 : d - 48;
            d = b.charAt(c + 1) == '+' ? shr32(a, d) : (a << d);
            a = b.charAt(c) == '+' ? (a + (d & 0xFFFFFFFF)) : a ^ d;
        }
        return a;
    }

    private static String generateToken(String text) {
        int[] tkk = TKK();
        int b = tkk[0];
        int e = 0;
        int f = 0;
        List<Integer> d = new ArrayList<Integer>();
        for (; f < text.length(); f++) {
            int g = text.charAt(f);
            if (0x80 > g) {
                d.add(e++, g);
            } else {
                if (0x800 > g) {
                    d.add(e++, g >> 6 | 0xC0);
                } else {
                    if (0xD800 == (g & 0xFC00) && f + 1 < text.length() && 0xDC00 == (text.charAt(f + 1) & 0xFC00)) {
                        g = 0x10000 + ((g & 0x3FF) << 10) + (text.charAt(++f) & 0x3FF);
                        d.add(e++, g >> 18 | 0xF0);
                        d.add(e++, g >> 12 & 0x3F | 0x80);
                    } else {
                        d.add(e++, g >> 12 | 0xE0);
                        d.add(e++, g >> 6 & 0x3F | 0x80);
                    }
                }
                d.add(e++, g & 63 | 128);
            }
        }

        int a_i = b;
        for (e = 0; e < d.size(); e++) {
            a_i += d.get(e);
            a_i = RL(a_i, "+-a^+6");
        }
        a_i = RL(a_i, "+-3^+b+-f");
        a_i ^= tkk[1];
        long a_l;
        if (0 > a_i) {
            a_l = 0x80000000L + (a_i & 0x7FFFFFFF);
        } else {
            a_l = a_i;
        }
        a_l %= Math.pow(10, 6);
        return String.format(Locale.US, "%d.%d", a_l, a_l ^ b);
    }
}
