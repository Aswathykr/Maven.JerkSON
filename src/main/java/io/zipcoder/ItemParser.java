package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    private final String delimiterPattern = "[:@;!\\^\\%\\*]";
    private final String toMatchPattern =
            "naMe" + delimiterPattern+"(\\w*)" + delimiterPattern +
            "price" + delimiterPattern+ "(\\d*.?\\d*)" + delimiterPattern +
            "type" + delimiterPattern + "(\\w+)" + delimiterPattern +
            "expiration" + delimiterPattern + "(\\d{1,2}/\\d{1,2}/\\d{2,4})##";

    public List<Item> parseItemList(String valueToParse)  {

        List<Item> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(toMatchPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(valueToParse);
        while(matcher.find()) {
            try {
                list.add(parseSingleItem(valueToParse.substring(matcher.start(), matcher.end())));
            }catch(ItemParseException ex){
                list.add(new Item("Error",0.0,null, null));
            }
        }
        return list;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {


        Pattern pattern = Pattern.compile(toMatchPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(singleItem);
        matcher.find();
        Item item = null;
        try {

            String name = matcher.group(1).toLowerCase();
            if(name.isEmpty())
                throw new ItemParseException();
            name = name.replace('0','o');
            item = new Item(name, Double.parseDouble(matcher.group(2)),
                    matcher.group(3).toLowerCase(),
                    matcher.group(4).toLowerCase());
        }catch (Exception ex){
            throw new ItemParseException();
        }
        return item;
    }
}
