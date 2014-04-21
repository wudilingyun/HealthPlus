package com.vee.easyting.parser;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.vee.easyting.domain.Music;

public class SaxMusicParser {

    public List<Music> parse(InputStream is) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        MyHandler handler = new MyHandler();
        parser.parse(is, handler);
        return handler.getSongs();
    }


    public String serialize(List<Music> songs) throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        handler.setResult(result);

        String uri = "";
        String localName = "";

        handler.startDocument();
        handler.startElement(uri, localName, "songs", null);

        AttributesImpl attrs = new AttributesImpl();
        char[] ch = null;
        for (Music music : songs) {
            attrs.clear();
            attrs.addAttribute(uri, localName, "id", "string", String.valueOf(music.getId()));
            handler.startElement(uri, localName, "music", attrs);

            handler.startElement(uri, localName, "name", null);
            ch = String.valueOf(music.getName()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "name");

            handler.startElement(uri, localName, "title", null);
            ch = String.valueOf(music.getTitle()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "title");

            handler.startElement(uri, localName, "singer", null);
            ch = String.valueOf(music.getSinger()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "singer");

            handler.startElement(uri, localName, "lrc", null);
            ch = String.valueOf(music.getSinger()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "lrc");

            handler.startElement(uri, localName, "image", null);
            ch = String.valueOf(music.getSinger()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "image");

            handler.startElement(uri, localName, "time", null);
            ch = String.valueOf(music.getTime()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "time");

            handler.startElement(uri, localName, "url", null);
            ch = String.valueOf(music.getUrl()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "url");

            handler.endElement(uri, localName, "music");
        }
        handler.endElement(uri, localName, "songs");
        handler.endDocument();

        return writer.toString();
    }

    private class MyHandler extends DefaultHandler {

        private List<Music> songs;
        private Music music;
        private StringBuilder builder;

        public List<Music> getSongs() {
            return songs;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            if (songs == null)
                songs = new ArrayList<Music>();
            if (builder == null)
                builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals("music")) {
                music = new Music();
            }
            builder.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("id")) {
                music.setId(Integer.parseInt(builder.toString()));
            } else if (localName.equals("name")) {
                music.setName(builder.toString());
            } else if (localName.equals("title")) {
                music.setTitle(builder.toString());
            } else if (localName.equals("singer")) {
                music.setSinger(builder.toString());
            } else if (localName.equals("lrc")) {
                music.setLrc(builder.toString());
            } else if (localName.equals("image")) {
                music.setAlbum(builder.toString());
            } else if (localName.equals("time")) {
                music.setTime(Long.parseLong(builder.toString()) * 1000);
            } else if (localName.equals("url")) {
                music.setUrl(builder.toString());
            } else if (localName.equals("music")) {
                songs.add(music);
            }
        }
    }
}