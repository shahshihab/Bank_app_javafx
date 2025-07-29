package bank;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;


public class BankController {
    private Timeline infoFlashTimeline;
    @FXML private TextField InsertAccNumber;
    @FXML private TextField InsertPinNumber;
    @FXML private TextField AmmountMoneyToWid;
    @FXML private TextField InfoText;
    @FXML private Button PinAccept;
    @FXML private Button StateOfAccount;
    @FXML private Button MoneyToWith;
    @FXML private Button DepositMoney;

    // Confirm PIN and create account
    @FXML
    private void onPinAccept() {
        String acc = InsertAccNumber.getText();
        String pin = InsertPinNumber.getText();
        if (acc.isEmpty() || pin.isEmpty()) {
            setInfoText("Account number and PIN required.", false);
            return;
        }
        try (var rs = MSSQLUtil.getAccount(acc)) {
            if (rs != null && rs.next()) {
                setInfoText("Account already exists.", false);
            } else {
                boolean ok = MSSQLUtil.insertAccount(acc, pin, 0.0);
                setInfoText(ok ? "Account created." : "Error creating account.", ok);
            }
        } catch (Exception e) {
            setInfoText("Error: " + e.getMessage(), false);
        }
    }

    // Show account balance
    @FXML
    private void onStateOfAccount() {
        String acc = InsertAccNumber.getText();
        String pin = InsertPinNumber.getText();
        try (var rs = MSSQLUtil.getAccount(acc)) {
            if (rs != null && rs.next()) {
                String dbPin = rs.getString("Pin");
                if (!dbPin.equals(pin)) {
                    setInfoText("Incorrect PIN for account.", false);
                    return;
                }
                double bal = rs.getDouble("Balance");
                setInfoText("Balance: " + bal, true);
            } else {
                setInfoText("Account not found.", false);
            }
        } catch (Exception e) {
            setInfoText("Error: " + e.getMessage(), false);
        }
    }

    // Withdraw money
    @FXML
    private void onMoneyToWith() {
        String acc = InsertAccNumber.getText();
        String pin = InsertPinNumber.getText();
        String amtStr = AmmountMoneyToWid.getText();
        try {
            double amt = Double.parseDouble(amtStr);
            try (var rs = MSSQLUtil.getAccount(acc)) {
                if (rs != null && rs.next()) {
                    String dbPin = rs.getString("Pin");
                    if (!dbPin.equals(pin)) {
                        setInfoText("Incorrect PIN for account.", false);
                        return;
                    }
                    double bal = rs.getDouble("Balance");
                    if (bal >= amt) {
                        double newBal = bal - amt;
                        MSSQLUtil.updateBalance(acc, newBal);
                        setInfoText("Withdrawn: " + amt + ". New balance: " + newBal, true);
                    } else {
                        setInfoText("Insufficient funds.", false);
                    }
                } else {
                    setInfoText("Account not found.", false);
                }
            }
        } catch (Exception e) {
            setInfoText("Error: " + e.getMessage(), false);
        }
    }

    // Deposit money
    @FXML
    private void onDepositMoney() {
        String acc = InsertAccNumber.getText();
        String pin = InsertPinNumber.getText();
        String amtStr = AmmountMoneyToWid.getText();
        try {
            double amt = Double.parseDouble(amtStr);
            try (var rs = MSSQLUtil.getAccount(acc)) {
                if (rs != null && rs.next()) {
                    String dbPin = rs.getString("Pin");
                    if (!dbPin.equals(pin)) {
                        setInfoText("Incorrect PIN for account.", false);
                        return;
                    }
                    double bal = rs.getDouble("Balance");
                    double newBal = bal + amt;
                    MSSQLUtil.updateBalance(acc, newBal);
                    setInfoText("Deposited: " + amt + ". New balance: " + newBal, true);
                } else {
                    setInfoText("Account not found.", false);
                }
            }
        } catch (Exception e) {
            setInfoText("Error: " + e.getMessage(), false);
        }
    }

    // Flash InfoText border: green for success, red for error
    private void setInfoText(String text, boolean isGood) {
        InfoText.setText(text);
        String color1 = isGood ? "#21c421ff" : "#ca2020ff";
        String baseStyle = "-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-border-color: #fff;";
        String finalStyle = "-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-border-color: " + color1 + ";";
        InfoText.setStyle(finalStyle);

        if (infoFlashTimeline != null) infoFlashTimeline.stop();

        infoFlashTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.0),
                new KeyValue(InfoText.styleProperty(), finalStyle))
            , new KeyFrame(Duration.seconds(0.25),
                new KeyValue(InfoText.styleProperty(), baseStyle))
            , new KeyFrame(Duration.seconds(0.5),
                new KeyValue(InfoText.styleProperty(), finalStyle))
        );
        infoFlashTimeline.setCycleCount(2);
        infoFlashTimeline.setOnFinished(_ -> InfoText.setStyle(finalStyle));
        infoFlashTimeline.play();
    }
}
