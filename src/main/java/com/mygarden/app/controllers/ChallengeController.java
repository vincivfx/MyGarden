package com.mygarden.app.controllers;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.User;
import com.mygarden.app.repositories.TransferRepository;
import com.mygarden.app.repositories.UserChallengeRepository;
import com.mygarden.app.repositories.UserRepository;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ChallengeController extends AbstractController{
    @FXML
    private Label typeDisplayed;

    @FXML
    private Label challengeDesc;

    @FXML
    private Label challengeTip;

    @FXML
    private Label UserCoins;

    @FXML
    private Button completedBtn;

    @FXML
    private Button typeBtn;

    @FXML
    private Label remainingTime;

    private ScheduledExecutorService scheduler;

    private Challenge currentDailyChallenge;
    private Challenge currentWeeklyChallenge;
    private List<Challenge> dailyChallengeList;
    private List<Challenge> weeklyChallengeList;

    @FXML
    private void initialize() {
        // add a listener to handle window close event
        Platform.runLater(() -> {
            Stage stage = (Stage) remainingTime.getScene().getWindow();
            stage.setOnCloseRequest(event -> stopScheduler());
        });
    }

    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    private void updateUIChallenge(Challenge challenge)
    {
        if(challenge == null)
        {
            completedBtn.setDisable(true);
            return;
        }

        challengeDesc.setText(challenge.getDescription());
        challengeTip.setText(challenge.getTip());

        try {
            UserChallengeRepository ucr = new UserChallengeRepository();
            boolean alreadyCompleted = ucr.isChallengeCompletedByUser(getUser(), challenge);

            // Check if the challenge is already completed
            completedBtn.setDisable(alreadyCompleted);
        } catch (Exception e) {
            e.printStackTrace();
            // If there's an error, disable the button to prevent issues
            completedBtn.setDisable(true);
        }
    }

    @Override
    public void onUserIsSet()
    {   
        //Call when the page is loaded to update all the UI with the user data
        updateUICoins();
        typeDisplayed.setText("Daily");
        typeBtn.setText("Weekly");
        loadChallengesFromDatabase();
        assignChallenges();

        Challenge daily = getUser().getCurrentDailyChallenge();
        Challenge weekly = getUser().getCurrentWeeklyChallenge();
        if (weekly != null) {
            currentWeeklyChallenge = weekly;
            updateUIChallenge(weekly);
        }
        if (daily != null) {
            currentDailyChallenge = daily;
            updateUIChallenge(daily);
        }
        startCountdown("daily");
    }

    private void loadChallengesFromDatabase() {
        try{
            UserChallengeRepository ucr = new UserChallengeRepository();
            dailyChallengeList = ucr.findNotCompletedByUserAndType(getUser(), "daily");
            weeklyChallengeList = ucr.findNotCompletedByUserAndType(getUser(), "weekly");
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public void assignChallenges() {
        User user = getUser();
        LocalDate today = LocalDate.now();
        LocalDate lastGen = user.getLastChallengeGenerationDate();

        if (lastGen == null) {
            regenerateDailyAndWeekly();
            return;
        }

        // Check if a week has passed or if it's Monday for weekly challenge
        if (today.isAfter(lastGen.plusDays(6)) 
            || (today.isAfter(lastGen) && today.getDayOfWeek() == DayOfWeek.MONDAY)) {
                
            regenerateDailyAndWeekly();
        } else if (today.isAfter(lastGen)) {
            regenerateDaily();
        }

    }

    private void regenerateDailyAndWeekly() {
        User user = getUser();
        Challenge weekly = pickRandomChallenge("weekly");

        user.setCurrentWeeklyChallenge(weekly);

        // Update UI
        updateUIChallenge(weekly);

        regenerateDaily();
    }

    private void regenerateDaily() {
        User user = getUser();
        LocalDate today = LocalDate.now();
        Challenge daily = pickRandomChallenge("daily");

        user.setCurrentDailyChallenge(daily);
        user.setLastChallengeGenerationDate(today);
        
        UserRepository ur = new UserRepository();
        try {
            ur.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update UI
        updateUIChallenge(daily);
    }

    private Challenge pickRandomChallenge(String type){
        List<Challenge> challengeList = null;

        if(type.equals("daily")){
            challengeList = dailyChallengeList;
        } else if(type.equals("weekly")){
            challengeList = weeklyChallengeList;
        } else {
            throw new IllegalArgumentException("Invalid challenge type: " + type);
        }

        if (challengeList == null || challengeList.isEmpty()) {
            String description = "Well done! You completed all the current " + type + " challenges!";
            String tip = "No " + type + " challenges available";
            Challenge dummy = new Challenge(-1, description, type, tip);

            // Update UI
            updateUIChallenge(dummy);

            return null;
        }

        // Pick a random challenge
        int index = (int) (Math.random() * challengeList.size());

        return challengeList.get(index);
    }


    private void updateTimeRemaining(String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target;

        if (type.equalsIgnoreCase("daily")) {
            // midnight tonight
            target = now.toLocalDate().plusDays(1).atStartOfDay();
        } else {
            // next Monday at midnight
            LocalDate nextMonday = now.toLocalDate()
                    .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            target = nextMonday.atStartOfDay();
        }

        Duration duration = Duration.between(now, target);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        String text = String.format("%dd %dh %dm", days, hours, minutes);
        // Update the label on the JavaFX Application Thread
        Platform.runLater(() -> remainingTime.setText(text));
    }

    private void startCountdown(String type) {
        stopScheduler(); // Stop any existing scheduler
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // thread daemon -> doesn't prevent JVM shutdown
            return t;
        });
        // update immediatly and after every minute
        scheduler.scheduleAtFixedRate(() -> updateTimeRemaining(type), 0, 1, TimeUnit.MINUTES);
    }



    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        stopScheduler();
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @FXML
    private void onChallengeCompleted(ActionEvent event) {
        SoundManager.getInstance().playClick();
        Challenge currentChallenge;
        if(typeDisplayed.getText().equals("Daily")){
            currentChallenge = currentDailyChallenge;
        } else {
            currentChallenge = currentWeeklyChallenge;
        }

        if (currentChallenge != null) {
            try {
                // Mark challenge as completed in the database
                UserChallengeRepository ucr = new UserChallengeRepository();
                ucr.markAsCompleted(getUser(), currentChallenge);

                // Update user coins and UI
                TransferRepository tr = new TransferRepository();
                try {
                    tr.registerChallenge(getUser(), currentChallenge);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //getUser().earnCoins(currentChallenge.getPoints());
                updateUICoins();

                completedBtn.setDisable(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }



    @FXML
    private void changeChallengeType(ActionEvent event) {
        String curTypeDisplayed = typeDisplayed.getText();
        String curTypeBtn = typeBtn.getText();
        typeDisplayed.setText(curTypeBtn);
        typeBtn.setText(curTypeDisplayed);

        if(curTypeDisplayed.equals("Daily")){
            SoundManager.getInstance().playClick();
            updateUIChallenge(currentWeeklyChallenge);
            startCountdown("weekly");
        } else {
            SoundManager.getInstance().playClick();
            updateUIChallenge(currentDailyChallenge);
            startCountdown("daily");
        }
    }
}