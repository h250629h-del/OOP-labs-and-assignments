import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// Singleton pattern: one shared configuration object.
final class AppConfig {
    private static final AppConfig INSTANCE=new AppConfig();
    private AppConfig(){}
    static AppConfig getInstance(){return INSTANCE;}
    String institution(){return "University Library";}
}

// Factory pattern: create a notification without exposing construction details.
interface Notification { void send(String message); }
class EmailNotification implements Notification { public void send(String message){System.out.println("Email: "+message);} }
class SmsNotification implements Notification { public void send(String message){System.out.println("SMS: "+message);} }
class NotificationFactory {
    static Notification create(String type){
        return switch(type.toLowerCase(Locale.ROOT)){
            case "email"->new EmailNotification();
            case "sms"->new SmsNotification();
            default->throw new IllegalArgumentException("Unknown notification type: "+type);
        };
    }
}

// Strategy pattern: the checkout can swap its pricing behaviour.
interface DiscountStrategy { double apply(double price); }
class Checkout {
    private DiscountStrategy strategy;
    Checkout(DiscountStrategy strategy){this.strategy=strategy;}
    void setStrategy(DiscountStrategy strategy){this.strategy=strategy;}
    double total(double price){return strategy.apply(price);}
}

// Observer pattern: listeners are notified when a grade is published.
interface GradeListener { void onGradePublished(String course,int mark); }
class GradeBook {
    private final List<GradeListener> listeners=new ArrayList<>();
    void subscribe(GradeListener listener){listeners.add(listener);}
    void publish(String course,int mark){for(GradeListener listener:listeners)listener.onGradePublished(course,mark);}
}

class ItemNotAvailableException extends Exception { ItemNotAvailableException(String m){super(m);} }
class MemberLimitExceededException extends Exception { MemberLimitExceededException(String m){super(m);} }

abstract class LibraryItem {
    private final String id,title; private boolean available=true;
    protected LibraryItem(String id,String title){if(id==null||id.isBlank()||title==null||title.isBlank())throw new IllegalArgumentException("Item id and title are required");this.id=id.trim();this.title=title.trim();}
    public abstract int loanPeriodDays();
    String getId(){return id;} String getTitle(){return title;} boolean isAvailable(){return available;}
    void setAvailable(boolean value){available=value;}
    String type(){return getClass().getSimpleName();}
    public String toString(){return type()+" ["+id+"] "+title+" ("+(available?"available":"on loan")+")";}
}
class Book extends LibraryItem { Book(String id,String title){super(id,title);} public int loanPeriodDays(){return 14;} }
class Dvd extends LibraryItem { Dvd(String id,String title){super(id,title);} public int loanPeriodDays(){return 7;} }
class Member {
    private final String id,name; private final Set<String> loanedItemIds=new LinkedHashSet<>();
    Member(String id,String name){if(id==null||id.isBlank()||name==null||name.isBlank())throw new IllegalArgumentException("Member id and name are required");this.id=id.trim();this.name=name.trim();}
    String getId(){return id;} String getName(){return name;} Set<String> getLoanedItemIds(){return Collections.unmodifiableSet(loanedItemIds);}
    int loanCount(){return loanedItemIds.size();}
    void addLoan(String itemId){loanedItemIds.add(itemId);} void removeLoan(String itemId){loanedItemIds.remove(itemId);}
    public String toString(){return id+" - "+name+" (active loans: "+loanCount()+")";}
}
class Loan {
    final String memberId,itemId; final LocalDate borrowedOn,dueOn; LocalDate returnedOn;
    Loan(String memberId,LibraryItem item){this(memberId,item.getId(),LocalDate.now(),LocalDate.now().plusDays(item.loanPeriodDays()),null);}
    Loan(String memberId,String itemId,LocalDate borrowedOn,LocalDate dueOn,LocalDate returnedOn){this.memberId=memberId;this.itemId=itemId;this.borrowedOn=borrowedOn;this.dueOn=dueOn;this.returnedOn=returnedOn;}
    boolean isOverdue(){return returnedOn==null&&LocalDate.now().isAfter(dueOn);}
    public String toString(){return itemId+" borrowed by "+memberId+", due "+dueOn+(returnedOn==null?"":" (returned "+returnedOn+")");}
}

class Library {
    private final Map<String,LibraryItem> items=new LinkedHashMap<>();
    private final Map<String,Member> members=new LinkedHashMap<>();
    private final List<Loan> loans=new ArrayList<>();
    private final Path itemsFile=Path.of("library-items.csv"), membersFile=Path.of("library-members.csv"), loansFile=Path.of("library-loans.csv");

    Library(){load();}
    void addItem(LibraryItem item){if(items.putIfAbsent(item.getId(),item)!=null)throw new IllegalArgumentException("Item ID already exists");save();}
    void registerMember(Member member){if(members.putIfAbsent(member.getId(),member)!=null)throw new IllegalArgumentException("Member ID already exists");save();}
    void borrow(String memberId,String itemId)throws ItemNotAvailableException,MemberLimitExceededException{
        Member member=members.get(memberId);LibraryItem item=items.get(itemId);
        if(member==null)throw new IllegalArgumentException("Member not found: "+memberId);
        if(item==null)throw new IllegalArgumentException("Item not found: "+itemId);
        if(!item.isAvailable())throw new ItemNotAvailableException("Item is already on loan");
        if(member.loanCount()>=3)throw new MemberLimitExceededException("A member may borrow at most 3 items");
        item.setAvailable(false);member.addLoan(itemId);loans.add(new Loan(memberId,item));save();
        System.out.println("Borrowed successfully. Due date: "+loans.get(loans.size()-1).dueOn);
    }
    void giveBack(String memberId,String itemId){
        Member member=members.get(memberId);LibraryItem item=items.get(itemId);
        if(member==null||item==null||!member.getLoanedItemIds().contains(itemId))throw new IllegalArgumentException("No matching active loan found");
        item.setAvailable(true);member.removeLoan(itemId);
        for(int i=loans.size()-1;i>=0;i--){Loan l=loans.get(i);if(l.memberId.equals(memberId)&&l.itemId.equals(itemId)&&l.returnedOn==null){l.returnedOn=LocalDate.now();break;}}
        save();System.out.println("Return recorded.");
    }
    List<LibraryItem> searchByTitle(String text){return items.values().stream().filter(i->i.getTitle().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))).sorted(Comparator.comparing(LibraryItem::getTitle)).toList();}
    void listItems(){if(items.isEmpty())System.out.println("No items yet.");else items.values().forEach(System.out::println);}
    void listMembers(){if(members.isEmpty())System.out.println("No members yet.");else members.values().forEach(System.out::println);}
    void listOverdue(){List<Loan> overdue=loans.stream().filter(Loan::isOverdue).toList();if(overdue.isEmpty())System.out.println("No overdue items.");else overdue.forEach(System.out::println);}
    void listLoans(){if(loans.isEmpty())System.out.println("No loans yet.");else loans.stream().filter(l->l.returnedOn==null).forEach(System.out::println);}
    private void save(){
        try{
            List<String> itemLines=items.values().stream().map(i->i.type()+","+i.getId()+","+i.getTitle()+","+i.isAvailable()).toList();
            List<String> memberLines=members.values().stream().map(m->m.getId()+","+m.getName()).toList();
            List<String> loanLines=loans.stream().map(l->l.memberId+","+l.itemId+","+l.borrowedOn+","+l.dueOn+","+(l.returnedOn==null?"":l.returnedOn)).toList();
            Files.write(itemsFile,itemLines);Files.write(membersFile,memberLines);Files.write(loansFile,loanLines);
        }catch(IOException e){System.out.println("Could not save data: "+e.getMessage());}
    }
    private void load(){
        try{
            if(Files.exists(itemsFile))for(String line:Files.readAllLines(itemsFile)){if(line.isBlank())continue;String[] p=line.split(",",4);LibraryItem item=p[0].equals("Dvd")?new Dvd(p[1],p[2]):new Book(p[1],p[2]);item.setAvailable(Boolean.parseBoolean(p[3]));items.put(item.getId(),item);}
            if(Files.exists(membersFile))for(String line:Files.readAllLines(membersFile)){if(line.isBlank())continue;String[] p=line.split(",",2);members.put(p[0],new Member(p[0],p[1]));}
            if(Files.exists(loansFile))for(String line:Files.readAllLines(loansFile)){if(line.isBlank())continue;String[] p=line.split(",",-1);LocalDate returned=p.length>4&&!p[4].isBlank()?LocalDate.parse(p[4]):null;Loan l=new Loan(p[0],p[1],LocalDate.parse(p[2]),LocalDate.parse(p[3]),returned);
                loans.add(l);if(returned==null&&items.containsKey(p[1])&&!items.get(p[1]).isAvailable()&&members.containsKey(p[0]))members.get(p[0]).addLoan(p[1]);
            }
        }catch(Exception e){System.out.println("Saved data could not be fully loaded: "+e.getMessage());}
    }
}

public class Main {
    private static final Scanner SC=new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println(AppConfig.getInstance().institution());
        System.out.println("Singleton check: "+(AppConfig.getInstance()==AppConfig.getInstance()));
        NotificationFactory.create("email").send("Library system started");
        Checkout checkout=new Checkout(price->price);System.out.println("Regular total: "+checkout.total(200));
        checkout.setStrategy(price->price*0.90);System.out.println("Student total: "+checkout.total(200));
        GradeBook grades=new GradeBook();grades.subscribe((course,mark)->System.out.println("Portal: "+course+" = "+mark));grades.subscribe((course,mark)->{if(mark<50)System.out.println("Advisor alert: failed "+course);});grades.publish("OOP",72);grades.publish("Databases",45);

        Library library=new Library();
        // Add sample data only when the persisted files are empty.
        try{library.addItem(new Book("B001","Object Oriented Java"));}catch(IllegalArgumentException ignored){}
        try{library.addItem(new Dvd("D001","Java Tutorial DVD"));}catch(IllegalArgumentException ignored){}
        try{library.registerMember(new Member("M001","Tariro"));}catch(IllegalArgumentException ignored){}
        menu(library);
    }
    private static void menu(Library library){
        while(true){
            System.out.println("\\n--- LIBRARY MENU ---");
            System.out.println("1 Add book  2 Add DVD  3 Register member  4 Borrow item");
            System.out.println("5 Return item  6 Search title  7 List items  8 List members");
            System.out.println("9 List overdue  10 List active loans  0 Exit");
            System.out.print("Choose: ");String choice=SC.nextLine().trim();
            try{
                switch(choice){
                    case "1"->{System.out.print("Book ID: ");String id=SC.nextLine();System.out.print("Title: ");String title=SC.nextLine();library.addItem(new Book(id,title));System.out.println("Book added.");}
                    case "2"->{System.out.print("DVD ID: ");String id=SC.nextLine();System.out.print("Title: ");String title=SC.nextLine();library.addItem(new Dvd(id,title));System.out.println("DVD added.");}
                    case "3"->{System.out.print("Member ID: ");String id=SC.nextLine();System.out.print("Member name: ");String name=SC.nextLine();library.registerMember(new Member(id,name));System.out.println("Member registered.");}
                    case "4"->{System.out.print("Member ID: ");String m=SC.nextLine();System.out.print("Item ID: ");String i=SC.nextLine();library.borrow(m,i);}
                    case "5"->{System.out.print("Member ID: ");String m=SC.nextLine();System.out.print("Item ID: ");String i=SC.nextLine();library.giveBack(m,i);}
                    case "6"->{System.out.print("Title contains: ");String q=SC.nextLine();List<LibraryItem> found=library.searchByTitle(q);if(found.isEmpty())System.out.println("No matches.");else found.forEach(System.out::println);}
                    case "7"->library.listItems();
                    case "8"->library.listMembers();
                    case "9"->library.listOverdue();
                    case "10"->library.listLoans();
                    case "0"->{System.out.println("Data saved. Goodbye.");return;}
                    default->System.out.println("Choose a listed menu number.");
                }
            }catch(ItemNotAvailableException|MemberLimitExceededException|IllegalArgumentException e){System.out.println("Operation failed: "+e.getMessage());}
        }
    }
}
