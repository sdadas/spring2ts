package com.sdadas.spring2ts.examples.types.collections;

import com.sdadas.spring2ts.annotations.SharedModel;

import java.util.*;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class CollectionsObject {

    private String[] array;

    private List<String> list1;

    private ArrayList<String> list2;

    private LinkedList<String> list3;

    private Vector<String> vector;

    private Set<String> set1;

    private HashSet<String> set2;

    private TreeSet<String> set3;

    private LinkedHashSet<String> set4;

    private NavigableSet<String> set5;

    private SortedSet<String> set6;

    private Map<String, String> map1;

    private HashMap<String, String> map2;

    private LinkedHashMap<String, String> map3;

    private TreeMap<String, String> map4;

    private SortedMap<String, String> map5;

    private NavigableMap<String, String> map6;

    private Queue<String> queue1;

    private PriorityQueue<String> queue2;

    private ArrayDeque<String> queue3;

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public List<String> getList1() {
        return list1;
    }

    public void setList1(List<String> list1) {
        this.list1 = list1;
    }

    public ArrayList<String> getList2() {
        return list2;
    }

    public void setList2(ArrayList<String> list2) {
        this.list2 = list2;
    }

    public LinkedList<String> getList3() {
        return list3;
    }

    public void setList3(LinkedList<String> list3) {
        this.list3 = list3;
    }

    public Vector<String> getVector() {
        return vector;
    }

    public void setVector(Vector<String> vector) {
        this.vector = vector;
    }

    public Set<String> getSet1() {
        return set1;
    }

    public void setSet1(Set<String> set1) {
        this.set1 = set1;
    }

    public HashSet<String> getSet2() {
        return set2;
    }

    public void setSet2(HashSet<String> set2) {
        this.set2 = set2;
    }

    public TreeSet<String> getSet3() {
        return set3;
    }

    public void setSet3(TreeSet<String> set3) {
        this.set3 = set3;
    }

    public LinkedHashSet<String> getSet4() {
        return set4;
    }

    public void setSet4(LinkedHashSet<String> set4) {
        this.set4 = set4;
    }

    public NavigableSet<String> getSet5() {
        return set5;
    }

    public void setSet5(NavigableSet<String> set5) {
        this.set5 = set5;
    }

    public SortedSet<String> getSet6() {
        return set6;
    }

    public void setSet6(SortedSet<String> set6) {
        this.set6 = set6;
    }

    public Map<String, String> getMap1() {
        return map1;
    }

    public void setMap1(Map<String, String> map1) {
        this.map1 = map1;
    }

    public HashMap<String, String> getMap2() {
        return map2;
    }

    public void setMap2(HashMap<String, String> map2) {
        this.map2 = map2;
    }

    public LinkedHashMap<String, String> getMap3() {
        return map3;
    }

    public void setMap3(LinkedHashMap<String, String> map3) {
        this.map3 = map3;
    }

    public TreeMap<String, String> getMap4() {
        return map4;
    }

    public void setMap4(TreeMap<String, String> map4) {
        this.map4 = map4;
    }

    public SortedMap<String, String> getMap5() {
        return map5;
    }

    public void setMap5(SortedMap<String, String> map5) {
        this.map5 = map5;
    }

    public NavigableMap<String, String> getMap6() {
        return map6;
    }

    public void setMap6(NavigableMap<String, String> map6) {
        this.map6 = map6;
    }

    public Queue<String> getQueue1() {
        return queue1;
    }

    public void setQueue1(Queue<String> queue1) {
        this.queue1 = queue1;
    }

    public PriorityQueue<String> getQueue2() {
        return queue2;
    }

    public void setQueue2(PriorityQueue<String> queue2) {
        this.queue2 = queue2;
    }

    public ArrayDeque<String> getQueue3() {
        return queue3;
    }

    public void setQueue3(ArrayDeque<String> queue3) {
        this.queue3 = queue3;
    }
}
