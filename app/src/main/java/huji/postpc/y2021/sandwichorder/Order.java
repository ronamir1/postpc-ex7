package huji.postpc.y2021.sandwichorder;

public class Order {
    String name;
    double pickles;
    boolean hummus;
    boolean tahini;
    String comment;
    String status;
    final static String WAITING = "waiting";
    final static String IN_PROGRESS = "in progress";
    final static String READY = "ready";
    final static String DONE = "done";


    Order(String name, double pickles, boolean hummus, boolean tahini, String comment, String status){
        this.name = name;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
        this.status = status;
    }

    Order(){

    }
    public String getName(){return name;}
    public String getComment(){return comment;}
    public String getStatus(){return status;}
    public double getPickles(){return pickles;}
    public boolean getHummus(){return hummus;}
    public boolean getTahini(){return tahini;}


    public void setName(String name){this.name = name;}
    public void setComment(String comment){this.comment = comment;}
    public void setStatus(String status){this.status = status;}
    public void setPickles(int pickles){this.pickles = pickles;}
    public void setHummus(boolean hummus){this.hummus = hummus;}
    public void setTahini(boolean tahini){this.tahini = tahini;}




}
