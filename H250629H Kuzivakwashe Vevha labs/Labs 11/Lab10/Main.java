import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

record Learner(String name,int roll,double gpa) {
    String toCsv(){return name+","+roll+","+gpa;}
    static Learner fromCsv(String line){String[] p=line.split(",");return new Learner(p[0],Integer.parseInt(p[1]),Double.parseDouble(p[2]));}
}
record Employee(String name,String dept,double salary){}
public class Main {
    public static void main(String[] args) {
        Path path=Path.of("students.txt");
        List<String> lines=List.of("Tariro,101,3.8","Farai,102,3.1","Rudo,103,3.9");
        try {
            Files.write(path,lines);
            Files.writeString(path,"Kuda,104,3.2"+System.lineSeparator(),StandardOpenOption.APPEND);
            System.out.println("File written: "+path.toAbsolutePath());
            int count=0;
            for(String line:Files.readAllLines(path)){String[] p=line.split(",");System.out.printf("%-8s roll=%s gpa=%s%n",p[0],p[1],p[2]);count++;}
            System.out.println(count+" records read");
        } catch(IOException e){System.out.println("File error: "+e.getMessage());}

        Path csv=Path.of("learners.csv");
        List<Learner> learners=List.of(new Learner("Ada",1,3.9),new Learner("Kuda",2,3.2),new Learner("Busi",3,2.8),new Learner("Tariro",4,3.7),new Learner("Rudo",5,2.9));
        try {
            Files.write(csv,learners.stream().map(Learner::toCsv).toList());
            List<Learner> loaded=Files.readAllLines(csv).stream().filter(s->!s.isBlank()).map(Learner::fromCsv).toList();
            System.out.println("Loaded learners: "+loaded);
            Predicate<String> longWord=s->s.length()>4;
            Function<String,Integer> length=String::length;
            Consumer<String> shout=s->System.out.println(s.toUpperCase()+"!");
            Supplier<List<String>> maker=ArrayList::new;
            System.out.println(longWord.test("lambda")+" "+length.apply("stream"));shout.accept("hello");System.out.println("Empty list size: "+maker.get().size());

            List<Employee> staff=List.of(new Employee("Ada","IT",4200),new Employee("Ben","IT",3800),new Employee("Cara","HR",3000),new Employee("Dan","Finance",5000),new Employee("Eve","HR",3400));
            List<String> high=staff.stream().filter(e->e.salary()>=3800).map(Employee::name).sorted().toList();
            System.out.println("High earners: "+high);
            staff.stream().sorted(Comparator.comparingDouble(Employee::salary).reversed()).limit(2).forEach(e->System.out.println("Top: "+e.name()));
            double total=staff.stream().mapToDouble(Employee::salary).sum();
            double average=staff.stream().mapToDouble(Employee::salary).average().orElse(0);
            System.out.printf("Total %.0f, average %.1f%n",total,average);
            Map<String,Long> counts=staff.stream().collect(Collectors.groupingBy(Employee::dept,TreeMap::new,Collectors.counting()));
            System.out.println("Head count: "+counts);
            System.out.println("Anyone in Finance? "+staff.stream().anyMatch(e->e.dept().equals("Finance")));
            System.out.println("Names: "+staff.stream().map(Employee::name).collect(Collectors.joining(", ","[","]")));

            // Exercise: calculate line, word and character counts from a text file.
            Path report=Path.of("text-analysis.txt");Files.writeString(report,"Java streams are useful.\\nFiles can be read safely.\\n");
            String all=Files.readString(report);long words=Arrays.stream(all.trim().split("\\\\s+")).count();
            System.out.println("Text analysis: lines="+Files.readAllLines(report).size()+", words="+words+", characters="+all.length());

            // Exercise: create one report file per grade band.
            Map<String,List<Learner>> bands=loaded.stream().collect(Collectors.groupingBy(l->l.gpa()>=3.5?"A":l.gpa()>=3.0?"B":"C"));
            for(var entry:bands.entrySet())Files.write(Path.of("grade-"+entry.getKey()+".txt"),entry.getValue().stream().map(Learner::toCsv).toList());
            System.out.println("Grade-band reports written.");
        } catch(IOException e){System.out.println("I/O error: "+e.getMessage());}
    }
}
