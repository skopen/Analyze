 public class Ticker
{
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String MIN = "MIN";
    public static final String MAX = "MAX";

    public static final double DROP_ALERT = 0.10;
    public static final double RISE_ALERT = 0.10;

    public String date;
    public int time;
    public double price;
    public String trend;
    public String inflection;
    public double capital = 100;
    public String localMinMax = null;

    public Ticker(String date, int time, double price)
    {
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public void print()
    {
        System.out.println("Date:" + date + ", Price:" + price + ", Trend:" + trend + ", Capital:" + capital);
    }

}