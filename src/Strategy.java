import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Strategy {

    private Ticker[] prices = null;

    public void readData(boolean reverse)
    {
        try (
             //  BufferedReader br = new BufferedReader(new FileReader("data/1998-2000-S_P_500.csv"))
                // BufferedReader br = new BufferedReader(new FileReader("data/1998-2003-S_P_500.csv"))
                //BufferedReader br = new BufferedReader(new FileReader("data/historical-S_P_500.csv"))
                BufferedReader br = new BufferedReader(new FileReader("data/1997-2021-S_P_500.csv"))
              // BufferedReader br = new BufferedReader(new FileReader("data/2007-2012-S_P_500.csv"))
               //  BufferedReader br = new BufferedReader(new FileReader("data/TEST.csv"))
        )
        {

            ArrayList<Ticker> prices2 = new ArrayList<Ticker>();

            String line;
            while ((line = br.readLine()) != null) {
               // System.out.println(line);
                String[] tokens = line.split(",");
                Ticker t = new Ticker(tokens[0], Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]));
            //    t.print();
                prices2.add(t);
            }

            //ArrayList<Ticker> prices = new ArrayList<Ticker>(prices2.size());
            prices = new Ticker[prices2.size()];

            if (reverse) {
                int j = 0;
                for (int i = prices2.size() - 1; i >= 0; i--) {
                    prices[j++] = prices2.get(i);
                }
            }
                else
                {
                    for (int i = 0; i < prices2.size(); i++) {
                        prices[i] = prices2.get(i);
                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Ticker[] processData()
    {
        String trend = Ticker.UNKNOWN;
        int highNdx = -1;
        int lowNdx = -1;
        double high = Double.MIN_VALUE;
        double low = Double.MAX_VALUE;

        for (int i = 0; i < prices.length; i++)
        {
            if (trend == Ticker.UP && prices[i].price > high)
            {
                highNdx = i;
                high = prices[i].price;
            }

            if (trend == Ticker.DOWN && prices[i].price < low)
            {
                lowNdx = i;
                low = prices[i].price;
            }

            if (trend == Ticker.UNKNOWN)
            {
                if (prices[i].price < low)
                {
                    lowNdx = i;
                    low = prices[i].price;
                }

                if (prices[i].price > high)
                {
                    highNdx = i;
                    high = prices[i].price;
                }

                if (prices[i].price >= low*(1.0 + Ticker.DROP_ALERT))
                {
                    for (int j = 0; j <= i; j++)
                    {
                        prices[j].trend = Ticker.UP;
                    }
                    trend = Ticker.UP;
                }

                if (prices[i].price <= high*(1.0 - Ticker.DROP_ALERT))
                {
                    for (int j = 0; j <= i; j++)
                    {
                        prices[j].trend = Ticker.DOWN;
                    }
                    trend = Ticker.DOWN;
                }
            }

            prices[i].trend = trend;

            if (trend == Ticker.UP && prices[i].price <= high*(1.0 - Ticker.DROP_ALERT))
            {
                prices[i].inflection = Ticker.DOWN;
                prices[i].trend = Ticker.DOWN;
                trend = Ticker.DOWN;

                lowNdx = i;
                low = prices[i].price;

                prices[highNdx].localMinMax = Ticker.MAX;
            }
            else if (trend == Ticker.DOWN && prices[i].price >= low*(1.0 + Ticker.RISE_ALERT))
            {
                prices[i].inflection = Ticker.UP;
                prices[i].trend = Ticker.UP;
                trend = Ticker.UP;

                highNdx = i;
                high = prices[i].price;

                prices[lowNdx].localMinMax = Ticker.MIN;
            }
        }

        System.out.println("=======================================");
        int lastInflectionDownPoint = 0;

        for (int i = 1; i < prices.length; i++)
        {
            if (prices[i].inflection == Ticker.DOWN)
            {
                prices[i].capital = prices[i-1].capital * (prices[i].price/prices[i-1].price);
                lastInflectionDownPoint = i;
            }
            else if (prices[i].inflection == Ticker.UP)
            {
                prices[i].capital = prices[lastInflectionDownPoint].capital;
                //prices[i].capital = prices[lastInflectionDownPoint].capital * (prices[i].price/prices[lastInflectionDownPoint].price);
            }
            else if (prices[i].trend == Ticker.UP)
            {
                prices[i].capital = prices[i-1].capital * (prices[i].price/prices[i-1].price);
            }
            else if (prices[i].trend == Ticker.DOWN)
            {
                prices[i].capital = prices[lastInflectionDownPoint].capital;
            }
            else if (prices[i].trend == Ticker.UNKNOWN)
            {
                prices[i].capital = prices[i-1].capital * (prices[i].price/prices[i-1].price);
            }
        }

        return prices;

    }

    public static void main(String[] args)
    {
        Strategy runner = new Strategy();
        runner.readData(true);
        Ticker[] prices = runner.processData();

        for (int i = 0; i < prices.length; i++)
        {
            prices[i].print();
        }
    }
}
