package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.*;
import java.util.Map.Entry;

public class GroceryReporter {
    private final String originalFileText;
    private Map<String, ItemRecord> recordMap;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
        ItemParser parser = new ItemParser();
        List<Item> itemList = parser.parseItemList(originalFileText);
        recordMap = new LinkedHashMap<>();
        for(Item item : itemList){
            if(item != null) {
                ItemRecord itemRecord = new ItemRecord(item.getName());
                itemRecord = recordMap.getOrDefault(itemRecord.name, new ItemRecord(item.getName()));
                itemRecord.incrementFrequency();
                itemRecord.addPrice(item.getPrice());
                recordMap.put(item.getName(), itemRecord);
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        Set<String> rocordNames = recordMap.keySet();
        for(String recordName : rocordNames){
            if(recordName != "Error")
            result += recordMap.get(recordName);
        }
        return result += recordMap.get("Error");
    }

    private class ItemRecord{
        private String name;
        private Integer itemFrequency;
        private Map<Double, Integer> priceMap;

        public Integer getItemFrequency() {
            return itemFrequency;
        }

        public ItemRecord(String name) {
            this.name = name;
            itemFrequency = 0;
            priceMap = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public void incrementFrequency(){
            itemFrequency++;
        }
        public void addPrice(Double price) {
            Integer frequency = priceMap.getOrDefault(price, null);
            if(frequency == null){
                priceMap.put(price, 1);
            }
            else {
                frequency = frequency.intValue() + 1;
                priceMap.put(price, frequency);
            }
        }
        public void populate(List<Item> list, String name){
            for(Item item : list) {
                if(item.getName().equals(name)){
                    itemFrequency ++;
                    Integer frequency = priceMap.getOrDefault(item.getPrice(), null);
                    if(frequency == null){
                        priceMap.put(item.getPrice(), 1);
                    }
                    else {
                        frequency = frequency.intValue() + 1;
                        priceMap.put(item.getPrice(), frequency);
                    }
                }
            }
        }

        @Override
        public String toString() {
            String priceSeparator = "-------------\t\t-------------\n";
            String heading = getFormatedName()+"\t\tseen: "+ itemFrequency + " times\n";
            String result = heading;
            if(!name.equals("Error")) {
                String separator = "=============\t\t=============\n";
                result += separator;
                int index = 0;
                Set<Double> prices = priceMap.keySet();
                for (Double key : prices) {
                    String price = "Price: 	 " + key + "\t\tseen: "
                            + priceMap.get(key) + " times\n" ;
                    result += price;
                    if(index < prices.size()-1 || prices.size() == 1)
                        result += priceSeparator;
                    index++;
                }
                result += "\n";
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ItemRecord)) return false;
            ItemRecord that = (ItemRecord) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String getFormatedName(){
            String result = "Error\t\t";
            if(!name.equals("Error")) {
                result = "name: ";
                result +=String.format("%7s", Character.toUpperCase(name.charAt(0)) + name.substring(1));
            }
            return result;
        }
    }
}
