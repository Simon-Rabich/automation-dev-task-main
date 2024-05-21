package com.antelope.af.utilities.testpparameters;

import com.antelope.af.utilities.WebElementUtils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Concurrent Linked Queue for returning the next user in line for login
 * purposes.
 *
 * @author Fadi Zaboura
 */
public class UsersRepository {

    static int USERS_COUNT;
    private static int start;
    static ConcurrentLinkedQueue<Integer> q1;


    private static ConcurrentLinkedQueue<Integer> initUserRepo() {
        if (q1 == null) {
            ArrayList<Integer> coll = new ArrayList<Integer>();
            for (int i = start; i <= USERS_COUNT; i++) {
                coll.add(i);
            }
            q1 = new ConcurrentLinkedQueue<>(coll);
        }
        return q1;
    }

    public UsersRepository() {
        ArrayList<Integer> coll = new ArrayList<>();
        q1 = new ConcurrentLinkedQueue<>(coll);
    }

    public UsersRepository(int start, int end) {
        setStartUsers(start);
        setMaxUsers(end);
        initUserRepo();
    }

    public int getUser() {
        return q1.poll();
    }

    public int getRandomUser(int maxNumberOfUsers) {

        int randomUser = WebElementUtils.getRandomNumberByRange(3, maxNumberOfUsers - 1);
        int count = 3;
        while (q1.iterator().hasNext()) {
            if (count++ == randomUser)
                return q1.poll();
            q1.poll();
        }
        return -1;
    }

    public boolean pushUser(Integer i) {
        if (!q1.contains(i))
            return q1.add(i);
        else return false;
    }

    public void clearQueue() {
        q1.clear();
    }

    public boolean isQueueClear() {
        return q1.isEmpty();
    }

    public void setMaxUsers(int maxUsers) {
        USERS_COUNT = maxUsers;
    }

    public void setStartUsers(int startUsers) {
        start = startUsers;
    }

    public void print() {
        Object[] a = q1.toArray();
        int size = a.length;
        for (int i = 0; i < size; i++) {
            System.err.println(a[i]);
        }
    }

    public int peek() {
        return q1.peek();
    }
}
