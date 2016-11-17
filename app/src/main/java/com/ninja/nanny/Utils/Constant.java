package com.ninja.nanny.Utils;

/**
 * Created by Administrator on 10/19/2016.
 */

public class Constant {
    public static final String PREF_NAME = "Nanny";
    public static final String PREF_KEY_IS_INIT_CONFIG = "is_init_config";
    public static final String PREF_KEY_MINIMAL_AMOUNT_PER_DAY = "MinimalAmountPerDay";
    public static final String PREF_KEY_SALARY_DATE = "SalaryDate";
    public static final String PREF_KEY_MONTHLY_INCOME = "MonthlyIncome";
    public static final String PREF_KEY_USED_SALARY = "UsedSalary";
    public static final String PREF_KEY_TOLERANCE_DAYS = "ToleranceDays";
    public static final String PREF_KEY_TOLERANCE_PERCENT = "TolerancePercent";
    public static final String PREF_KEY_SMS_TIMESTAMP = "sms_timestamp";

    public static final String JSON_NAME = "name";
    public static final String JSON_TYPE = "type";
    public static final String JSON_TRANSACTION = "transaction";
    public static final String JSON_SPENDING = "spending";
    public static final String JSON_INCOME = "income";

    public static final String FRAGMENT_HOME = "home";
    public static final String FRAGMENT_BANK = "bank";
    public static final String FRAGMENT_WISH = "wish";
    public static final String FRAGMENT_PAYMENT = "payment";
    public static final String FRAGMENT_TRANSACTION = "transaction";
    public static final String FRAGMENT_SETTING = "setting";
    public static final String FRAGMENT_ADVICE = "advice";
    public static final String FRAGMENT_ADD_BANK = "add_bank";
    public static final String FRAGMENT_EDIT_BANK = "edit_bank";
    public static final String FRAGMENT_TRANSACTION_DETAIL = "transaction_detail";
    public static final String FRAGMENT_NEW_WISH = "new_wish";
    public static final String FRAGMENT_EDIT_WISH = "edit_wish";
    public static final String FRAGMENT_VIEW_WISH = "view_wish";
    public static final String FRAGMENT_NEW_PAYMENT = "new_payment";
    public static final String FRAGMENT_EDIT_PAYMENT = "edit_payment";
    public static final String FRAGMENT_EXPENSE_AS_PAYED = "expense_as_payed";
    public static final String FRAGMENT_MONTHLY_SAVINGS = "monthly_savings";
    public static final String FRAGMENT_CALENDAR_PAYMENT = "calendar_payment";

    public static final String strBankJsonData = "[" +
            "{" +
                "\"name\":\"EmiratesNBD\", " +
                "\"type\":\"TYPE 1\", " +
                "\"transaction\":{" +
                    "\"spending\":[" +
                        "\"Purchase of xxx with Debit Card ending 1736 at xxx.\", " +
                        "\"Cash Withdrawal of xxx with Debit Card ending 1736 at, xxx.\"" +
                    "]," +
                    "\"income\":[" +
                        "\"xxx has been received as TELEGRAHPHIC TRANSFER.\"" +
                    "]" +
                "}" +
            "}," +

            "{" +
                "\"name\":\"ADIB\", " +
                "\"type\":\"TYPE 2\", " +
                "\"transaction\":{" +
                    "\"spending\":[" +
                        "\"Purchase of xxx with Debit Card ending 1736 at xxx\", " +
                        "\"Cash Withdrawal of xxx with Debit Card ending 1736 at, xxx\"" +
                    "]," +
                    "\"income\":[" +
                        "\"xxx has been received as TELEGRAHPHIC TRANSFER.\"" +
                    "]" +
                "}" +
            "}," +

            "{" +
                "\"name\":\"HSBC\", " +
                "\"type\":\"TYPE 3\", " +
                "\"transaction\":{" +
                    "\"spending\":[" +
                        "\"Purchase of xxx with Debit Card ending 1736 at xxx\", " +
                        "\"Cash Withdrawal of xxx with Debit Card ending 1736 at, xxx\"" +
                    "]," +
                    "\"income\":[" +
                        "\"xxx has been received as TELEGRAHPHIC TRANSFER.\"" +
                    "]" +
                "}" +
            "}," +

            "{" +
                "\"name\":\"UNB\", " +
                "\"type\":\"TYPE 4\", " +
                "\"transaction\":{" +
                    "\"spending\":[" +
                        "\"Purchase of xxx with Debit Card ending 1736 at xxx\", " +
                        "\"Cash Withdrawal of xxx with Debit Card ending 1736 at, xxx\"" +
                    "]," +
                    "\"income\":[" +
                        "\"xxx has been received as TELEGRAHPHIC TRANSFER.\"" +
                    "]" +
                "}" +
            "}" +

            "]";

}