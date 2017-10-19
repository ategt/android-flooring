package com.example.ateg.flooringmaster.model;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.ateg.flooringmaster.Address;
import com.google.common.base.Strings;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by ATeg on 10/13/2017.
 */

public class AddressTestUtilities {

    public static String caseRandomizer(final Random random, String input) {
        switch (random.nextInt(6)) {

            case 0:
                input = input;
                break;
            case 1:
                input = input.toLowerCase();
                break;
            case 2:
                input = input.toUpperCase();
                break;
            default:
                char[] charArray = input.toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    switch (random.nextInt(4)) {
                        case 1:
                            charArray[j] = Character.toLowerCase(charArray[j]);
                            break;
                        case 2:
                            charArray[j] = Character.toUpperCase(charArray[j]);
                            break;
                        case 3:
                            charArray[j] = Character.toTitleCase(charArray[j]);
                            break;
                        default:
                            charArray[j] = charArray[j];
                            break;
                    }

                    input = new String(charArray);
                }
        }

        return input;
    }

    public static Comparator<Address> sortByLastNameComparator() {
        return new Comparator<Address>() {
            @SuppressWarnings("Since15")
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public int compare(Address address1, Address address2) {
                int result = Strings.nullToEmpty(address1.getLastName()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getLastName()).toLowerCase());

                if (result == 0) {
                    result = Strings.nullToEmpty(address1.getFirstName()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getFirstName()).toLowerCase());
                }

                if (result == 0) {
                    if (Strings.isNullOrEmpty(address1.getCompany())
                            && !Strings.isNullOrEmpty(address2.getCompany())) {
                        return 1;
                    } else if (!Strings.isNullOrEmpty(address1.getCompany())
                            && Strings.isNullOrEmpty(address2.getCompany())) {
                        return -1;
                    }
                }

                if (result == 0) {
                    result = Strings.nullToEmpty(address1.getCompany()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getCompany()).toLowerCase());
                }

                if (result == 0) {
                    result = Integer.compare(address1.getId(), address2.getId());
                }

                return result;
            }
        };
    }
}