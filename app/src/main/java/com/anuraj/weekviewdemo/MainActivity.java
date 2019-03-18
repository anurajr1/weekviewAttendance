package com.anuraj.weekviewdemo;

import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.anuraj.weekviewdemo.weekview.WeekView;
import com.anuraj.weekviewdemo.weekview.WeekViewEvent;

import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.anuraj.weekviewdemo.utils.AppConstants.DAY_VIEW;
import static com.anuraj.weekviewdemo.utils.AppConstants.DD_MMM_YYYY;
import static com.anuraj.weekviewdemo.utils.AppConstants.END;
import static com.anuraj.weekviewdemo.utils.AppConstants.EVENT_ADD_FAILURE_MESSAGE;
import static com.anuraj.weekviewdemo.utils.AppConstants.RALEWAY_LIGHT;
import static com.anuraj.weekviewdemo.utils.AppConstants.RALEWAY_REGULAR;
import static com.anuraj.weekviewdemo.utils.AppConstants.RALEWAY_SEMI_BOLD;
import static com.anuraj.weekviewdemo.utils.AppConstants.START;
import static com.anuraj.weekviewdemo.utils.AppConstants.WEEK_VIEW;

public class MainActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.ChangeBackgroundListener {

    // This is the counter for event count - can be removed after testing
    private static int count = 1;
    // To keep record of viewType being shown - Added by Muddassir
    private static int viewType;
    // This map is used to store the events
    HashMap<Integer, List<WeekViewEvent>> eventMap = new HashMap<>();
    // Typeface for text - Added by Muddassir
    Typeface ralewayLight, ralewayRegular, ralewaySemiBold;
    // Day view & Week view object
    private WeekView mWeekView;
    // For button background toggle
    private Button buttonWeekView;

    private SimpleDateFormat formatter;
    private Date[] startEndTime;

    String[] strOperator = {"Anuraj R","Ben johnson","Captain philip","Allen, Agnes","Blake, William","Crockett, Davy","Finney, Albert"
    ,"Golding, W","Han Shan","Jones, Norah","King, William","Kruk, John","Meir, Golda","Roth, Philip","West, Mae","Zola, Emile","Young, Neil"};


    WeekViewEvent event;
    List<WeekViewEvent> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize all the required components
        initComponents();

        //passing dummy data to populate events
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, HH:mm:ss yyyy");
//        String dateInString = "Mar 18, 01:00:00 2019";
//        String dateendString = "Mar 18, 01:15:00 2019";
//        Date date =null;
//        Date dateend =null;
//            try {
//                date = formatter.parse(dateInString);
//                dateend = formatter.parse(dateendString);
//                System.out.println(date);
//            }
//            catch (Exception   e){
//
//            }
//       addEvent(date,dateend,"Testing");





        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, HH:mm:ss yyyy");
        String nameMap = "01:30:00";
        String dateInString = "Mar 20,"+" "+ nameMap + " 2019";
        Date date =null;
        try {
            date = formatter.parse(dateInString);
        }
        catch (Exception   e){

        }
        //setting the time and add 15mins
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 15);
        //adding the event
        addEvent(date,cal.getTime(),"Testing");


    }

    // Initialize all the required components
    private void initComponents() {
        // Set the view type to Day view
        viewType = WEEK_VIEW;
        // Initialise the typefaces
        ralewayLight = Typeface.createFromAsset(getAssets(),
                RALEWAY_LIGHT); // Added by Muddassir
        ralewayRegular = Typeface.createFromAsset(getAssets(),
                RALEWAY_REGULAR); // Added by Muddassir
        ralewaySemiBold = Typeface.createFromAsset(getAssets(),
                RALEWAY_SEMI_BOLD); // Added by Muddassir

        // To record the button pressed - Added by Muddassir

        buttonWeekView = (Button) findViewById(R.id.action_week_view);
        buttonWeekView.setTypeface(ralewayRegular);
        formatter = new SimpleDateFormat(DD_MMM_YYYY, Locale.getDefault());

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.goToToday();
        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);
        // to toggle button
        mWeekView.setmBackgroundListener(this);
        // Setup start and end time of the calendar view - Added by Muddassir
//        mWeekView.setmEndMinute("24:00:00");
//        mWeekView.setmStartMinute("01:00:00");
        mWeekView.setOperatorNames(strOperator);
        //mWeekView.setOperatorLength(3);
        mWeekView.setEmptyViewClickListener(this); // Added by Muddassir
    }


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events;
        events = extractEvent(newMonth);
        return events;
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this, "Pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    // It will implement the swipe on the events to cancel it - Added by Muddassir
    public void onEventSwipe(WeekViewEvent event, RectF eventRect) {
        if (cancelEvent(event)) {
            Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        startEndTime = convertTime(time);
        {
            // This tries to add event - Added by Muddassir
            if (addEvent(startEndTime[0], startEndTime[1], "On Leave")) {
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                Toast.makeText(MainActivity.this, EVENT_ADD_FAILURE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    // It will implement the swipe on the empty view to add blank - Added by Muddassir
    public void onEmptyViewSwiped(Calendar time) {
        startEndTime = convertTime(time);
        if (addEvent(startEndTime[0], startEndTime[1], "")) {
            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
        }
    }

    // Extracts the event list of the specified month - Added by Muddassir
    private List<WeekViewEvent> extractEvent(int month) {
        List<WeekViewEvent> events = eventMap.get(month);
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }


    // Add event to the calendar - Added by Muddassir
    private boolean addEvent(Date startTime, Date endTime, String eventTitle) {
        Calendar currentDate = Calendar.getInstance();
        Date today = currentDate.getTime();
        Calendar startEventTime = Calendar.getInstance();
        startEventTime.setTime(startTime);
        int month = startTime.getMonth();
        Calendar endEventTime = Calendar.getInstance();
        endEventTime.setTime(endTime);
        events = extractEvent(month + 1);
        event = new WeekViewEvent(count, eventTitle, startEventTime, endEventTime);
        if (startTime.getTime() < today.getTime()) {
            event.setColor(getResources().getColor(R.color.event_color_past));
        } else {
            event.setColor(getResources().getColor(R.color.event_color_upcoming));
        }
        events.add(event);
        eventMap.put(month + 1, events);
        mWeekView.notifyDatasetChanged();
        return true;
    }

    @Override
    public void changeBackground() {
        viewType = DAY_VIEW;

    }

    // This will remove the event from calendar - Added by Muddassir
    private boolean cancelEvent(WeekViewEvent event) {
        final int month = event.getStartTime().getTime().getMonth();
        List<WeekViewEvent> eventList = extractEvent(month + 1);
        for (WeekViewEvent viewEvent : eventList) {
            if (event.getStartTime().getTime().getTime() == viewEvent.getStartTime().getTime().getTime()) {
                eventList.remove(viewEvent);
                eventMap.put(month + 1, eventList);
                mWeekView.notifyDatasetChanged();
                count--;
                return true;
            }
        }
        return false;
    }

    /**
     * Convert the given Calendar touch time and compute its slot
     * Added by Muddassir
     *
     * @param time time at which it it touched
     * @return Date[] of start and end time of the slot
     */
    private Date[] convertTime(Calendar time) {
        int startMinute = mWeekView.getmStartMinute();
        Date startTime = new Date();
        Date endTime = new Date();

        Date date = time.getTime();
        int hour = date.getHours();
        int minute = date.getMinutes();

        int minutes = hour * 60 + minute;
        minutes += startMinute;
        minute = minutes % 60;
        int buffer = minute % 15;

        long timeInMillis = date.getTime();
        timeInMillis /= 1000;
        timeInMillis = (timeInMillis / 60) - buffer + startMinute;
        date.setTime(timeInMillis * 60 * 1000);

        startTime.setTime(timeInMillis * 60 * 1000);
        timeInMillis = timeInMillis + 15;
        date.setTime(timeInMillis * 60 * 1000);

        endTime.setTime(timeInMillis * 60 * 1000);
        Date[] startNend = new Date[2];
        startNend[START] = startTime;
        startNend[END] = endTime;
        return startNend;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
