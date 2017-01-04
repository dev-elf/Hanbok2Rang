package mountainq.organization.dongguk.hanbokapp.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.datas.LocationItem;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class TourAPIHandler extends DefaultHandler {

    private ArrayList<LocationItem> items = new ArrayList<>();
    private LocationItem item;
    private String tagName = "";


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch, start, length);
        switch (tagName){
            case "mapx":
                item.setMapLon(data);
                break;
            case "mapy":
                item.setMapLat(data);
                break;
            case "title":
                item.setLocationName(data);
                break;
            case "firstimage":
                item.setFirstImgUrl(data);
                break;
            case "tel":
                item.setPhoneNumber(data);
                break;
            case "addr1":
                item.setAddress(data);
                break;
        }
        tagName = "";
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName){
            case "item":
                items.add(item);
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName){
            case "item":
                item = new LocationItem();
                break;
        }
        tagName = localName;
    }

    public ArrayList<LocationItem> getItems() {
        return items;
    }
}
