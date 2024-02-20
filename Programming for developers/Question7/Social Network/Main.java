package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class the implements the components and functionality of the program
 */
public class Main extends Application {
	private SocialNetwork socialNetwork = new SocialNetwork();
	private User centralUser = null;

	private static final int WINDOW_WIDTH = 800; // width of window
	private static final int WINDOW_HEIGHT = 800; // height of window
	// name of application
	private static final String APP_TITLE = "Social Network";

	// list of users
	private final ObservableList<String> USERS = FXCollections.observableArrayList();
	// list of friends
	private final ObservableList<String> FRIENDS = FXCollections.observableArrayList();

	private Stage PRIMARY_STAGE = null; // primary stage for the program
	private Scene startScene;
	private Scene userScene;

	// Components
	ImageView image;
	Label name;
	Label description;
	ComboBox<String> removeFriendBox;
	ListView<String> friendList;
	ListView<String> userList;
	Label numFriends;
	Label numUsers;
	Label numGroups;

	// Sections
	private HBox infoSection; // user information
	private VBox functionSection; // contains all functions
	private VBox listSection; // displays list of friends, and list of users

	// Pages
	private BorderPane startPage; // start page
	private HBox userPage; // current user page

	/**
	 * Runs the program
	 *
	 * @param args (unused)
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Sets up the components and displays the sign in page
	 *
	 * @param primaryStage - stage for the program
	 */
	@Override
	public void start(Stage primaryStage) {
		// Set up sign in page
		setUpStartPage();

		// Set up user page
		setUpUserPage();

		// Adjust widths
		HBox.setHgrow(startPage, Priority.ALWAYS);
		HBox.setHgrow(userPage, Priority.ALWAYS);

		// Set stage
		PRIMARY_STAGE = primaryStage;

		// Create new scene
		startScene = new Scene(startPage, WINDOW_WIDTH, WINDOW_HEIGHT);
		userScene = new Scene(userPage, WINDOW_WIDTH, WINDOW_HEIGHT);

		startScene.getStylesheets().add("application/startStyle.css");
		userScene.getStylesheets().add("application/userStyle.css");

		// Show sign in page
		PRIMARY_STAGE.setTitle(APP_TITLE);
		PRIMARY_STAGE.setScene(startScene);
		PRIMARY_STAGE.show();

		primaryStage.setMinWidth(WINDOW_WIDTH);
		primaryStage.setMinHeight(WINDOW_HEIGHT);
	}

	/**
	 * Sets up log in page
	 */
	private void setUpStartPage() {
		// Components
		BorderPane borderPaneStart = new BorderPane();
		borderPaneStart.setPadding(new Insets(WINDOW_WIDTH / 4.0));

		// Labels
		Label labelWelcome = new Label("Welcome!");
		Label labelUsername = new Label("Username");
		Label labelImport = new Label("Import File");

		// Text fields
		TextField textFieldUsername = new TextField();
		TextField textFieldImport = new TextField();

		// Buttons
		Button buttonStart = new Button("STRT");
		Button buttonImport = new Button("IMP");

		// Layouts
		HBox hBoxUsername = new HBox(4);
		hBoxUsername.getChildren().addAll(textFieldUsername, buttonStart);

		HBox hBoxImport = new HBox(4);
		hBoxImport.getChildren().addAll(textFieldImport, buttonImport);

		// Adjust widths
		HBox.setHgrow(textFieldUsername, Priority.ALWAYS);
		HBox.setHgrow(textFieldImport, Priority.ALWAYS);

		// Layouts
		VBox vBoxUsername = new VBox(8);
		vBoxUsername.getChildren().addAll(labelUsername, hBoxUsername);

		VBox vBoxImport = new VBox(8);
		vBoxImport.getChildren().addAll(labelImport, hBoxImport);

		VBox vBoxStartEntry = new VBox(20);
		vBoxStartEntry.getChildren().addAll(vBoxUsername, vBoxImport);
		vBoxStartEntry.setAlignment(Pos.CENTER);

		borderPaneStart.setTop(labelWelcome);
		borderPaneStart.setCenter(vBoxStartEntry);

		// Functionality
		EventHandler<ActionEvent> startEventHandler = startEvent -> {
			if ((textFieldUsername.getText().trim().isEmpty()
					|| !isValidUser(textFieldUsername.getText())
					|| (textFieldUsername.getText().split(" ").length > 1))) {

				Alert usernameAlert = new Alert(AlertType.ERROR,
						"Please enter a valid username");
				usernameAlert.show();

			} else {
				socialNetwork.addUser(textFieldUsername.getText().trim());
				socialNetwork.setCentralUser(textFieldUsername.getText().trim());

				centralUser = socialNetwork.getUser(textFieldUsername.getText().trim());

				updateCentralUser();
				textFieldUsername.clear();
				PRIMARY_STAGE.setScene(userScene);
			}

		};

		buttonStart.setOnAction(startEventHandler);
		textFieldUsername.setOnAction(startEventHandler);

		EventHandler<ActionEvent> importEventHandler = importEvent -> {
			if (textFieldImport.getText().trim().isEmpty()) {
				Alert importAlert = new Alert(AlertType.ERROR,
						"Please enter file name");
				importAlert.show();

			} else {
				if (socialNetwork.importFile(textFieldImport.getText().trim())) {
					String cUser = socialNetwork.getCentralUser();

					if (cUser == null) {
						Alert importAlert = new Alert(AlertType.ERROR,
								"No users present on the network. \n"
										+ "Cannot set the central user. \n"
										+ "Pls import a valid file.");
						importAlert.show();
					} else {

						centralUser = socialNetwork.getUser(cUser);

						updateCentralUser();
						PRIMARY_STAGE.setScene(userScene);
					}

				} else {
					Alert importAlert = new Alert(AlertType.ERROR,
							"Error importing " + textFieldImport.getText().trim());
					importAlert.show();
				}

				textFieldImport.clear();
			}

		};

		buttonImport.setOnAction(importEventHandler);
		textFieldImport.setOnAction(importEventHandler);

		startPage = borderPaneStart;
	}

	/**
	 * Sets up user page
	 */
	private void setUpUserPage() {

		// Components
		HBox hBoxUserPage = new HBox(20);

		// Set up profile
		setUpInfoSection();

		// Set up functions
		setUpFunctionSection();

		// Set up friends list
		setUpListSection();

		// Layouts
		VBox vBoxUser = new VBox(20);
		vBoxUser.getChildren().addAll(infoSection, functionSection);
		vBoxUser.setPadding(new Insets(20));

		VBox vBoxFriendList = new VBox(8);
		vBoxFriendList.getChildren().add(listSection);
		vBoxFriendList.setPadding(new Insets(20));

		// Adjust widths
		vBoxUser.setMinWidth(WINDOW_WIDTH / 2.0);
		vBoxFriendList.setMinWidth(WINDOW_WIDTH / 2.0 - 20);
		HBox.setHgrow(vBoxUser, Priority.ALWAYS);

		hBoxUserPage.getChildren().addAll(vBoxUser, vBoxFriendList);
		userPage = hBoxUserPage;
	}

	/**
	 * Sets up user profile
	 */
	private void setUpInfoSection() {
		// Components
		HBox hBoxInfo = new HBox(8);

		// Images
		ImageView imageViewUser = new ImageView(new Image("file:default1.jpg"));
		imageViewUser.setPreserveRatio(true);
		imageViewUser.setFitHeight(100);

		image = imageViewUser;

		// Labels
		Label labelName = new Label("No name");
		Label labelDescription = new Label("No Description");

		name = labelName;
		description = labelDescription;

		labelName.setStyle("-fx-font-size: 18;");

		// Layouts
		VBox vBoxInfoDescription = new VBox(8);
		vBoxInfoDescription.getChildren().addAll(labelName, labelDescription);

		hBoxInfo.getChildren().addAll(imageViewUser, vBoxInfoDescription);

		// Adjust widths
		HBox.setHgrow(vBoxInfoDescription, Priority.ALWAYS);

		infoSection = hBoxInfo;
	}

	/**
	 * Sets up all functions
	 */
	private void setUpFunctionSection() {
		// Components
		VBox vBoxFunctions = new VBox(12);

		// Set up user related functionality
		Pane userFunctionSection = setUpUserFunctionSection();

		// Set up friend related functionality
		Pane friendFunctionSection = setUpFriendFunctionSection();

		// Set up file related functionality
		Pane fileFunctionSection = setUpFileFunctionSection();

		// Set up clear and exit functionality
		Pane clearExitSection = setUpClearExitSection();

		vBoxFunctions.getChildren().addAll(userFunctionSection, friendFunctionSection, fileFunctionSection,
				clearExitSection);
		functionSection = vBoxFunctions;
	}

	/**
	 * Sets up user related functionality
	 */
	private Pane setUpUserFunctionSection() {
		// Components
		VBox vBoxUserFunction = new VBox(12);

		// Label
		Label labelAddUser = new Label("Add User");
		Label labelRemoveUser = new Label("Remove User");
		Label labelSearchUser = new Label("Search User");

		// Text fields
		TextField textFieldAddUser = new TextField();
		TextField textFieldRemoveUser = new TextField();
		TextField textFieldSearchUser = new TextField();

		// Buttons
		Button buttonAddUser = new Button("ADD");
		Button buttonRemoveUser = new Button("REM");
		Button buttonSearchUser = new Button("SRCH");

		// Layouts
		HBox hBoxAddUser = new HBox(4);
		hBoxAddUser.getChildren().addAll(textFieldAddUser, buttonAddUser);

		HBox hBoxRemoveUser = new HBox(4);
		hBoxRemoveUser.getChildren().addAll(textFieldRemoveUser, buttonRemoveUser);

		HBox hBoxSearchUser = new HBox(4);
		hBoxSearchUser.getChildren().addAll(textFieldSearchUser, buttonSearchUser);

		// Layouts
		VBox vBoxAddUser = new VBox(8);
		vBoxAddUser.getChildren().addAll(labelAddUser, hBoxAddUser);

		VBox vBoxRemoveUser = new VBox(8);
		vBoxRemoveUser.getChildren().addAll(labelRemoveUser, hBoxRemoveUser);

		VBox vBoxSearchUser = new VBox(8);
		vBoxSearchUser.getChildren().addAll(labelSearchUser, hBoxSearchUser);

		// Adjust Widths
		HBox.setHgrow(textFieldAddUser, Priority.ALWAYS);
		HBox.setHgrow(textFieldRemoveUser, Priority.ALWAYS);
		HBox.setHgrow(textFieldSearchUser, Priority.ALWAYS);

		// Add user function
		EventHandler<ActionEvent> addUserHandler = addUserEvent -> {
			if (!textFieldAddUser.getText().trim().isEmpty()) {
				if (!socialNetwork.addUser(textFieldAddUser.getText().trim())) {
					Alert addUserAlert = new Alert(AlertType.ERROR, "Invalid entry");
					addUserAlert.show();
				}
				updateCentralUser();
			}
			textFieldAddUser.clear();
		};

		buttonAddUser.setOnAction(addUserHandler);
		textFieldAddUser.setOnAction(addUserHandler);

		// Remove user function
		EventHandler<ActionEvent> removeUserHandler = removeUserEvent -> {
			if (!textFieldRemoveUser.getText().trim().isEmpty()) {
				// Checks if this is central user
				if (textFieldRemoveUser.getText().trim().equals(centralUser.getName())) {
					List<String> users = socialNetwork.getAllUsers();
					// Checks if central user is the only user in program
					if (users.size() > 1) {
						// If not last in program, then remove user and notify that
						// the central user has been updated
						socialNetwork.removeUser(textFieldRemoveUser.getText().trim());
						Alert exportAlert = new Alert(AlertType.INFORMATION, "Central User Removed and Updated");
						exportAlert.show();
						users = socialNetwork.getAllUsers();
						centralUser = socialNetwork.getUser(users.get(0));
						updateCentralUser();
					} else {
						// If this is the last user in the entire social network
						// then provide an option to save or cancel decison
						// before exiting the program
						removeLastUserAndSave();
					}

				} else {
					// Check if it is possible to remove a user and prints error
					// if they do not exist
					if (!socialNetwork.removeUser(textFieldRemoveUser.getText().trim())) {
						Alert addUserAlert = new Alert(AlertType.ERROR, "Cannot Remove User That Does Not Exist");
						addUserAlert.show();
					}
					// Update what is shown to screen
					updateCentralUser();
				}
			}
			textFieldRemoveUser.clear();
		};

		buttonRemoveUser.setOnAction(removeUserHandler);
		textFieldRemoveUser.setOnAction(removeUserHandler);

		// Search user function
		EventHandler<ActionEvent> searchUserHandler = searchUserEvent -> {
			if (!textFieldSearchUser.getText().trim().isEmpty()) {
				// Checks if the user exits
				User name = socialNetwork.getUser(textFieldSearchUser.getText().trim());
				if (name == null) {
					// If the user does not exit, then print that the user is not found
					Alert exportAlert = new Alert(AlertType.ERROR,
							textFieldSearchUser.getText().trim() + " does not exist");
					exportAlert.show();
				} else {
					// If the user does exist in the social network, then update
					// central user
					// socialNetwork.removeFriend(textFieldSearchUser.getText().trim()); // TODO WHY
					// WERE WE REMOVING A FRIEND IN SEARCH? IT DOESN'T SEEM TO HURT THE PROGRAM, BUT
					// MAYBE YOU FOUND AN EXCEPTION I DIDNT
					socialNetwork.setCentralUser(textFieldSearchUser.getText().trim());
					centralUser = socialNetwork.getUser(textFieldSearchUser.getText().trim());
					updateCentralUser();
				}

			}
			textFieldSearchUser.clear();
		};

		buttonSearchUser.setOnAction(searchUserHandler);
		textFieldSearchUser.setOnAction(searchUserHandler);

		vBoxUserFunction.getChildren().addAll(vBoxAddUser, vBoxRemoveUser, vBoxSearchUser);
		return vBoxUserFunction;
	}

	/**
	 * Sets up friend related functionality
	 */
	private Pane setUpFriendFunctionSection() {
		// Components
		VBox vBoxFriendFunction = new VBox(12);

		// Layouts
		Label labelAddFriend = new Label("Add Friend");
		Label labelRemoveFriend = new Label("Remove Friend");
		Label labelMutualFriends = new Label("Mutual Friends");
		Label labelShortestPath = new Label("Shortest Friendship Path");

		// Text Fields
		TextField textFieldAddFriend = new TextField();
		TextField textFieldMutualUser1 = new TextField();
		TextField textFieldMutualUser2 = new TextField();
		TextField textFieldShortestPathUser1 = new TextField();
		TextField textFieldShortestPathUser2 = new TextField();

		// Combo Boxes
		ComboBox<String> comboBoxRemoveFriend = new ComboBox<>(FRIENDS);

		comboBoxRemoveFriend.setPromptText("Select Friend");
		removeFriendBox = comboBoxRemoveFriend;

		// Buttons
		Button buttonAddFriend = new Button("ADD");
		Button buttonRemoveFriend = new Button("REM");
		Button buttonMutualFriends = new Button("SRCH");
		Button buttonShortestPath = new Button("SRCH");

		// Layouts
		HBox hBoxAddFriendField = new HBox(4);
		hBoxAddFriendField.getChildren().addAll(textFieldAddFriend, buttonAddFriend);

		HBox hBoxRemoveFriend = new HBox(4);
		hBoxRemoveFriend.getChildren().addAll(comboBoxRemoveFriend, buttonRemoveFriend);

		HBox hBoxMutualFriendsEntry = new HBox(4);
		hBoxMutualFriendsEntry.getChildren().addAll(textFieldMutualUser1, textFieldMutualUser2, buttonMutualFriends);

		HBox hBoxShortestPathEntry = new HBox(4);
		hBoxShortestPathEntry.getChildren().addAll(textFieldShortestPathUser1, textFieldShortestPathUser2,
				buttonShortestPath);

		// Adjust widths
		comboBoxRemoveFriend.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(textFieldAddFriend, Priority.ALWAYS);
		HBox.setHgrow(comboBoxRemoveFriend, Priority.ALWAYS);
		HBox.setHgrow(textFieldMutualUser1, Priority.ALWAYS);
		HBox.setHgrow(textFieldMutualUser2, Priority.ALWAYS);
		HBox.setHgrow(textFieldShortestPathUser1, Priority.ALWAYS);
		HBox.setHgrow(textFieldShortestPathUser2, Priority.ALWAYS);

		// Layouts
		VBox vBoxAddFriend = new VBox(8);
		vBoxAddFriend.getChildren().addAll(labelAddFriend, hBoxAddFriendField);

		VBox vBoxRemoveFriend = new VBox(8);
		vBoxRemoveFriend.getChildren().addAll(labelRemoveFriend, hBoxRemoveFriend);

		VBox vBoxMutualFriends = new VBox(8);
		vBoxMutualFriends.getChildren().addAll(labelMutualFriends, hBoxMutualFriendsEntry);

		VBox vBoxShortestPath = new VBox(8);
		vBoxShortestPath.getChildren().addAll(labelShortestPath, hBoxShortestPathEntry);

		// Functionality
		// Add friend function
		EventHandler<ActionEvent> addFriendHandler = addFriendEvent -> {
			if (!textFieldAddFriend.getText().trim().isEmpty()) {
				socialNetwork.addFriend(textFieldAddFriend.getText().trim());
				updateCentralUser();
			}

			textFieldAddFriend.clear();
		};

		buttonAddFriend.setOnAction(addFriendHandler);
		textFieldAddFriend.setOnAction(addFriendHandler);

		// Remove friend function
		EventHandler<ActionEvent> removeFriendHandler = removeFriendEvent -> {
			if (comboBoxRemoveFriend.getSelectionModel().getSelectedItem() != null) {
				socialNetwork.removeFriend(comboBoxRemoveFriend.getSelectionModel().getSelectedItem());
				updateCentralUser();
			}

		};

		buttonRemoveFriend.setOnAction(removeFriendHandler);

		// Mutual Friends Function
		EventHandler<ActionEvent> mutualFriendsHandler = mutualFriendsEvent -> {
			if (!textFieldMutualUser1.getText().trim().isEmpty() && !textFieldMutualUser2.getText().trim().isEmpty()) {
				List<String> mutualFriendsList = socialNetwork.getMutualFriends(textFieldMutualUser1.getText().trim(),
						textFieldMutualUser2.getText().trim());

				setUpListDialog(mutualFriendsList, "Mutual Friends");
			}
		};

		buttonMutualFriends.setOnAction(mutualFriendsHandler);
		textFieldMutualUser1.setOnAction(mutualFriendsHandler);
		textFieldMutualUser2.setOnAction(mutualFriendsHandler);

		// Shortest Path Function
		EventHandler<ActionEvent> shortestPathHandler = shortestPathEvent -> {
			if (!textFieldShortestPathUser1.getText().trim().isEmpty()
					&& !textFieldShortestPathUser2.getText().trim().isEmpty()) {
				User user1 = socialNetwork.getUser(textFieldShortestPathUser1.getText().trim());
				User user2 = socialNetwork.getUser(textFieldShortestPathUser2.getText().trim());
				if (user1 == null || user2 == null) {
					Alert importAlert = new Alert(AlertType.ERROR,
							"Error Finding Friendship between " + textFieldShortestPathUser1.getText().trim() + " and "
									+ textFieldShortestPathUser2.getText().trim());
					importAlert.show();
				} else {
					List<String> shortestPathList = socialNetwork.getShortestPath(
							textFieldShortestPathUser1.getText().trim(), textFieldShortestPathUser2.getText().trim());

					setUpListDialog(shortestPathList, "Shortest Friendship Path");
				}
			}
		};

		buttonShortestPath.setOnAction(shortestPathHandler);
		textFieldShortestPathUser1.setOnAction(shortestPathHandler);
		textFieldShortestPathUser2.setOnAction(shortestPathHandler);

		vBoxFriendFunction.getChildren().addAll(vBoxAddFriend, vBoxRemoveFriend, vBoxMutualFriends, vBoxShortestPath);
		return vBoxFriendFunction;
	}

	/**
	 * Sets up file related functionality
	 */
	private Pane setUpFileFunctionSection() {
		// Components
		VBox vBoxFileFunction = new VBox(12);

		// Layouts
		Label labelImportFile = new Label("Import File");
		Label labelExportFile = new Label("Export File");

		// Text fields
		TextField textFieldImportFile = new TextField();
		TextField textFieldExportFile = new TextField();

		textFieldExportFile.setText("saveLog.txt");

		// Buttons
		Button buttonImport = new Button("IMP");
		Button buttonExport = new Button("EXP");

		// Layouts
		HBox hBoxImportFile = new HBox(4);
		hBoxImportFile.getChildren().addAll(textFieldImportFile, buttonImport);

		HBox hBoxExportFile = new HBox(4);
		hBoxExportFile.getChildren().addAll(textFieldExportFile, buttonExport);

		// Layouts
		HBox.setHgrow(textFieldImportFile, Priority.ALWAYS);
		HBox.setHgrow(textFieldExportFile, Priority.ALWAYS);

		// Layouts
		VBox vBoxImportFile = new VBox(8);
		vBoxImportFile.getChildren().addAll(labelImportFile, hBoxImportFile);

		VBox vBoxExportFile = new VBox(8);
		vBoxExportFile.getChildren().addAll(labelExportFile, hBoxExportFile);

		// Functionality
		EventHandler<ActionEvent> importHandler = importEvent -> {
			if (!textFieldImportFile.getText().trim().isEmpty()) {

				if (socialNetwork.importFile(textFieldImportFile.getText().trim())) {
					centralUser = socialNetwork.getUser(socialNetwork.getCentralUser());
					updateCentralUser();

					Alert importAlert = new Alert(AlertType.INFORMATION,
							"Succesfully imported " + textFieldExportFile.getText().trim());
					importAlert.show();

				} else {
					Alert importAlert = new Alert(AlertType.ERROR,
							"Error importing " + textFieldImportFile.getText().trim());
					importAlert.show();
				}

			} else {
				Alert importAlert = new Alert(AlertType.ERROR, "Enter file name");
				importAlert.show();
			}

			textFieldImportFile.clear();
		};

		buttonImport.setOnAction(importHandler);
		textFieldImportFile.setOnAction(importHandler);

		EventHandler<ActionEvent> exportHandler = exportEvent -> {
			if (!textFieldExportFile.getText().trim().isEmpty()) {

				if (socialNetwork.exportFile(textFieldExportFile.getText().trim())) {
					centralUser = socialNetwork.getUser(socialNetwork.getCentralUser());
					updateCentralUser();
					Alert exportAlert = new Alert(AlertType.INFORMATION,
							"Succesfully exported " + textFieldExportFile.getText().trim());
					exportAlert.show();
				} else {
					Alert exportAlert = new Alert(AlertType.ERROR,
							"Error exporting " + textFieldExportFile.getText().trim());
					exportAlert.show();
				}

			} else {
				Alert exportAlert = new Alert(AlertType.ERROR, "Enter file name");
				exportAlert.show();
			}

			textFieldExportFile.clear();
		};

		buttonExport.setOnAction(exportHandler);
		textFieldExportFile.setOnAction(exportHandler);

		vBoxFileFunction.getChildren().addAll(vBoxImportFile, vBoxExportFile);
		return vBoxFileFunction;
	}

	/**
	 * Sets up clear and exit functionality
	 */
	private Pane setUpClearExitSection() {
		// Components
		VBox vBoxClearExitFunction = new VBox(12);

		// Buttons
		Button buttonClearNetwork = new Button("Clear Network");
		Button buttonExitApplication = new Button("Exit Application");

		buttonClearNetwork.setStyle("");

		// Adjust Widths
		buttonClearNetwork.setMaxWidth(Double.MAX_VALUE);
		buttonExitApplication.setMaxWidth(Double.MAX_VALUE);

		// Functionality
		// Clear network function
		buttonClearNetwork.setOnAction(clearNetworkEvent -> {
			Alert exitClearAlert = new Alert(AlertType.CONFIRMATION, "Save to saveLog.txt before leaving?",
					ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

			Optional<ButtonType> option = exitClearAlert.showAndWait();

			if (option.get() == ButtonType.YES) {

				if (socialNetwork.exportFile("saveLog.txt")) {
					centralUser = socialNetwork.getUser(socialNetwork.getCentralUser());
					updateCentralUser();

					socialNetwork.removeAllUsers();
					clearNetwork();

					Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Exported Successfully. Thank you!");
					Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

					if (tyOption.get() == ButtonType.CLOSE) {
						start(PRIMARY_STAGE);
					} else {
						start(PRIMARY_STAGE);
					}

				} else {
					Alert exportAlert = new Alert(AlertType.ERROR, "Error exporting saveLog.txt");
					exportAlert.show();
				}

			} else if (option.get() == ButtonType.NO) {
				socialNetwork.removeAllUsers();
				clearNetwork();

				Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Thank you for using our app!",
						ButtonType.CLOSE);
				Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

				if (tyOption.get() == ButtonType.CLOSE) {
					start(PRIMARY_STAGE);
				} else {
					start(PRIMARY_STAGE);
				}

			} else {
				exitClearAlert.close();
			}

		});

		buttonExitApplication.setOnAction(exitNetworkEvent -> {
			Alert exitClearAlert = new Alert(AlertType.CONFIRMATION, "Save to saveLog.txt before leaving?",
					ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

			Optional<ButtonType> option = exitClearAlert.showAndWait();

			if (option.get() == ButtonType.YES) {

				if (socialNetwork.exportFile("saveLog.txt")) {
					centralUser = socialNetwork.getUser(socialNetwork.getCentralUser());
					updateCentralUser();

					socialNetwork.removeAllUsers();
					clearNetwork();

					Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Exported Successfully. Thank you!",
							ButtonType.CLOSE);
					Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

					if (tyOption.get() == ButtonType.CLOSE) {
						Platform.exit();
					} else {
						Platform.exit();
					}

				} else {
					Alert exportAlert = new Alert(AlertType.ERROR, "Error exporting saveLog.txt");
					exportAlert.show();
				}

			} else if (option.get() == ButtonType.NO) {
				socialNetwork.removeAllUsers();
				clearNetwork();

				Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Thank you for using our app!",
						ButtonType.CLOSE);
				Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

				if (tyOption.get() == ButtonType.CLOSE) {
					Platform.exit();
				} else {
					Platform.exit();
				}

			} else {
				exitClearAlert.close();
			}

		});

		vBoxClearExitFunction.getChildren().addAll(buttonClearNetwork, buttonExitApplication);
		return vBoxClearExitFunction;
	}

	private void setUpListSection() {
		// Components
		VBox vBoxList = new VBox(20);

		VBox.setVgrow(vBoxList, Priority.ALWAYS);

		// List views
		ListView<String> listViewFriends = new ListView<>(FRIENDS);
		ListView<String> listViewUsers = new ListView<>(USERS);

		friendList = listViewFriends;
		listViewFriends.setStyle("-fx-focus-color: transparent;");

		userList = listViewUsers;
		listViewUsers.setStyle("-fx-focus-color: transparent;");

		// Labels
		Label clickUser = new Label("CLICK NAME TO SET CENTRAL USER");
		clickUser.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

		Label labelNumFriends = new Label("Friends: " + FRIENDS.size());
		Label labelNumUsers = new Label("Users: " + USERS.size());
		Label labelNumGroups = new Label("Groups: " + 0);

		numFriends = labelNumFriends;
		numUsers = labelNumUsers;
		numGroups = labelNumGroups;

		// Adjust heights
		VBox.setVgrow(listViewFriends, Priority.ALWAYS);
		VBox.setVgrow(listViewUsers, Priority.ALWAYS);

		// Layouts
		VBox vBoxUserLabel = new VBox(4);
		vBoxUserLabel.getChildren().addAll(labelNumUsers, labelNumGroups);

		// Functionality
		listViewFriends.setOnMouseClicked(itemClickEvent -> {
			String item = listViewFriends.getSelectionModel().getSelectedItem();

			if (item != null) {
				socialNetwork.setCentralUser(item);
				centralUser = socialNetwork.getUser(item);
				updateCentralUser();
			}

		});

		listViewUsers.setOnMouseClicked(itemClickEvent -> {
			String item = listViewUsers.getSelectionModel().getSelectedItem();

			if (item != null) {
				socialNetwork.setCentralUser(item);
				centralUser = socialNetwork.getUser(item);
				updateCentralUser();
			}

		});
		vBoxList.getChildren().add(clickUser);
		vBoxList.getChildren().addAll(labelNumFriends, listViewFriends, vBoxUserLabel, listViewUsers);
		listSection = vBoxList;
	}

	/**
	 * Helper method to update central user
	 */
	private void updateCentralUser() {
		if (centralUser != null) {
			image.setImage(centralUser.getImage());
			name.setText(centralUser.getName());
			description.setText(centralUser.getDescription());

			image.setPreserveRatio(true);
			image.setFitHeight(100);

		} else {
			Alert centralUserAlert = new Alert(AlertType.ERROR, "Error changing central user");
			centralUserAlert.show();
			return;
		}

		updateUserList();
		updateFriendList();
		numGroups.setText("Groups: " + socialNetwork.getGroups());
	}

	/**
	 * Helper method to clear the network
	 */
	private void clearNetwork() {
		centralUser = null;
		socialNetwork = new SocialNetwork();

		USERS.clear();
		userList.setItems(USERS);
		numUsers.setText("Users: " + USERS.size());

		FRIENDS.clear();
		friendList.setItems(FRIENDS);
		numFriends.setText("Friends: " + FRIENDS.size());

		numGroups.setText("Groups: " + 0);
	}

	/**
	 * Helper method to update user list
	 */
	private void updateUserList() {
		USERS.clear();
		USERS.addAll(socialNetwork.getAllUsers());

		userList.setItems(USERS);
		numUsers.setText("Users: " + USERS.size());
		numGroups.setText("Groups: " + 0);
	}

	/**
	 * Helper method to update friend list
	 */
	private void updateFriendList() {
		FRIENDS.clear();
		FRIENDS.addAll(socialNetwork.displayNetwork(centralUser.getName()));

		removeFriendBox.setItems(FRIENDS);
		friendList.setItems(FRIENDS);
		numFriends.setText("Friends: " + FRIENDS.size());
	}

	/**
	 * Sets up a dialog bost that lists items
	 * 
	 * @param list
	 * @param header
	 */
	private void setUpListDialog(List<String> list, String header) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setHeaderText(header);

		dialog.getDialogPane().getButtonTypes().clear();
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

		ListView<String> listView = new ListView<>();

		if (list.isEmpty()) {
			listView.getItems().add("DOES NOT EXIST");
		}

		listView.getItems().addAll(list);

		dialog.getDialogPane().setContent(listView);
		Optional<ButtonType> option = dialog.showAndWait();

		if (option.get() == ButtonType.OK) {
			dialog.close();
		}
	}

	/**
	 * Creates a Dialog Box when the last user is removed. This provides an option
	 * to save in such a scenario before going back to the main log in screen
	 * 
	 */
	private void removeLastUserAndSave() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setHeaderText("You are removing the only indivdual in this Network. "
				+ "Removing this user will quit the program. Save to saveLog.txt before leaving?");

		dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);

		Optional<ButtonType> option = dialog.showAndWait();

		if (option.get() == ButtonType.YES) {
			if (socialNetwork.exportFile("saveLog.txt")) {
				centralUser = socialNetwork.getUser(socialNetwork.getCentralUser());
				updateCentralUser();

				socialNetwork.removeAllUsers();
				clearNetwork();

				Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Exported Successfully. Thank you!",
						ButtonType.CLOSE);
				Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

				if (tyOption.get() == ButtonType.CLOSE) {
					Platform.exit();
				} else {
					Platform.exit();
				}

			} else {
				Alert exportAlert = new Alert(AlertType.ERROR, "Error exporting saveLog.txt");
				exportAlert.show();
			}

		} else if (option.get() == ButtonType.NO) {
			socialNetwork.removeAllUsers();
			clearNetwork();

			Alert thankYouAlert = new Alert(AlertType.INFORMATION, "Thank you for using our app!", ButtonType.CLOSE);
			Optional<ButtonType> tyOption = thankYouAlert.showAndWait();

			if (tyOption.get() == ButtonType.CLOSE) {
				Platform.exit();
			} else {
				Platform.exit();
			}
		}
	}

	/**
	 * Helper method to check if a new user's username is valid
	 * 
	 * @param name name of user
	 * @return true if valid; false otherwise
	 */
	private boolean isValidUser(String name) {
		// Loop through all characters in name and check that each one is legal
		for (int i = 0; i < name.length(); i++) {
			Character letter = name.charAt(i);
			if (!Character.isLetter(letter) && !Character.isDigit(letter) && !letter.equals('\'')
					&& !letter.equals('_')) {
				return false;
			}
		}
		return true;
	}

}
