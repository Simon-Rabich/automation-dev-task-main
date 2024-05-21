package com.antelope.af.utilities;

import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.logging.SelTestLog;
import com.antelope.af.restapi.UniRestAPITestsController;
import com.antelope.af.restapi.UniRestUsersController;
import com.antelope.af.testconfig.popertiesinit.PropertiesGetter;
import com.antelope.af.utilities.testpparameters.UserParamsThreadLocal;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static java.lang.Math.abs;

/**
 * Utilities class varios methods which can be used in different tests
 *
 * @author Fadi Zaboura
 */
public class XsitesUtilities {

    protected static javax.mail.Message[] messages = null;
    protected final static List<String> monthList = new ArrayList<>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));

    public static File getFileFromResources(String fileName, Class<?> c) {
        ClassLoader classLoader = c.getClassLoader();
        if (PropertiesGetter.instance().retrieveDriverProperties().isLocal())
            return new File(classLoader.getResource("documents/" + fileName).getFile());
        else
            return new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                    + File.separator + "java" + File.separator + "resources" + File.separator + "documents"
                    + File.separator + fileName);
    }

    public static File getShhFileFromResources(String fileName, Class<?> c) {
        ClassLoader classLoader = c.getClassLoader();
        if (PropertiesGetter.instance().retrieveDriverProperties().isLocal())
            return new File(classLoader.getResource("ssh/" + fileName).getFile());
        else
            return new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                    + File.separator + "java" + File.separator + "resources" + File.separator + "ssh"
                    + File.separator + fileName);
    }

    public static void createUsers(int min, int max, String baseUrl, String apiKey, String authTuken, String crmBaseUrl, String brand)
            throws Exception {
        HashMap<String, String> _data;
        for (int i = min; i <= max; i++) {
            _data = UniRestAPITestsController.registerNewUserThroughApiTest(baseUrl, apiKey, i);
            UniRestUsersController.convertToTestUser(crmBaseUrl, authTuken, Integer.valueOf(_data.get("userId")), brand);
        }
    }

    // just for debugging
    public static void turnOnTestToggle(String authToken)
            throws Exception {
        String userId = "12582";
        String crmBaseUrl = "https://stg-apicrm.antelopesystem.com";
        String brand = "StgMysql8";
        UniRestUsersController.convertToTestUser(crmBaseUrl, authToken, Integer.valueOf(userId), brand);
    }

// just for debugging
    public static void turnOnBlock(String authToken)
            throws Exception {
        String userId = "12599";
        String crmBaseUrl = "https://stg-apicrm.antelopesystem.com";
        UniRestUsersController.blockAllCommunications(crmBaseUrl, authToken, Integer.valueOf(userId), true);
    }
    public static String getCurrentDate() {
        String currentDateYear = String.valueOf(LocalDate.now().getYear());
        String currentDateDay = String.valueOf(LocalDate.now().getDayOfMonth());
        String currentDateMonth = LocalDate.now().getMonth().name();
        String date = convertMonthToShourtcut(currentDateMonth) + " " + currentDateDay + ", " + currentDateYear;
        return date;
    }

    public static String convertMonthToShourtcut(String month) {
        String monthShortcut = "";
        switch (month.toLowerCase()) {
            case "january":
                monthShortcut = "Jan";
                break;

            case "february":
                monthShortcut = "Feb";
                break;

            case "march":
                monthShortcut = "Mar";
                break;

            case "april":
                monthShortcut = "Apr";
                break;

            case "may":
                monthShortcut = "May";
                break;

            case "june":
                monthShortcut = "Jun";
                break;

            case "july":
                monthShortcut = "Jul";
                break;

            case "august":
                monthShortcut = "Aug";
                break;

            case "september":
                monthShortcut = "Sep";
                break;
            case "october":
                monthShortcut = "Oct";
                break;

            case "november":
                monthShortcut = "Nov";
                break;

            case "december":
                monthShortcut = "Dec";
                break;
        }
        return monthShortcut;
    }

    public static void searchForEmailSubjectUsingImap(String userName, String password, List<String> emailSubjects) throws IOException {
        List<String> foundSubjects = new ArrayList<>();
        Folder folder = null;
        Store store = null;
        String strMailSubject = "";
        Object subject = null;
        SelTestLog.info("***Reading mailbox...");
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            int messagesCount = folder.getMessageCount();
            messages = folder.getMessages();
            SelTestLog.info("No of Messages : " + messagesCount);
            Asserter.assertTrue(messagesCount >= emailSubjects.size(),
                    "Expected number of emails in automation gmail inbox to be bigger than " + emailSubjects.size()
                            + " but found " + messagesCount,
                    "Validating that number of emails in automation gmail inbox is bigger than " + 1);
            for (int i = 0; i < messages.length; i++) {
                SelTestLog.info("Reading MESSAGE # " + (i + 1) + "...");
                javax.mail.Message msg = messages[i];
                subject = msg.getSubject();
                strMailSubject = (String) subject;
                SelTestLog.info(strMailSubject);
                Asserter.assertFalse(strMailSubject.isEmpty(), "Expected email subject not be empty",
                        "Validating that email subject is not empty, found the following : " + strMailSubject);
                Asserter.assertFalse(getTextFromMessage(msg).isEmpty(), "Expected message content not to be empty",
                        "Validating that message content is not empty");
                foundSubjects.add(strMailSubject);
            }
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        } finally {
            int count = 0;

            for (int i = 0; i <= foundSubjects.size() - 1; i++) {
                for (int j = 0; j <= emailSubjects.size() - 1; j++)
                    if (foundSubjects.get(i).toLowerCase().contains(emailSubjects.get(j).toLowerCase()))
                        count++;
            }
            Asserter.assertTrue(count >= emailSubjects.size(),
                    "Expected that email subjects" + emailSubjects.toString() + "to be found," + " but found " + count  + " emails",
                    "Validating that email subjects " + emailSubjects.toString() + " is found");
            if (folder != null) {
                try {
                    folder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void searchForEmailSubject(String userName, String password, List<String> emailSubjects) throws IOException {
            List<String> foundSubjects = new ArrayList<>();
            Folder folder = null;
            Store store = null;
            String strMailSubject = "";
            Object subject = null;
            SelTestLog.info("***Reading mailbox...");

            try {
                Properties props = new Properties();
                props.put("mail.store.protocol", "imaps");
                Session session = Session.getInstance(props);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", userName, password);
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);

                int messagesCount = folder.getMessageCount();
                messages = folder.getMessages();
                SelTestLog.info("No of Messages : " + messagesCount);
                Asserter.assertTrue(messagesCount >= emailSubjects.size(),
                        "Expected number of emails in automation gmail inbox to be bigger than " + emailSubjects.size()
                                + " but found " + messagesCount,
                        "Validating that number of emails in automation gmail inbox is bigger than " + 1);

                // Read only the newest email
                if (messages.length > 0) {
                    javax.mail.Message latestMessage = messages[messages.length - 1];
                    SelTestLog.info("Reading latest email...");
                    subject = latestMessage.getSubject();
                    strMailSubject = (String) subject;
                    SelTestLog.info(strMailSubject);
                    Asserter.assertFalse(strMailSubject.isEmpty(), "Expected email subject not to be empty",
                            "Validating that email subject is not empty, found the following: " + strMailSubject);
                    Asserter.assertFalse(getTextFromMessage(latestMessage).isEmpty(), "Expected message content not to be empty",
                            "Validating that message content is not empty");
                    foundSubjects.add(strMailSubject);
                }

            } catch (MessagingException messagingException) {
                messagingException.printStackTrace();
            } finally {
                // Log found email subjects in lowercase
                SelTestLog.info("Found email subjects: " + foundSubjects);

                // Validate that at least one expected subject is found
                Asserter.assertTrue(emailSubjects.stream().anyMatch(foundSubjects::contains),
                        "Expected that at least one of the email subjects " + emailSubjects.toString() + " to be found, but found none",
                        "Validating that at least one expected email subject is found");

                if (folder != null) {
                    try {
                        folder.close(false);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                if (store != null) {
                    try {
                        store.close();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    public static void searchAndReplyToEmailSubject(String userName, String password, List<String> emailSubjects, String replyContent) throws IOException {
        List<String> foundSubjects = new ArrayList<>();
        Folder folder = null;
        Store store = null;
        String strMailSubject = "";

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            props.put("mail.smtp.ssl.enable", "true");
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE); // Open folder for read and write operations

            // Get all messages in the folder
            Message[] messages = folder.getMessages();
            int messagesCount = messages.length;
            SelTestLog.info("No of Messages : " + messagesCount);

            // Iterate through messages to find the latest matching email subject
            for (int i = messagesCount - 1; i >= 0; i--) {
                javax.mail.Message currentMessage = messages[i];
                String currentSubject = currentMessage.getSubject();

                if (emailSubjects.contains(currentSubject)) {
                    SelTestLog.info("Found email subject: " + currentSubject);
                    foundSubjects.add(currentSubject);

//                    // Create reply message
//                    MimeMessage replyMessage = (MimeMessage) currentMessage.reply(false);
//                    replyMessage.setText(replyContent); // Set the reply message content
//
//                    // Save the reply into the folder
//                    folder.appendMessages(new Message[] { replyMessage });
//
//                    SelTestLog.info("Reply added to the folder successfully!");

//                    // Create reply message
//                    MimeMessage replyMessage = (MimeMessage) currentMessage.reply(false);
//
//// Set the "From" address to the same as the original message's "To" address
//                    replyMessage.setFrom(new InternetAddress(userName));
//
//// Set the reply message content
//                    replyMessage.setText(replyContent); // Set the reply message content
//
//// Save the reply into the folder
//                    folder.appendMessages(new Message[] { replyMessage });
//                    SelTestLog.info("Reply added to the folder successfully!");
//



                    // Create reply message
                    MimeMessage replyMessage = (MimeMessage) currentMessage.reply(false);

// Set the "From" address to the same as the original message's "To" address
                    Address[] fromAddresses = currentMessage.getFrom();
                    if (fromAddresses != null && fromAddresses.length > 0) {
                        replyMessage.setFrom(fromAddresses[0]); // Set the "From" address to the original sender
                    } else {
                        // If no "From" address found, use the username provided
                        replyMessage.setFrom(new InternetAddress(userName));
                    }

// Set the reply message content
                    replyMessage.setText(replyContent); // Set the reply message content

// Send the reply
                    Transport.send(replyMessage);

                    SelTestLog.info("Reply sent successfully!");

                    // Exit loop after replying to the first matching email
                    break;
                }
            }

            // Validate that at least one expected subject is found
            Asserter.assertTrue(foundSubjects.size() > 0,
                    "Expected at least one of the email subjects " + emailSubjects.toString() + " to be found, but found none",
                    "Validating that at least one expected email subject is found");

        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        } finally {
            // Close the folder and store
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void automateGmailUsingImap(String USERNAME, String PASSWORD, List<String> emailsSubjects)
            throws IOException, MessagingException {
        HashMap<String, Boolean> foundSubjects = new HashMap<>();
        HashSet<String> emailSubjectsHashSet = new HashSet<>();
        Folder folder = null;
        Store store = null;
        String strMailSubject = "";
        Object subject = null;
        SelTestLog.info("***Reading mailbox...");
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", USERNAME, PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            int messagesCount = folder.getMessageCount();
            messages = folder.getMessages();
            SelTestLog.info("No of Messages : " + messagesCount);
//            Asserter.assertTrue(messagesCount >= emailsSubjects.size(),
//                    "Expected number of emails in automation gmail inbox to be bigger than " + emailsSubjects.size()
//                            + " but found " + messagesCount,
//                    "Validating that number of emails in automation gmail inbox is bigger than "
//                            + emailsSubjects.size());
            for (int i = 0; i < messages.length; i++) {
                SelTestLog.info("Reading MESSAGE # " + (i + 1) + "...");
                javax.mail.Message msg = messages[i];
                subject = msg.getSubject();
                strMailSubject = (String) subject;
                emailSubjectsHashSet.add(strMailSubject);
                SelTestLog.info(strMailSubject);
                Asserter.assertFalse(strMailSubject.isEmpty(), "Expected email subject not be empty",
                        "Validating that email subject is not empty, found the following : " + strMailSubject);
                Asserter.assertFalse(getTextFromMessage(msg).isEmpty(), "Expected message content not to be empty",
                        "Validating that message content is not empty");
            }
            for (int k = 0; k < emailsSubjects.size() - 1; k++) {
                foundSubjects.put(emailsSubjects.get(k), false);
            }
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        } finally {
            int size = emailSubjectsHashSet.size();
            Object[] subjectsArray = emailSubjectsHashSet.toArray();
            for (int i = 0; i < emailsSubjects.size() - 1; i++) {
                for (int j = 0; j < size - 1; j++) {
                    if (subjectsArray[j].toString().toLowerCase().contains(emailsSubjects.get(i).toLowerCase())) {
                        foundSubjects.put(emailsSubjects.get(j), true);
                    }
                }
            }
            if (foundSubjects.containsValue(false)) {
                List<String> notFoundSubjects = new ArrayList<String>();
                for (int i = 0; i < foundSubjects.size(); i++) {
                    if (foundSubjects.get(emailsSubjects.get(i)) == false) {
                        notFoundSubjects.add(emailsSubjects.get(i));
                    }
                }
//                Asserter.fail(
//                        "Expected all email subjects to be found in email but found the following subjects which are not there "
//                                + notFoundSubjects.toString());
            }

//            SelTestLog.info("cleaning inbox!");
//            cleanInbox(folder, store);
            if (folder != null) {
                try {
                    folder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void cleanInbox(String userName, String password) throws Throwable {
        Folder folder = null;
        Store store = null;
        SelTestLog.info("***Cleaning mailbox...");
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore("imaps");
            SelTestLog.info("***Connecting to gmail account...");
            store.connect("imap.gmail.com", userName, password);
            System.out.println("LOGG" + userName + password + "\n");
            SelTestLog.info("***Redirecting to inbox...");
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            messages = folder.getMessages();
            SelTestLog.info("Deleting " + messages.length + " messages");
            for (int i = 0; i < messages.length; i++)
                messages[i].setFlag(Flags.Flag.DELETED, true);

            WebdriverUtils.sleep(2000);
            folder = store.getFolder("INBOX");
            Asserter.assertTrue(folder.getMessageCount() == 0, "Expected automation mailbox to be empty",
                    "Validating that automation mailbox is empty");
        } catch (Throwable e) {
            throw e;
        }
    }

    private static void cleanInbox(Folder folder, Store store) throws MessagingException {
        messages = folder.getMessages();
        for (int i = 0; i < messages.length; i++)
            messages[i].setFlag(Flags.Flag.DELETED, true);

        WebdriverUtils.sleep(2000);
        folder = store.getFolder("INBOX");
        Asserter.assertTrue(folder.getMessageCount() == 0, "Expected automation mailbox to be empty",
                "Validating that automation mailbox is empty");
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        try {
            result = message.getContent().toString();
        } catch (Exception e) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public static String getCountry(String countryIso) {
        Locale country = new Locale("", countryIso);
        return country.getDisplayCountry();
    }


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    public static String getRandomDay(String month) {
        int monthNumber = 0;
        for (int i = 0; i < monthList.size(); i++) {
            if (month == monthList.get(i)) {
                monthNumber = i + 1;
                break;
            }
        }
        if (monthNumber == 2) {
            return String.valueOf(WebElementUtils.getRandomNumberByRange(1, 28));
        } else if ((abs(monthNumber - 5) == 1) || (abs(monthNumber - 10) == 1))
            return String.valueOf(WebElementUtils.getRandomNumberByRange(1, 30));
        else
            return String.valueOf(WebElementUtils.getRandomNumberByRange(1, 31));

    }

    public static String getRandomYear() {
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(WebElementUtils.getRandomNumberByRange(1900, now.getYear()));
    }

    public static String getRandomMonth() {
        List<String> monthList = new ArrayList<>();
        Arrays.stream(Month.values()).forEach(x -> monthList.add(getMonth(x.getValue())));
        int randomMonth = WebElementUtils.getRandomNumberByRange(0, monthList.size() - 1);
        return monthList.get(randomMonth);
    }

    public static String getCurrentDate(Boolean withTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter;

        if(withTime)
            formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        else
            formatter = new SimpleDateFormat("dd-MM-yyyy");

        return formatter.format(calendar.getTime());
    }

    public static String getTomorrowDate(Boolean withTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter;

        if(withTime)
            formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        else
            formatter = new SimpleDateFormat("dd-MM-yyyy");

        calendar.add(Calendar.DATE, 1);
        return formatter.format(calendar.getTime());
    }


//    public static void replyToEmailSubject(String userName, String password, String replySubject, String replyContent) throws IOException {
//        List<String> foundSubjects = new ArrayList<>();
//        Folder folder = null;
//        Store store = null;
//        String strMailSubject = "";
//        Object subject = null;
//        SelTestLog.info("***Reading mailbox...");
//
//        try {
//            Properties props = new Properties();
//            props.put("mail.store.protocol", "imaps");
//            Session session = Session.getInstance(props);
//            store = session.getStore("imaps");
//            store.connect("imap.gmail.com", userName, password);
//            folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_ONLY);
//
//            int messagesCount = folder.getMessageCount();
//            messages = folder.getMessages();
//            SelTestLog.info("No of Messages : " + messagesCount);
//            Asserter.assertTrue(messagesCount >= emailSubjects.size(),
//                    "Expected number of emails in automation gmail inbox to be bigger than " + emailSubjects.size()
//                            + " but found " + messagesCount,
//                    "Validating that number of emails in automation gmail inbox is bigger than " + 1);
//
//            // Read only the newest email
//            if (messages.length > 0) {
//                javax.mail.Message latestMessage = messages[messages.length - 1];
//                SelTestLog.info("Reading latest email...");
//                subject = latestMessage.getSubject();
//                strMailSubject = (String) subject;
//                SelTestLog.info(strMailSubject);
//                Asserter.assertFalse(strMailSubject.isEmpty(), "Expected email subject not to be empty",
//                        "Validating that email subject is not empty, found the following: " + strMailSubject);
//                Asserter.assertFalse(getTextFromMessage(latestMessage).isEmpty(), "Expected message content not to be empty",
//                        "Validating that message content is not empty");
//                foundSubjects.add(strMailSubject);
//            }
//
//        } catch (MessagingException messagingException) {
//            messagingException.printStackTrace();
//        } finally {
//            // Log found email subjects in lowercase
//            SelTestLog.info("Found email subjects: " + foundSubjects);
//
//            // Validate that at least one expected subject is found
//            Asserter.assertTrue(emailSubjects.stream().anyMatch(foundSubjects::contains),
//                    "Expected that at least one of the email subjects " + emailSubjects.toString() + " to be found, but found none",
//                    "Validating that at least one expected email subject is found");
//
//            if (folder != null) {
//                try {
//                    folder.close(false);
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (store != null) {
//                try {
//                    store.close();
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
