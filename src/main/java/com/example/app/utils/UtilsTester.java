package com.example.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class UtilsTester {

    public static void main(String[] args) {

        try {
            // loadFile test
            InputStream is = FileUtils.loadFile("test.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            System.out.println(line);
            // loadFile test2
            is = FileUtils.loadFile("test2.txt");
            br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            System.out.println(line);

            // find resources (base)
            List<String> resources = FileUtils.listResources("maps");
            System.out.println(resources);
            // find resources (runtime)
            resources = FileUtils.listUserFiles("maps");
            System.out.println(resources);

            // find all resources
            resources = FileUtils.listAllResources("maps");
            System.out.println(resources);

        }catch (IOException e){
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
