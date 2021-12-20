package com.example.madt_lab5_makeyenka;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataLoader extends MainActivity {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://www.floatrates.com/daily/usd.xml";

    private class DownloadXml extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXml(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            exchange_list.setAdapter(adapter);

        }
    }

    private String loadXml(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        Parser parser = new Parser();
        List<Parser.Item> items = null;
        String[] list = {};

        try {
            stream = downloadUrl(urlString);
            items = parser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        int i = 0;
        for (Parser.Item item : items) {
            list[i] = item.targetCurrency + " - " + item.exchangeRate;
            i+= 1;
        }
        display_list = list;
        exchange_list.setAdapter(adapter);
        return list[0];
    }


    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
    public void loadPage() {

        new DownloadXml().execute(URL);
    }
}