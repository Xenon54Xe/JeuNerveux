package com.example.app.utils;

public class UtilsTester {

    public static void main(String[] args) {

        // MAKE a test for LoopList
        // Test LoopList
        ILoopList<Integer> loopList = new LoopList<>();
        for (int i = 0; i < 10; i++) {
            loopList.add(i);
        }
        System.out.println("LoopList initial: " + loopList);
        System.out.println("size: " + loopList.size());
        System.out.println("get(3): " + loopList.get());
        System.out.println("contains(5): " + loopList.contains(5));
        loopList.remove(0);
        System.out.println("after remove 0: " + loopList);
        loopList.add(100);
        System.out.println("after add 100: " + loopList);
        for (int i = 0; i < loopList.size(); i++) {
            System.out.print(loopList.get(true) + " ");
        }
        System.out.println();
        loopList.clear();
        System.out.println("after clear isEmpty: " + loopList.isEmpty());

        // MAKE a test to test ArrayList
//        List<Integer> iList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            iList.add(i);
//        }
//        System.out.println(iList);
//        // test remove
//        for (int i = 0; i < 50; i++) {
//            iList.remove(i);
//        }
//        System.out.println(iList);
//        // test get
//        for (int i = 0; i < iList.size(); i++) {
//            System.out.print(iList.get(i) + " ");
//        }
//        System.out.println();
//        // test contains
//        System.out.println(iList.contains(25)); // false
//        System.out.println(iList.contains(75)); // true
//        // test size
//        System.out.println(iList.size()); // 50
//        // test equality between two lists
//        List<Integer> iList2 = new ArrayList<>();
//        for (int i = 50; i < 100; i++) {
//            iList2.add(i);
//        }
//        System.out.println(iList.equals(iList2)); // true
//        iList2.add(100);
//        System.out.println(iList.equals(iList2)); // false
//        // test clear
//        iList.clear();
//        System.out.println(iList.isEmpty()); // true
//        // test toString
//        System.out.println(iList); // []
//        // test addAll
//        Integer[] arr = new Integer[]{1,2,3,4,5};
//        iList.addAll(arr);
//        System.out.println(iList); // [1, 2, 3, 4, 5]

//        // ILoopList
//        ILoopList<Integer> iLinkedList = new LoopList<>();
//        for (int i = 0; i < 100; i++) {
//            iLinkedList.add(i);
//        }
//        System.out.println(iLinkedList);


//        // ID tester
//        EntityGroup group = new EntityGroup(new GameCanvas());
//        System.out.println(group.ID);
//        group = new EntityGroup(new GameCanvas());
//        System.out.println(group.ID);
//        group = new EntityGroup(new GameCanvas());
//        System.out.println(group.ID);
//        group = new EntityGroup(new GameCanvas());
//        System.out.println(group.ID);

//        try {
//            // loadFile test
//            InputStream is = FileUtils.loadFile("test.txt");
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String line = br.readLine();
//            System.out.println(line);
//            // loadFile test2
//            is = FileUtils.loadFile("test2.txt");
//            br = new BufferedReader(new InputStreamReader(is));
//            line = br.readLine();
//            System.out.println(line);
//
//            // find resources (base)
//            List<String> resources = FileUtils.listResources("maps");
//            System.out.println(resources);
//            // find resources (runtime)
//            resources = FileUtils.listUserFiles("maps");
//            System.out.println(resources);
//
//            // find all resources
//            resources = FileUtils.listAllResources("maps");
//            System.out.println(resources);
//
//        }catch (IOException e){
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
    }
}
