package com.antelope.af.utilities.testpparameters;

public class EnumUtilities {

    public enum CRMNavigtorOptions {
        GENERAL_INFORMATION, EDIT_INFORMATION, COMPLIANCE, FINANCIAL_TRANSACTIONS, CREDIT, TRADING_ACCOUNTS, CALLBACKS, TASKS, CHANGE_CRDENTIALS, TRACKING, USER_ACTIVITY_LOG, ACTIVITY_LOG, NINJA, DUPLICATES;

        public String getName() {
            return this.name().replaceAll("_", " ");
        }
    }
}
