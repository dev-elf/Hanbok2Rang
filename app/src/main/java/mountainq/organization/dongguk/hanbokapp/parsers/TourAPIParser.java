package mountainq.organization.dongguk.hanbokapp.parsers;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mountainq.organization.dongguk.hanbokapp.datas.LocationItem;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class TourAPIParser {

    /**
     * 들어온 InputStream에 대해서 XML을 리스트 형식으로 반환
     * @param is
     * @return
     */
    public ArrayList<LocationItem> parse(InputStream is){
        ArrayList<LocationItem> items = null;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        TourAPIHandler handler = new TourAPIHandler();
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
            items = handler.getItems();
        } catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }
}
