package bank;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BankController {
    private javafx.animation.Timeline infoFlashTimeline;
    @FXML private TextField InsertAccNumber;
    @FXML private TextField InsertPinNumber;
    @FXML private TextField AmmountMoneyToWid;
    @FXML private TextField InfoTetx;
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

    // Flash InfoTetx border: green for good, red for bad
    private void setInfoText(String text, boolean isGood) {
        InfoTetx.setText(text);
        String color1 = isGood ? "#4CAF50" : "#F44336";
        String color2 = "#eeeeee";
        if (infoFlashTimeline != null) infoFlashTimeline.stop();
        InfoTetx.setStyle("-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 4px; -fx-border-radius: 10px; -fx-border-color: " + color1 + ";");
        infoFlashTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.0),
                new javafx.animation.KeyValue(InfoTetx.styleProperty(), "-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 4px; -fx-border-radius: 10px; -fx-border-color: " + color1 + ";")),
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.3),
                new javafx.animation.KeyValue(InfoTetx.styleProperty(), "-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 4px; -fx-border-radius: 10px; -fx-border-color: " + color2 + ";"))
        );
        infoFlashTimeline.setCycleCount(4);
        infoFlashTimeline.setOnFinished(_ -> InfoTetx.setStyle("-fx-background-color: #eeeeee; -fx-font-size: 22px; -fx-text-fill: #333; -fx-border-width: 4px; -fx-border-radius: 10px; -fx-border-color: " + color1 + ";"));
        infoFlashTimeline.play();
    }
}
