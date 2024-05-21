package com.antelope.af.utilities.testpparameters;

public class UserParamsThreadLocal {

    public enum UsersParamsEnum {
        USER_NAME, GENERATED_EMAIL, PASSWORD, USER_ID, USER_LOGGED_IN, VISITED_CLIENT, USER_IS_TEST;

        public String getName() {
            return this.name().replaceAll("_", " ");
        }
    }

    public static boolean isUserTest(int userID) {
        return (Integer) TestParamsThreadPool.get(UsersParamsEnum.USER_IS_TEST.getName()) == userID;
    }

    public static void setUserAsMarkedToTest(int userID) {
        TestParamsThreadPool.put(UsersParamsEnum.USER_IS_TEST.getName(), userID);
    }

    public static boolean isClientVisited() {
        return (boolean) TestParamsThreadPool.get(UsersParamsEnum.VISITED_CLIENT.getName());
    }

    public static void setVisitedClient(boolean loggedIn) {
        TestParamsThreadPool.put(UsersParamsEnum.VISITED_CLIENT.getName(), loggedIn);
    }

    public static boolean isUserLoggedIn() {
        return (boolean) TestParamsThreadPool.get(UsersParamsEnum.USER_NAME.getName());
    }

    public static void setUserLoginStatus(boolean loggedIn) {
        TestParamsThreadPool.put(UsersParamsEnum.USER_NAME.getName(), loggedIn);
    }

    public static String getUserName() {
        return (String) TestParamsThreadPool.get(UsersParamsEnum.USER_NAME.getName());
    }

    public static void setUserName(String userName) {
        TestParamsThreadPool.put(UsersParamsEnum.USER_NAME.getName(), userName);
    }

    public static String getClientGeneratedEmail() {
        return (String) TestParamsThreadPool.get(UsersParamsEnum.GENERATED_EMAIL.getName());
    }

    public static void setClientGeneratedEmail(String email) {
        TestParamsThreadPool.put(UsersParamsEnum.GENERATED_EMAIL.getName(), email);
    }

    public static String getClientPassword() {
        return (String) TestParamsThreadPool.get(UsersParamsEnum.PASSWORD.getName());
    }

    public static void setClientPasswod(String password) {
        TestParamsThreadPool.put(UsersParamsEnum.PASSWORD.getName(), password);
    }

    public static int getUserID() {
        return (int) TestParamsThreadPool.get(UsersParamsEnum.USER_ID.getName());
    }

    public static void setUserID(int userID) {
        TestParamsThreadPool.put(UsersParamsEnum.USER_ID.getName(), userID);
    }
}
