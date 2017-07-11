package com.example.ateg.flooringmaster;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressFragment extends Fragment{

    public static final String EXTRA_ADDRESS_ID = "com.example.ateg.flooringmaster.address_id";

    private Address address;

    public static AddressFragment newInstance(Integer id) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_ADDRESS_ID, id);

        AddressFragment addressFragment = new AddressFragment();
        addressFragment.setArguments(arguments);
        return addressFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);

        Integer id = (Integer) getArguments().getSerializable(EXTRA_ADDRESS_ID);
        address = null;
    }

    public class

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflator.inflate(R.layout.fragment_crime, parent, false);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            Activity activity = getActivity();
            if (activity != null) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        titleField = (EditText) view.findViewById(R.id.crime_title);
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence inputString, int start, int count, int after) {
                // This is supposed to be blank.
            }

            @Override
            public void onTextChanged(CharSequence inputString, int start, int before, int count) {
                crime.setTitle(inputString.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Blank by intent.  (Get it?)
            }
        });

        dateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(crime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        timeButton = (Button) view.findViewById(R.id.crime_time);
        updateTime();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(crime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fragmentManager, DIALOG_TIME);
            }
        });

        solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crimes solved state.
                crime.setSolved(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            long dateTime = data.getLongExtra(TimePickerFragment.EXTRA_MILLS, new Date().getTime());
            Date incomingDate = new Date(dateTime);

            updateCrimeDate(incomingDate);

        } else if (requestCode == REQUEST_TIME) {
            long dateTime = data.getLongExtra(TimePickerFragment.EXTRA_MILLS, new Date().getTime());

            updateCrimeTime(dateTime);
        }

        updateDate();
        updateTime();
    }

    private void updateCrimeDate(Date incomingDate) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(crime.getDate());

        Calendar incomingCalendar = Calendar.getInstance();
        incomingCalendar.setTime(incomingDate);

        int year = incomingCalendar.get(Calendar.YEAR);
        int month = incomingCalendar.get(Calendar.MONTH);
        int day = incomingCalendar.get(Calendar.DAY_OF_MONTH);

        currentCalendar.set(Calendar.DAY_OF_MONTH, day);
        currentCalendar.set(Calendar.YEAR, year);
        currentCalendar.set(Calendar.MONTH, month);

        crime.setDate(new Date(currentCalendar.getTimeInMillis()));
    }

    private void updateCrimeTime(int hours, int minutes) {
        Date crimeDate = crime.getDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        crime.setDate(calendar.getTime());
    }

    private void updateCrimeTime(long dateTime) {
        Calendar incomingDateTime = Calendar.getInstance();
        incomingDateTime.setTimeInMillis(dateTime);

        int hours = incomingDateTime.get(Calendar.HOUR_OF_DAY);
        int minutes = incomingDateTime.get(Calendar.MINUTE);

        Date crimeDate = crime.getDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        crime.setDate(calendar.getTime());
    }

    private void updateDate() {
        if (crime == null){
            Toast.makeText(getActivity(), "Crime null.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (crime.getDate() == null){
            Toast.makeText(getActivity(), "Crime Date Null.", Toast.LENGTH_SHORT).show();
            return;
        }

        dateButton.setText(crime.getDate().toString());
    }

    private void updateTime() {

        String timeString = getTimeString(crime.getDate());

        timeButton.setText(timeString);
    }

    @NonNull
    public static String getTimeString(Date crimeDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY) % 12;
        int minute = calendar.get(Calendar.MINUTE);
        int am_pm = calendar.get(Calendar.AM_PM);

        hourOfDay = (hourOfDay == 0) ? hourOfDay = 12 : hourOfDay;

        String am_pm_string;

        if (am_pm == Calendar.AM) {
            am_pm_string = "AM";
        } else if (am_pm == Calendar.PM) {
            am_pm_string = "PM";
        } else {
            am_pm_string = "un";
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(2);

        return hourOfDay + ":" + numberFormat.format(minute) + " " + am_pm_string;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

}
