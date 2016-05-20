package com.example.fxjr.testetabs;

/**
 * Created by FranciscoJunior on 20/05/2016.
 */
public class FilterModel2 {

    CharSequence name = "";

    final String GROUP_CRYPT_FILTER = "(_group = '*' or _group in (?))";

    boolean groups[] = new boolean[6];
    boolean groupsFilterChanged;
    String groupsFilterCached = "";

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

        if (groupsQuery.length() > 0) {
            result.append(" and ");
            result.append(groupsQuery);

        }



        return result.toString();


    }

    public String getLibraryFilterQuery() {
        return "";
    }
}
