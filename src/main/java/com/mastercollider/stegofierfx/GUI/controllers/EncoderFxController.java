

/*******************************************************************************
 *  This file is made by Probal D. Saikia on 3/1/2022
 *  https://github.com/Master-COLLiDER
 *  NOTICE: This file is subject to the terms and conditions defined
 * in the file 'LICENSE' which is part of this source code package.
 ******************************************************************************/

package com.mastercollider.stegofierfx.GUI.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.mastercollider.stegofierfx.CLI.CLIExecutor;
import com.mastercollider.stegofierfx.CLI.EncodeCLIParameters;
import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Encryption.AES256;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.Encryption.TripleDES;
import com.mastercollider.stegofierfx.GUI.DecoderFX;
import com.mastercollider.stegofierfx.GUI.EncoderFX;
import com.mastercollider.stegofierfx.GUI.RSAKeyGeneratorFX;
import com.mastercollider.stegofierfx.ImageProcessing.ImageFormat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import static com.mastercollider.stegofierfx.Config.HEADER_LENGTH_BYTE;

public class EncoderFxController implements Initializable {

    File selectedCoverImageFile =  null;
    File selectedOutputImageFile = null;
    File selectedPublicKey = null;


    int selectedImageFileWidth = 0;
    int selectedImageFileHeight = 0;
    int selectedEncryptionType = 0;

    int MaxRequiredPixels = 0;
    int maxEncryptedTextSize = 0;
    int TotalAvailablePixel = 0;

    @FXML
    private Button btnMainMinimize;
    @FXML
    private Button btnMainExit;
    @FXML
    private Button btnSideBarEncode;
    @FXML
    private Button btnSideBarDecode;
    @FXML
    private Button btnSideBarGenerateRSAKeys;


    @FXML private AnchorPane anchorPaneMain;
    @FXML private Button btnBrowseCoverImage;
    @FXML private Label labelSelectedCoverImage;
    @FXML private Label labelInvalidCoverImage;
    @FXML private Button btnBrowseOutputFile;
    @FXML private Label labelSelectedOutputFile;
    @FXML private Label labelInvalidOutputImage;
    @FXML private javafx.scene.control.TextArea textAreaMessage;
    @FXML private Label labelMessageCharacterLength;
    @FXML private Label labelInvalidMessage;
    @FXML private Label labelEncryptionHeader;
    @FXML private Label labelEncryptionErrorMessage;
    @FXML private VBox groupEncryption;
    @FXML private JFXCheckBox jfxCheckboxEncryptMessage;
    @FXML private HBox groupEncryptionType;
    @FXML private RadioButton radioButtonAES;
    @FXML private RadioButton radioButtonTripleDES;
    @FXML private RadioButton radioButtonRSA;
    @FXML private HBox groupPassword;
    @FXML private PasswordField passwordField;
    @FXML private HBox groupPublicKey;
    @FXML private Label labelSelectedPublicKey;
    @FXML private Button btnBrowsePublicKey;
    @FXML private ImageView ImageviewEncoder;
    @FXML private GridPane groupImageDetails;
    @FXML private Label labelTextSize;
    @FXML private Label labelEncryptedTextSize;
    @FXML private Label labelMaxRequiredPixels;
    @FXML private Label labelTotalAvailablePixels;
    @FXML private VBox groupAditionalOptions;
    @FXML private JFXCheckBox jfxCheckBoxRed;
    @FXML private JFXCheckBox jfxCheckBoxGreen;
    @FXML private JFXCheckBox jfxCheckBoxBlue;
    @FXML private JFXSlider jfxSliderLSB;
    @FXML private Label labelEncodingError;
    @FXML private Button btnStartEncoding;


    TextFormatter defaultTextFormatter;
    TextFormatter rsaTextFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        defaultTextFormatter = textAreaMessage.getTextFormatter();
        rsaTextFormatter = new TextFormatter<String>(change ->
        {
            if (change.getControlNewText().length()>=117)
            {
                labelInvalidMessage.setText("Character Limit Reached!");
            }else
                labelInvalidMessage.setText("Character Limit For RSA: 117 Characters");

           return change.getControlNewText().length() <= 117 ? change : null;
        });




        jfxSliderLSB.valueProperty().addListener((obs,ov,nv) -> {
            doImageDetailsUpdate();
        });

        EventHandler ColorChannelCheckBoxHandler = event -> {
            doImageDetailsUpdate();
            if (event.getSource()==jfxCheckBoxBlue){
                if (!jfxCheckBoxGreen.isSelected() && !jfxCheckBoxRed.isSelected()) {
                    jfxCheckBoxBlue.setSelected(true);
                }
            }else if(event.getSource() == jfxCheckBoxGreen){
                if (!jfxCheckBoxBlue.isSelected() && !jfxCheckBoxRed.isSelected()) {
                    jfxCheckBoxGreen.setSelected(true);
                }
            }else if (event.getSource() == jfxCheckBoxRed){
                if (!jfxCheckBoxGreen.isSelected() && !jfxCheckBoxBlue.isSelected()) {
                    jfxCheckBoxRed.setSelected(true);
                }
            }
        };
       jfxCheckBoxBlue.setOnAction(ColorChannelCheckBoxHandler);
       jfxCheckBoxGreen.setOnAction(ColorChannelCheckBoxHandler);
       jfxCheckBoxRed.setOnAction(ColorChannelCheckBoxHandler);

       jfxCheckboxEncryptMessage.setOnAction(event -> {
           if (jfxCheckboxEncryptMessage.isSelected())
           {
               groupEncryptionType.setDisable(false);
               doOnEncryptionTypeRadioButtonChange();
           }else
               {
                   labelInvalidMessage.setVisible(false);
                   groupEncryptionType.setDisable(true);
                   groupPassword.setDisable(true);
                   groupPublicKey.setDisable(true);
                   selectedEncryptionType = 0;
                   doImageDetailsUpdate();

               }
       });

    }

    public void doOnEncryptionTypeRadioButtonChange(){


        if (radioButtonAES.isSelected()){


            textAreaMessage.setTextFormatter(defaultTextFormatter);
            selectedEncryptionType = 1;
            groupPassword.setDisable(false);
            groupPublicKey.setDisable(true);
            labelInvalidMessage.setVisible(false);
        }else if (radioButtonTripleDES.isSelected()){


            textAreaMessage.setTextFormatter(defaultTextFormatter);
            selectedEncryptionType = 2;
            labelInvalidMessage.setVisible(false);
            groupPassword.setDisable(false);
            groupPublicKey.setDisable(true);
        }else if (radioButtonRSA.isSelected()){



            if (textAreaMessage.getLength()>117){
                labelInvalidMessage.setText("Character Limit Reached!");
                textAreaMessage.setText(textAreaMessage.getText().substring(0,117));
            }
            textAreaMessage.setTextFormatter(rsaTextFormatter);
            labelInvalidMessage.setVisible(true);
            selectedEncryptionType = 3;
            groupPassword.setDisable(true);
            groupPublicKey.setDisable(false);

        }

        doImageDetailsUpdate();
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnSideBarEncode) {
            System.out.println("SideBar Encode Button Pressed");
        }
        else if (actionEvent.getSource() == btnSideBarDecode) {
            try {
               DecoderFX decoderFX = new DecoderFX();
                Stage currentStage = (Stage) btnSideBarDecode.getScene().getWindow();
                decoderFX.display(currentStage);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }else if (actionEvent.getSource() == btnSideBarGenerateRSAKeys) {
            try {
                RSAKeyGeneratorFX rsaKeyGeneratorFX = new RSAKeyGeneratorFX();
                Stage currentStage = (Stage) btnSideBarGenerateRSAKeys.getScene().getWindow();
                rsaKeyGeneratorFX.display(currentStage);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openChooseCoverImage(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("Bitmap files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll( extFilterPNG,extFilterBMP);
        File temp = fileChooser.showOpenDialog((Stage) btnBrowseCoverImage.getScene().getWindow());
        if (temp!=null)
        {
            System.out.println(temp.toString());
            selectedCoverImageFile = temp;
            labelSelectedCoverImage.setText(selectedCoverImageFile.toString());
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedCoverImageFile);

                selectedImageFileHeight = bufferedImage.getHeight();
                selectedImageFileWidth = bufferedImage.getWidth();

                Image image = ImageFormat.convertToFxImage(bufferedImage);
                ImageviewEncoder.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                labelSelectedCoverImage.setText("Failed to Load");
            }finally {
                doImageDetailsUpdate();
            }
        }
    }
    public void openChooseOutputImage(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("Bitmap files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll( extFilterPNG,extFilterBMP);
        File temp = fileChooser.showSaveDialog(((Stage) btnBrowseOutputFile.getScene().getWindow()));
        if (temp!=null)
        {

            System.out.println(temp.toString());
            selectedOutputImageFile = temp;
            labelSelectedOutputFile.setText(selectedOutputImageFile.toString());
            doImageDetailsUpdate();

        }
    }

    public void openChoosePublicKey(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterSFPBK = new FileChooser.ExtensionFilter("Stegofier RSA Public Key (*.sfpbk)", "*.SFPBK");
        fileChooser.getExtensionFilters().addAll( extFilterSFPBK);
        File temp = fileChooser.showOpenDialog(((Stage) btnBrowsePublicKey.getScene().getWindow()));
        if (temp!=null)
        {
            System.out.println(temp.toString());

            selectedPublicKey = temp;
            labelSelectedPublicKey.setText(selectedPublicKey.toString());
            doImageDetailsUpdate();
        }
    }


    public int getColorChannelsValue(){
        boolean R = jfxCheckBoxRed.isSelected();
        boolean G = jfxCheckBoxGreen.isSelected();
        boolean B = jfxCheckBoxBlue.isSelected();

        if (R&&G&&B){return 7;}
        else if (R&&B){return 6;}
        else if (G&&B){return 5;}
        else if (R&&G){return 4;}
        else if (B){return 3;}
        else if (G){return 2;}
        else {return 1;}

    }




    public void doImageDetailsUpdate()
    {
        TotalAvailablePixel = selectedImageFileWidth*selectedImageFileHeight;
        int inputMessageLength = textAreaMessage.getLength();
        labelMessageCharacterLength.setText("Characters: "+inputMessageLength);
        maxEncryptedTextSize = inputMessageLength;
        if (selectedEncryptionType == 2 && passwordField.getLength() < 24 ){
            labelEncryptionErrorMessage.setText("Enter password for Encryption, TripleDES requires 24 character password");
            labelEncryptionErrorMessage.setVisible(true);
        }
        else if (selectedEncryptionType == 1 && passwordField.getLength() <=0 )
        {
            labelEncryptionErrorMessage.setText("Enter password for Encryption");
            labelEncryptionErrorMessage.setVisible(true);
        }else if (selectedEncryptionType == 3 && selectedPublicKey == null )
        {
            labelEncryptionErrorMessage.setText("Select a public!");
            labelEncryptionErrorMessage.setVisible(true);
        }
        else
            labelEncryptionErrorMessage.setVisible(false);

        if (inputMessageLength == 0)
            btnStartEncoding.setDisable(true);
        else
            {
                if (selectedEncryptionType == 1){
                    if (passwordField.getLength()<=0)
                        btnStartEncoding.setDisable(true);
                    else {
                        if (selectedCoverImageFile ==null || selectedOutputImageFile ==null)
                            btnStartEncoding.setDisable(true);
                        else
                            btnStartEncoding.setDisable(false);
                    }
                }else if (selectedEncryptionType == 2){
                    if (passwordField.getLength()<24)
                    {
                        btnStartEncoding.setDisable(true);

                    }

                    else {
                        if (selectedCoverImageFile ==null || selectedOutputImageFile ==null)
                            btnStartEncoding.setDisable(true);
                        else
                            btnStartEncoding.setDisable(false);
                    }

                } else if (selectedEncryptionType == 3) {
                    if (selectedPublicKey==null)
                        btnStartEncoding.setDisable(true);
                    else {
                        if (selectedCoverImageFile ==null || selectedOutputImageFile ==null)
                            btnStartEncoding.setDisable(true);
                        else
                            btnStartEncoding.setDisable(false);
                    }
                }else if (selectedCoverImageFile ==null || selectedOutputImageFile ==null){
                    btnStartEncoding.setDisable(true);
                }else
                    btnStartEncoding.setDisable(false);

            }

        if (EncryptionType.getEncryptionType(selectedEncryptionType)==EncryptionType.AES256){
            maxEncryptedTextSize = AES256.lengthAfterEncryption(inputMessageLength);
        }else if (EncryptionType.getEncryptionType(selectedEncryptionType)==EncryptionType.TRIPLEDES){
            maxEncryptedTextSize = TripleDES.lengthAfterEncryption(inputMessageLength);
        }else if (EncryptionType.getEncryptionType(selectedEncryptionType)==EncryptionType.RSA){
            maxEncryptedTextSize = 172;
        }

        int maxTotalRequiredBytes = HEADER_LENGTH_BYTE+maxEncryptedTextSize;
        int bitsPerPixel = (int) (ColorChannel.getColorChannel(getColorChannelsValue()).toString().length() * jfxSliderLSB.getValue());
        MaxRequiredPixels = (int)(((maxTotalRequiredBytes * 8) /(float)bitsPerPixel)+0.9999f);

        labelTotalAvailablePixels.setText(TotalAvailablePixel+" Pixels");

        if (inputMessageLength == 0)
        {
            labelEncryptedTextSize.setText("∞ Bytes");
            labelMaxRequiredPixels.setText("∞ Pixels");
        }
        else{
            labelEncryptedTextSize.setText(maxEncryptedTextSize+" Bytes");
            labelMaxRequiredPixels.setText(MaxRequiredPixels+" Pixels");
        }

        labelTextSize.setText(inputMessageLength+" Bytes");
    }

    public void doStartEncoding(){
        System.out.println("\nStarted Encoding with");
        System.out.println("Cover Image File : "+selectedCoverImageFile);
        System.out.println("Output Image File : "+selectedOutputImageFile);
        System.out.println("ColorChannel : "+ColorChannel.getColorChannel(getColorChannelsValue()));
        System.out.println("Encryption Type: "+ EncryptionType.getEncryptionType(selectedEncryptionType));
        System.out.println("LSB Value: "+(int)jfxSliderLSB.getValue());
        System.out.println("Message : "+textAreaMessage.getText());


        EncodeCLIParameters encodeArgs = new EncodeCLIParameters();
        encodeArgs.message = textAreaMessage.getText();
        encodeArgs.encryptionType = selectedEncryptionType;
        encodeArgs.colorChannel = getColorChannelsValue();
        encodeArgs.noOfLSB = (int) (jfxSliderLSB.getValue()-1);
        if (selectedPublicKey!=null)
         encodeArgs.publicKeyFile = Path.of(selectedPublicKey.toString());
        encodeArgs.password = passwordField.getText();
        encodeArgs.coverImageFile = Path.of(selectedCoverImageFile.toString());
        encodeArgs.outputImageFile = Path.of(selectedOutputImageFile.toString());

        Effect effect = anchorPaneMain.getEffect();
        GaussianBlur blur = new GaussianBlur();
        blur.setInput(effect);
        blur.setRadius(13.57);
        anchorPaneMain.setEffect(blur);


        try {
            CLIExecutor.encode(encodeArgs);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Encoding Successful!!!\nOutput file saved as:"+selectedOutputImageFile);
            alert.setTitle("Success");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.initOwner((Stage)btnStartEncoding.getScene().getWindow());
            ButtonType okButton = new ButtonType("Finish", ButtonBar.ButtonData.FINISH);
            alert.getButtonTypes().setAll(okButton);
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData()== ButtonBar.ButtonData.FINISH)
                {
                    EncoderFX encoderFX = new EncoderFX();
                    encoderFX.display((Stage)btnStartEncoding.getScene().getWindow());
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Encoding Failed!!!");
            alert.setTitle("Error");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.initOwner((Stage)btnStartEncoding.getScene().getWindow());
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }

        anchorPaneMain.setEffect(effect);
    }


    public void doMinimizeApplication()
    {
        Stage stage = (Stage) btnMainMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void doCloseApplication()
    {
        System.out.println("Exit Button Pressed");
        Stage stage = (Stage) btnMainExit.getScene().getWindow();
        stage.close();
    }

    public void openGithub()
    {
        try {
        Desktop.getDesktop().browse(new URI("https://github.com/Master-COLLiDER"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
