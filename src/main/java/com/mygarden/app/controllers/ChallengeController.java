package com.mygarden.app.controllers;
import java.io.IOException;
import java.util.List;

import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.repositories.ChallengesRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ChallengeController extends AbstractController{
    @FXML
    private Label challengeDescription;

    @FXML
    private Label challengeTip;

    @FXML
    private Label UserCoins;

    @FXML
    private Button completedBtn;

    private Challenge currentChallenge;
    private List<Challenge> challengeList;


    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @Override
    public void onUserIsSet()
    {
        //Call when the page is load to update all the UI with the user data
        updateUICoins();
        loadChallengesFromDatabase();
        loadRandomChallenge("daily");
    }

    private void loadChallengesFromDatabase() {

        ChallengesRepository repository = new ChallengesRepository();
        try 
        {
            challengeList = repository.findAll();
        } 
        catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    private void loadRandomChallenge(String type){
        if (challengeList == null || challengeList.isEmpty()) {
            challengeDescription.setText("There are no available challenges");
            challengeTip.setText("");
            completedBtn.setDisable(true);
            return;
        }

        // Filter challenges by type if specified
        List<Challenge> filtered = challengeList.stream()
            .filter(c -> type == null || c.getType().equalsIgnoreCase(type))
            .toList();

        if (filtered.isEmpty()) {
            challengeDescription.setText("Well done! You completed all the current challenges of this type");
            challengeTip.setText("No challenges of this type available");
            completedBtn.setDisable(true);
            return;
        }

        // Pick a random challenge
        int index = (int) (Math.random() * filtered.size());
        currentChallenge = filtered.get(index);

        // Update UI
        challengeDescription.setText(currentChallenge.getDescription());
        challengeTip.setText(currentChallenge.getTip());
        completedBtn.setDisable(false);


    }

    @FXML
    private void onChallengeCompleted(ActionEvent event) {
        if (currentChallenge != null) {
            // Update user coins
            getUser().earnCoins(currentChallenge.getPoints());
            updateUICoins();

            // Remove completed challenge from the list
            challengeList.remove(currentChallenge);

            // Load a new challenge
            loadRandomChallenge(null);
        }
    }

}