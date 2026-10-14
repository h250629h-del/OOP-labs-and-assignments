import java.util.*;
class Box<T> { private T content; Box(T content){this.content=content;} T get(){return content;} void set(T value){content=value;} }
class Pair<K,V> { private final K key; private final V value; Pair(K k,V v){key=k;value=v;} public String toString(){return key+"="+value;} }
public class Main {
    static <T> void printAll(List<T> items){for(T item:items)System.out.print(item+" ");System.out.println();}
    static <T extends Comparable<T>> T maxOf(List<T> items){if(items.isEmpty())throw new IllegalArgumentException("List cannot be empty");T best=items.get(0);for(T x:items)if(x.compareTo(best)>0)best=x;return best;}
    public static void main(String[] args) {
        Box<String> sb=new Box<>("Hello");Box<Integer> ib=new Box<>(42);
        System.out.println(sb.get()+" "+(ib.get()+1));System.out.println(new Pair<>("age",21));
        printAll(List.of(1,2,3));printAll(List.of("a","b"));
        System.out.println("Max number: "+maxOf(List.of(3,9,4)));
        System.out.println("Max word: "+maxOf(List.of("pear","apple","zebra")));

        List<String> names=new ArrayList<>(List.of("Zodwa","Alice","Moyo","Alice"));
        System.out.println("List: "+names+" size="+names.size());
        names.remove("Moyo");Collections.sort(names);System.out.println("Sorted: "+names);
        Set<String> unique=new TreeSet<>(names);System.out.println("Set: "+unique);
        Map<String,Integer> marks=new HashMap<>();marks.put("Tino",78);marks.put("Rufaro",91);marks.put("Tino",82);marks.putIfAbsent("Nomsa",65);
        System.out.println("Tino: "+marks.get("Tino"));System.out.println("Has Nomsa? "+marks.containsKey("Nomsa"));
        for(Map.Entry<String,Integer> e:new TreeMap<>(marks).entrySet())System.out.println(e.getKey()+" -> "+e.getValue());
        String text="to be or not to be";Map<String,Integer> freq=new TreeMap<>();
        for(String word:text.split(" "))freq.merge(word,1,Integer::sum);
        System.out.println("Frequency: "+freq);
        Deque<Integer> stack=new ArrayDeque<>();stack.push(1);stack.push(2);stack.push(3);System.out.println("Stack pop: "+stack.pop());
        Queue<String> queue=new ArrayDeque<>();queue.offer("first");queue.offer("second");System.out.println("Queue poll: "+queue.poll());
        List<Integer> values=new ArrayList<>(List.of(1,2,3,4,5,6));values.removeIf(n->n%2==0);System.out.println("Odd values: "+values);
    }
}
