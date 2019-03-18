package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.*;
import java.util.Map.Entry;

public class GroceryReporter {
    private final String originalFileText;
    private List<Item> itemList;
    private List<ItemRecord> recordList;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
        ItemParser parser = new ItemParser();
        itemList = parser.parseItemList(originalFileText);
        recordList = new ArrayList<>();
        for(Item item : itemList){
            ItemRecord itemRecord = null;
            if(item != null) {
                itemRecord = new ItemRecord(item.getName());
                if(!recordList.contains(itemRecord)){
                    itemRecord.format(itemList, item.getName());
                    recordList.add(itemRecord);
                }
            }

        }
    }

    @Override
    public String toString() {
        String result = "";
        for(ItemRecord record : recordList){
            result += record;
        }
        return result;
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


        public void format(List<Item> list, String name){
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
            String result = "";
            String priceSeparator = "-------------\t\t-------------\n";
            if(!name.equals("Error")) {
                String heading = "name: "+getFormatedName()+"\t\tseen: "+ itemFrequency + " times\n";
                result = heading;
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
            else{

                String heading = name+"\t\t\t\tseen: "+ itemFrequency + " times\n";
                result = heading;
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
            return String.format("%7s", Character.toUpperCase(name.charAt(0)) + name.substring(1));
        }
    }
}
