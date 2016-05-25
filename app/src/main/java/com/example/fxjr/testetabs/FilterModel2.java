package com.example.fxjr.testetabs;

import android.util.Log;

/**
 * Created by FranciscoJunior on 20/05/2016.
 */
public class FilterModel2 {

    private static final String TAG = "FilterModel2";

    CharSequence name = "";

    final String GROUP_CRYPT_FILTER = "(_group = '*' or _group in (?))";
    final String CAPACITY_CRYPT_FILTER = "Cast(capacity as integer) between ";

    boolean groups[] = new boolean[6];
    boolean groupsFilterChanged;
    String groupsFilterCached = "";

    int capacityMin = 1;
    int capacityMax = 11;

    public String getGroupsQuery() {


        if (groupsFilterChanged) {
            StringBuilder result = new StringBuilder();


            for (int i = 0; i < groups.length; i++) {
                if (groups[i])
                    result.append("'").append(i + 1).append("',");

            }

            // Remove last comma, if any.
            int commaIndex = result.lastIndexOf(",");

            if (commaIndex != -1) {
                result.deleteCharAt(commaIndex);
                groupsFilterCached = GROUP_CRYPT_FILTER.replace("?", result.toString());
            } else {
                groupsFilterCached = "";
            }
        }

        return groupsFilterCached;
    }


    public void setGroup(CharSequence text, boolean checked) {
        groups[Integer.parseInt(text.toString()) - 1] = checked;
        groupsFilterChanged = true;
    }


    public void setName(CharSequence name) {

        if (!name.equals(this.name))
            this.name = name;

    }

    public String getNameFilterQuery() {
        // TODO Auto-generated method stub

        if (name.length() > 0)
            return " and lower(Name) like '%" + name + "%'";
        else
            return "";

    }

    public String getCryptFilterQuery() {

        StringBuilder result = new StringBuilder();

        String groupsQuery = getGroupsQuery();

        // Groups processing
        if (groupsQuery.length() > 0) {
            result.append(" and ");
            result.append(groupsQuery);

        }


        // Capacity processing

        result.append(" and ");
        result.append(CAPACITY_CRYPT_FILTER).append(capacityMin).append(" AND ").append(capacityMax);


        Log.d(TAG, "getCryptFilterQuery() returned: " + result);

        return result.toString();


    }

    public String getLibraryFilterQuery() {
        return "";
    }

    public void setCapacityMin(int progress) {
        capacityMin = progress;

    }

    public void setCapacityMax(int progress) {
        capacityMax = progress;
    }
}
