package com.example.madt_lab5_makeyenka;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String ns = null;

    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readChannel(parser);
        } finally {
            in.close();
        }
    }

    private static List readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List items = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static class Item {
        public final String targetCurrency;
        public final String exchangeRate;

        private Item(String targetCurrency, String exchangeRate) {
            this.targetCurrency = targetCurrency;
            this.exchangeRate = exchangeRate;
        }
    }

    private static Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String targetCurrency = null;
        String exchangeRate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("targetCurrency")) {
                targetCurrency = readCurrency(parser);
            } else if (name.equals("exchangeRate")) {
                exchangeRate = readRate(parser);
            } else {
                skip(parser);
            }
        }
        return new Item(targetCurrency, exchangeRate);
    }

    private static String readCurrency(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "targetCurrency");
        String targetCurrency = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "targetCurrency");
        return targetCurrency;
    }

    private static String readRate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "exchangeRate");
        String exchangeRate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "exchangeRate");
        return exchangeRate;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
