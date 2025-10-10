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
    }
    private void loadChallengesFromDatabase() {

        ChallengesRepository repository = new ChallengesRepository();
        try 
        {
            List<Challenge> challengeList = repository.findAll();
        } 
        catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }


    private void loadRandomChallenge(String type){
        if (challengeList == null || challengeList.isEmpty()) {
            challengeDescription.setText("There are no available challenges");
            //ChallengeTip.setText("");
            completedBtn.setDisable(true);
            return;
        }
        //take a random one 


    }
}