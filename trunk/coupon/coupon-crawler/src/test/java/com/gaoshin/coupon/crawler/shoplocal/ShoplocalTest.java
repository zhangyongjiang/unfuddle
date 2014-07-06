package com.gaoshin.coupon.crawler.shoplocal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ShoplocalTest {
    private List<String> getCats() throws Exception {
        List<String> cats = new ArrayList<String>();
        
        FileReader fr = new FileReader("./src/main/resources/cats.tsv");
        BufferedReader br = new BufferedReader(fr);
        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            String[] items = line.split("\t");
            if(!"null".equals(items[3])) {
                continue;
            }
            cats.add(items[1]);
        }
        
        return cats;
    }
    
    public void getAllStores() throws Exception {
        List<String> cats = getCats();
        ShoplocalCrawler crawler = new ShoplocalCrawler();
        FileWriter fw = new FileWriter("./target/stores.tsv");
        FileReader fr = new FileReader("./src/main/resources/uscities.txt");
        BufferedReader br = new BufferedReader(fr);
        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            String[] items = line.split(",");
            int index = 0;
            for(String categoryId : cats) {
                int total = crawler.getStore(items[0], items[1], categoryId, fw);
                System.out.println("found " + total + " stores in " + line + " in category " + categoryId);
                if(index == 0 && total == 0)
                    break;
                index++;
            }
        }
        
        fw.close();
        br.close();
    }

    @Test
    public void getAllStoresV2() throws Exception {
        ShoplocalCrawler crawler = new ShoplocalCrawler();
        FileWriter fw = new FileWriter("./target/stores-v2.tsv");
        FileReader fr = new FileReader("./src/main/resources/uscities.txt");
        BufferedReader br = new BufferedReader(fr);
        boolean start = false;
        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            if(!start) {
                if(line.equals("WINDSOR,PA"))
                    start = true;
                System.out.println("skip " + line);
                continue;
            }
            String[] items = line.split(",");
            try {
                int total = crawler.getStoresV2(items[0], items[1], fw);
                System.out.println("found " + total + " stores in " + line);
            }
            catch (Exception e) {
                e.printStackTrace();
                fw.write("ERROR " + " in " + line + "\n");
                System.out.println("ERROR " + " in " + line);
            }
        }
        
        fw.close();
        br.close();
    }
}
