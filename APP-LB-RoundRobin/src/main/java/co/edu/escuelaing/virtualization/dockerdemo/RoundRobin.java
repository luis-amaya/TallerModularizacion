package co.edu.escuelaing.virtualization.dockerdemo;

import java.util.ArrayList;

public class RoundRobin {
    private ArrayList<String> loginURIList;
    private int currentIndex = 0;

    public void LoginServices() {

        loginURIList = new ArrayList<>();
        loginURIList.add(
                "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo");
        loginURIList.add("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo");
        loginURIList.add("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=IBM&apikey=demo");
    }

    public String getLoginServiceURI() {
        String currentService = loginURIList.get(currentIndex);
        currentIndex = (currentIndex + 1) % loginURIList.size();
        return currentService;
    }
}
