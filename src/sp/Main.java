package sp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main extends Application
{
    //Set all public vars
    Stage window;
    Scene sceneMain, sceneTable, sceneStart;
    Label Header, SelectedProgsForPreset;
    Button[] buttons, EditBtn;
    Button addBtn, deleteBtn, selectBtn, deselectBtn, backBtn, acceptBtn, renamePresetsBtn;
    TextField nameInput, pathInput, urlInput;
    TextField [] inputs;
    TableView<Program> table;
    ObservableList<Program> programs = FXCollections.observableArrayList();


    File tekstfolder = new File(Reference.TEKSTFOLDER);
    File tempFolder = new File(Reference.TEMPFOLDER);
    File presetFolder = new File(Reference.PRESETFOLDER);
    File testFolder = new File(Reference.TESTFOLDER);

    int buttonheight;
    int inputscnt;
    boolean stringGev = false;
    Runtime rt = Runtime.getRuntime();
    String setSelected = "The selected programs for";

    public static void  main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //Makes it more readable
//        String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
//        System.out.println(newLine);
//        FILE MAKING
        if(!tekstfolder.exists() || !presetFolder.exists())
        {
            tekstfolder.createNewFile();
            presetFolder.createNewFile();
        }

        /*MAIN
        Text on sceneStart window*/
        Header = new Label("Set your presets");
        Header.setAlignment(Pos.CENTER);

        SelectedProgsForPreset = new Label("The selected programs for");
        SelectedProgsForPreset.setAlignment(Pos.CENTER_LEFT);

        /*Buttons
        "hardcoded" buttons */
        addBtn = new Button("Add");
        addBtn.setPrefWidth(65);
        addBtn.setOnAction(e -> addButtonClicked());

        deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(65);
        deleteBtn.setOnAction(e -> deleteButtonClicked());

        selectBtn = new Button("Enable");
        selectBtn.setPrefWidth(65);
        selectBtn.setOnAction(event -> selectButtonClicked());

        deselectBtn = new Button("Disable");
        deselectBtn.setPrefWidth(65);
        deselectBtn.setOnAction(event -> deselectButtonClicked());

        backBtn = new Button("Back");
        backBtn.setPrefWidth(60);
        backBtn.setPrefHeight(60);
        backBtn.setOnAction(event -> backButtonClicked());

        acceptBtn = new Button("Accept names and start program");
        acceptBtn.setOnAction(e -> acceptButtonClicked());

        renamePresetsBtn = new Button("Clear presets list");
        renamePresetsBtn.setOnAction(e -> deletePresetButtonClicked());

        /*Var buttons

        //MAIN button */
        buttons = new Button[5];
        String  b[]={"","","","",""};
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(b[i]);
            buttons[i].setPrefWidth(125);
            buttons[i].setPrefHeight(1);
            buttons[i].setAlignment(Pos.CENTER);
            if(MyFileReader.prognameGet(Reference.PRESETFOLDER, i + 1).toString().trim().equals("*") || MyFileReader.prognameGet(Reference.PRESETFOLDER, i + 1).toString().trim().equals("empty") )
            {
                buttons[i].setText("");
            }
            else
            {
                buttons[i].setText(MyFileReader.prognameGet(Reference.PRESETFOLDER, i + 1).toString().trim().split("\\[")[0]);
            }
            final int j = i;
            buttons[i].setOnAction(e -> myListenerOpening(j, buttons[j].getText()));
            if(buttons[i].getText().equals("")||buttons[i].getText().equals("empty"))
            {
                buttons[i].setMaxHeight(1);
                buttons[i].setDisable(true);
                buttons[i].setVisible(false);
            }
            else
            {
                buttonheight ++;
            }
        }

        //Edit button
        EditBtn = new Button[5];
        String  c[]={"Edit: "+ buttons[0].getText().trim(),
                "Edit: "+ buttons[1].getText().trim(),
                "Edit: "+ buttons[2].getText().trim(),
                "Edit: "+ buttons[3].getText().trim(),
                "Edit: "+ buttons[4].getText().trim()};

        for(int i = 0; i < EditBtn.length; i++)
        {
            EditBtn[i] = new Button(c[i]);
            EditBtn[i].setPrefWidth(170);
            EditBtn[i].setAlignment(Pos.BASELINE_LEFT);
            final int j = i;
            EditBtn[i].setOnAction(e -> {
                Reference.test = j + 1;
                window.setScene(sceneTable);
                window.setTitle("Startup Program: " + buttons[j].getText().trim());
                setSelectedProgsForPreset(buttons[j].getText().trim());
                window.show();
            });

            if(buttons[i].getText().equals("") ||buttons[i].getText().equals("empty"))
            {
                EditBtn[i].setDisable(true);
                EditBtn[i].setVisible(false);
            }
        }

        inputs = new TextField[5];
        String  a[]={"Preset button name","Preset button name","Preset button name","Preset button name","Preset button name"};
        for(int j = 0; j < inputs.length; j++) {
            inputs[j] = new TextField("");
            inputs[j].setPromptText(a[j]);
            inputs[j].setMinWidth(100);
            inputs[j].setMinHeight(25);
            inputs[j].setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    acceptButtonClicked();
            });
        }


        /*Table Columns
        Name column */
        TableColumn<Program, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Path column
        TableColumn<Program, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setMinWidth(350);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        //Url column
        TableColumn<Program, String> urlColumn = new TableColumn<>("Url");
        urlColumn.setMinWidth(255);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));


        /*Table rest
        Name input */
        nameInput = new TextField();
        nameInput.setPromptText("Program");
        nameInput.setMinWidth(100);
        nameInput.setOnKeyPressed(keyEvent ->
        {
            if (keyEvent.getCode() == KeyCode.ENTER)
                addButtonClicked();
        });

        //Path input
        pathInput = new TextField();
        pathInput.setPromptText("Path");
        pathInput.setOnKeyPressed(keyEvent ->
        {
            if (keyEvent.getCode() == KeyCode.ENTER)
                addButtonClicked();
        });

        //URL input
        urlInput = new TextField();
        urlInput.setPromptText("Url for browser");
        urlInput.setOnKeyPressed(keyEvent ->
        {
            if (keyEvent.getCode() == KeyCode.ENTER)
                addButtonClicked();
        });


        table = new TableView<>();
        table.getColumns().addAll(nameColumn, pathColumn, urlColumn);
        table.setPrefWidth(400);
        table.setItems(getPrograms());
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        /*LAYOUTMANAGER
        Layout sceneStart*/
        VBox layoutStart = new VBox();
        layoutStart.setPadding(new Insets(15));
        layoutStart.setSpacing(10);
        layoutStart.getChildren().add(Header);
        layoutStart.getChildren().addAll(inputs);
        layoutStart.getChildren().addAll(acceptBtn);

        //Layout sceneMain
        VBox leftSide = new VBox();
        leftSide.setSpacing(5);
        leftSide.setPadding(new Insets(2.5));
        leftSide.setAlignment(Pos.TOP_LEFT);
        leftSide.getChildren().addAll(buttons);

        VBox rightSide = new VBox();
        rightSide.setSpacing(5);
        rightSide.setPadding(new Insets(2.5));
        rightSide.setAlignment(Pos.TOP_LEFT);
        rightSide.getChildren().addAll(EditBtn);

        HBox top = new HBox();
        top.getChildren().add(renamePresetsBtn);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(2.5));

        BorderPane layoutMain = new BorderPane();
        layoutMain.setBottom(top);

        layoutMain.setLeft(leftSide);
        layoutMain.setRight(rightSide);

        //Layout sceneTable
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(selectBtn, addBtn);

        VBox vBox1 = new VBox();
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(deselectBtn, deleteBtn);

        HBox hBoxempty = new HBox();
        hBoxempty.setPrefHeight(35);
        HBox hboxInput = new HBox();
        hboxInput.setSpacing(10);
        hboxInput.getChildren().addAll(nameInput, pathInput, urlInput);

        VBox vBoxAlign = new VBox();
        vBoxAlign.getChildren().addAll(hBoxempty, hboxInput);

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10, 10, 10, 10));
        hBox2.setSpacing(10);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(vBoxAlign, vBox, vBox1, backBtn);

        VBox vBox3 = new VBox();
        vBox3.getChildren().addAll(table, SelectedProgsForPreset, hBox2);

        HBox layoutTable = new HBox();
        layoutTable.setPadding(new Insets(10));
        layoutTable.setSpacing(10);
        layoutTable.setAlignment(Pos.CENTER);
        layoutTable.getChildren().addAll(vBox3);

        //SCENE and WINDOW Manager
        sceneStart = new Scene(layoutStart,250,300);
        sceneMain = new Scene(layoutMain,310,180);
        sceneTable = new Scene(layoutTable);
        window = primaryStage;
        if(presetFolder.length() == 0)
        {
            window.setScene(sceneStart);
            window.setTitle("Startup presets: " + "Presets");
        }
        else
        {
            window.setScene(sceneMain);
            window.setTitle("Startup presets: " + "MAIN");
        }
        window.show();
    }


    /*Methods
    //Button handlers

    //Delete program, path,(url) from the table and file */
    public void deleteButtonClicked()
    {
        try
        {
            ObservableList<Program> programSelected, allPrograms;
            allPrograms = table.getItems();
            programSelected = table.getSelectionModel().getSelectedItems();
            for (int i = 0; i < programSelected.size(); i++)
            {
                String test = programSelected.toString().split(",")[i].replace("[", "").replace("]", "").trim().split("  ")[0];
                replacer(test, "");
            }
            programSelected.forEach(allPrograms::remove);
        }
        catch (Exception e)
        {
            //            e.printStackTrace();
        }
    }


    //Add program, path,(url) to the table and file
    public void addButtonClicked()
    {
        System.out.println("CLICKED");
        if(nameInput.getText().length() > 0 && pathInput.getText().length() >5)
        {
            Program program = new Program();
            program.setName(nameInput.getText());
            program.setPath(pathInput.getText().replace("\"",""));
            program.setUrl(urlInput.getText());
            writeToTextFile(nameInput.getText().trim(), pathInput.getText().trim(), urlInput.getText().trim());
            table.getItems().add(program);
            nameInput.clear();
            pathInput.clear();
            urlInput.clear();
        }
        else
        {
            if(nameInput.getText().length() <= 0)
               alertBox("Name of the program is to short.", "Set a longer name");
            if(pathInput.getText().length() <= 0)
               alertBox("Path of the program is to short.", "Get the full path.");
        }
        nameInput.requestFocus();
    }


    //Select progams for a preset
    public void selectButtonClicked()
    {
        try
        {
            ObservableList<Program> programSelected;
            String Presetname = MyFileReader.prognameGet(Reference.PRESETFOLDER, Reference.test).toString().split("  ")[0].trim();
            List<String> strProgs = new ArrayList<>();
            programSelected = table.getSelectionModel().getSelectedItems();
            for(int i= 0; i < programSelected.size(); i++)
            {
                String test = programSelected.toString().split(",")[i].replace("[","").replace("]", "").trim().split("  ")[0];
                strProgs.add(test);
            }
            writeSelected(Presetname, strProgs);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Deselect programs from a preset
    public void deselectButtonClicked()
    {
        try
        {
            ObservableList<Program> programSelected;
            String Presetname = MyFileReader.prognameGet(Reference.PRESETFOLDER, Reference.test).toString().split("  ")[0].trim();
            List<String> strProgs = new ArrayList<>();
            programSelected = table.getSelectionModel().getSelectedItems();
            for(int i= 0; i < programSelected.size(); i++)
            {
                String test = programSelected.toString().split(",")[i].replace("[","").replace("]", "").trim().split("  ")[0];
                strProgs.add(test);
            }
            writeDeselected(Presetname, strProgs);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Go back a scene
    //TODO: Make it so i can use it multiple times, not only for table -> main
    public void backButtonClicked()
    {
        window.setScene(sceneMain);
        window.setTitle("Startup Program: " + "MAIN");
    }


    //Set preset Names
    public void acceptButtonClicked()
    {
        for (TextField input : inputs)
        {
            if (isInteger(input.getText()))
            {
                writeToPresetFile(input.getText());
            }
            else
            {
                writeToPresetFile(input.getText());
            }
        }
        try
        {
            //Start a new .jar file
            rt.exec("cmd /c start " +Reference.MAIN);
            window.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //Resetting names of the presets
    public void deletePresetButtonClicked()
    {
        presetFolder.delete();
        try
        {
            presetFolder.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        window.close();
        window.setScene(sceneStart);
        window.show();

    }


    public void myListenerOpening(int btnNum, String Presetname)
    {
        String program1 = "";
        String Path;
        System.out.println("HERE");
        try
        {
            if(MyFileReader.prognameGet(Reference.PRESETFOLDER, btnNum+1).toString().contains("["))
            {
                program1 = MyFileReader.prognameGet(Reference.PRESETFOLDER, btnNum + 1).toString().split("\\[")[1].replaceAll("[\\[\\]]", " ").trim();
            }
            BufferedReader file = new BufferedReader(new FileReader(Reference.PRESETFOLDER));
            String line;
            int commas = 0;

            while ((line = file.readLine()) != null)
            {
                if(line.contains(Presetname))
                {
                    for (int i = 0; i < line.length(); i++)
                    {
                        if (line.charAt(i) == ',')
                            commas++;
                    }
                }
            }
            file.close();
            System.out.println("COMMA: " + commas);
            //Number of progs: commas + 1
            if(commas != 0)
            {
                for (int i = 0; i < commas + 1; i++)
                {
                    System.out.println("Program: " + program1);
                    String Temp = program1.split(", ")[i];
                    BufferedReader file2 = new BufferedReader(new FileReader(Reference.TEKSTFOLDER));
                    System.out.println("ARRAY: " + Temp);
                    String line2;

                    while ((line2 = file2.readLine()) != null)
                    {
                        if(line2.contains(Temp))
                        {
                            System.out.println(line2.contains("not set"));
                            Path = "\"" + line2.split("\"")[1] + "\"";
                            //Has an URL
                            if (!line2.contains("not set"))
                            {
                                String url = "\"" + line2.split("\"")[2].trim() + "\"";
                                System.out.println(url);
                                try
                                {
                                    Process clientProcess = rt.exec(new String[]{Path, url});
                                    System.out.println(Temp + " is starting up with url" + url );
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {

                                System.out.println(Path);
                                Path = "cmd /c " + Path;
                                rt.exec(Path);
                                System.out.println(Temp + " is starting up");
                            }
                        }
                    }
                    file2.close();
                }
            }

            if(commas == 0)
            {
                System.out.println("Program: " + program1);
                BufferedReader file2 = new BufferedReader(new FileReader(Reference.TEKSTFOLDER));
                String line2;
                while ((line2 = file2.readLine()) != null)
                {
                    if(line2.contains(program1 + "  \""))
                    {
                        System.out.println("not set: " + line2.contains("not set"));
                        Path = "\"" + line2.split("\"")[1] + "\"";
                        //Has an URL
                        if (!line2.contains("not set"))
                        {
                            String url = "\"" + line2.split("\"")[2].trim() + "\"";
                            System.out.println(url);
                            try
                            {
                                Process clientProcess = rt.exec(new String[]{Path, url});
                                System.out.println(program1 + " is starting up with url" + url );
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {

                            System.out.println(Path);
                            Path = "cmd /c " + Path;
                            rt.exec(Path);
                            System.out.println(program1 + " is starting up");
                        }
                    }
                }
                file2.close();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //OTHERS
    //Get all of the programs
    public ObservableList<Program> getPrograms()
    {
        try
        {
            for (int i = 0; i < 99; i++)
            {
                String Temp = MyFileReader.prognameGet(Reference.TEKSTFOLDER, i + 1).toString();
                if (Temp.length() == 5)
                {
                    i++;
                }
                if (!Objects.equals(Temp, "*"))
                {
                    programs.add(new Program(MyFileReader.prognameGet(Reference.TEKSTFOLDER, i + 1).toString().split("  ")[0],
                                             MyFileReader.prognameGet(Reference.TEKSTFOLDER, i + 1).toString().split("  ")[1].replace("\"", ""),
                                             MyFileReader.prognameGet(Reference.TEKSTFOLDER, i + 1).toString().split("  ")[2]));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return programs;
    }


    //Write table to text.txt
    public void writeToTextFile(String Name, String path, String url)
    {
        //If I changes the space here,I need to change it in getPrograms too.
        if(url.length() ==0)
        {
            url = "not set";
        }
        String editPath = path.replace("\"", "");
        editPath = "\"" + editPath + "\"";
        System.out.println(editPath);

        String output = Name.trim() + "  " + editPath.trim() + "  " + url.trim() + "  " + "\n";
        {
            try
            {
                FileWriter file1 = new FileWriter(Reference.TEKSTFOLDER, true);
                file1.append(output);
                file1.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    //Write Presets to Presets.txt
    public void writeToPresetFile(String PresetName)
    {
        String output;
        if(PresetName.equals(""))
        {

            output = PresetName;
        }
        else
        {
            output = PresetName + "\n";
        }
        try
        {
            FileWriter file1 = new FileWriter(Reference.PRESETFOLDER, true);
            file1.append(output);
            file1.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //Write selected programs to the right Preset in Presets.txt
    public void writeSelected(String PresetName,  List<String> Progs)
    {
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(Reference.PRESETFOLDER));
            FileOutputStream fileOut = new FileOutputStream(Reference.TEMPFOLDER);
            String line;
            String input = "";
            while ((line = file.readLine()) != null)
            {
                input += line + "\n";
                System.out.println(line.startsWith(PresetName) && !line.contains("["));
                if(line.startsWith(PresetName) && !line.contains("["))
                {
                    System.out.println("INP old "+ input);
                    input = input.replace(line, PresetName + "  " + Progs);
                    System.out.println("INP new " + input);
                }
                else if(line.startsWith(PresetName) && line.contains("["))
                {
                    String Temp = line.split("\\[")[1].replace("]","");
                    String Tempnew;
                    for (int i = 0; i < Progs.size(); i++)
                    {
                        String TempProgs = Progs.toString().split(", ")[i].replace("[","").replace("]","");
                        if(!Temp.contains(TempProgs))
                        {
                            Temp = Temp + ", " + TempProgs;
                            System.out.println(Temp);
                        }

                    }
                    if(!Temp.contains("["))
                    {
                        Temp = "[" + Temp + "]";
                    }
                    input = input.replace(line, PresetName + "  " + Temp);
                    System.out.println("INPUT: " + input);
                }
            }
            fileOut.write(input.getBytes());
            fileOut.close();
            file.close();
            presetFolder.delete();
            tempFolder.renameTo(presetFolder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Remove selected programs from the right Preset in Presets.txt
    public void writeDeselected(String PresetName,  List<String> Progs)
    {
        try {
            // input the file content to the String "input"
            BufferedReader file = new BufferedReader(new FileReader(Reference.PRESETFOLDER));
            FileOutputStream fileOut = new FileOutputStream(Reference.TEMPFOLDER);
            String line;
            String input = "";
            while ((line = file.readLine()) != null)
            {
                input += line + "\n";
                if(line.startsWith(PresetName) && line.contains("["))
                {
                    String Temp = line.split("\\[")[1].replace("]","");
                    for (int i = 0; i < Progs.size(); i++)
                    {
                        String TempProgs = Progs.toString().split(", ")[i].replace("[", "").replace("]", "");
                        if (Temp.contains(TempProgs))
                        {
                            if(line.contains(TempProgs+ ","))
                            {
                                Temp = Temp.replace(TempProgs + ", ", "");
                            }
                            if(!line.contains(TempProgs+","))
                            {
                                Temp = Temp.replace(TempProgs, "");
                            }
                        }
                    }
                    input = input.replace(line, PresetName + "  " + "[" + Temp + "]");
                    if(input.contains("[]"))
                        input = input.replace("[]","");
                }
            }
            fileOut.write(input.getBytes());
            fileOut.close();
            file.close();
            presetFolder.delete();
            tempFolder.renameTo(presetFolder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Set the text for selected
    public void setSelectedProgsForPreset(String PresetName)
    {
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(Reference.PRESETFOLDER));
            String line;
            String input = "";
            while ((line = file.readLine()) != null)
            {
                input += line + "\n";
                System.out.println(line.startsWith(PresetName));
                if(line.startsWith(PresetName) && line.contains("["))
                {
                    SelectedProgsForPreset.setText(setSelected + " " +line.toString().split("\\[")[0]+ ": " + line.toString().split("\\[")[1].replace("]",""));
                }
                if(line.startsWith(PresetName) && !line.contains("["))
                {
                    SelectedProgsForPreset.setText(setSelected + " " +line.toString().split("\\[")[0]+ ": ");
                }

            }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Replace specific words/lines from a file
    public void replacer(String Old, String New)
    {

        try {
            // input the file content to the String "input"
            BufferedReader file = new BufferedReader(new FileReader(Reference.TEKSTFOLDER));
            FileOutputStream fileOut = new FileOutputStream(Reference.TESTFOLDER);
            String line;
            String input = "";

            while ((line = file.readLine()) != null)
            {
                input += line + '\n';
                if (input.contains(Old) && !Old.equals("not selected"))
                {
                    // "\n" is needed for removing a empty lines
                    input = input.replace(line + "\n", New);
                }
            }
            fileOut.write(input.getBytes());
            fileOut.close();
            file.close();

            tekstfolder.delete();
            testFolder.renameTo(tekstfolder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Shows an ERROR box when a error appears
    public void alertBox(String Error, String Fix)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Startup program error messenger");
        alert.setHeaderText(Error);
        alert.setContentText(Fix);
        alert.showAndWait();
    }


    //Check if the input is a Int and make it into a String
    public static boolean isInteger(String str)
    {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }
}